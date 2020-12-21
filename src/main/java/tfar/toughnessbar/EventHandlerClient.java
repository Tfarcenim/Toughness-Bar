package tfar.toughnessbar;

import com.mojang.blaze3d.matrix.MatrixStack;
import com.mojang.blaze3d.systems.RenderSystem;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.AbstractGui;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.ai.attributes.Attributes;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.MathHelper;
import net.minecraftforge.client.event.RenderGameOverlayEvent;
import net.minecraftforge.client.gui.ForgeIngameGui;
import net.minecraftforge.eventbus.api.SubscribeEvent;

import java.util.ArrayList;
import java.util.List;

import static net.minecraft.client.gui.AbstractGui.GUI_ICONS_LOCATION;

public class EventHandlerClient {
  private final ResourceLocation TEXTURE = new ResourceLocation(ToughnessBar.MOD_ID, "textures/gui/toughness.png");
  private final List<Integer> colors = new ArrayList<>();
  private static final Minecraft mc = Minecraft.getInstance();
  int lastToughness = 0;

  static Index[] indexes;

  @SubscribeEvent(receiveCanceled = true)
  public void onRenderArmorToughnessEvent(RenderGameOverlayEvent.Post event) {
    if (event.getType() == RenderGameOverlayEvent.ElementType.FOOD) {
      if (mc.getRenderViewEntity() instanceof LivingEntity) {
        LivingEntity viewEntity = (LivingEntity) mc.getRenderViewEntity();
        int armorToughness = MathHelper.floor(viewEntity.getAttribute(Attributes.ARMOR_TOUGHNESS).getValue());
        if (armorToughness < 1) {
          return;
        }
        if (colors.isEmpty()) {
          ToughnessBar.ToughnessBarConfig.ClientConfig.colorValues.get().stream().filter(hexColor -> hexColor.startsWith("#")).forEach(hexColor -> colors.add(Integer.parseInt(hexColor.substring(1), 16)));
          if (colors.isEmpty()) {
            //Add white as a default if nothing was loaded from the config. White doesn't change texture color
            colors.add(0xffffff);
          }
        }
        if (lastToughness != armorToughness) calculateIndex(armorToughness);
        int layer = (int)Math.ceil(armorToughness / 20d) - 1;
        int color = getColor(layer);
        int previous = getColor(layer - 1);

        RenderSystem.enableBlend();
        RenderSystem.pushMatrix();

        int top = mc.getMainWindow().getScaledHeight() - ForgeIngameGui.right_height;
        int right = mc.getMainWindow().getScaledWidth() / 2 + 82;
        mc.getTextureManager().bindTexture(TEXTURE);
        for (int i = 0; i < 10; i++) {
          Index index = indexes[i];
          MatrixStack stack = event.getMatrixStack();
          if (layer > 0)//toughness>20
          switch (index) {
            case empty: drawFullIcon(stack,previous,i,right,top);break;
            case half: drawSplitIcon(stack,previous,color,i,right,top);break;
            case full: drawFullIcon(stack,color,i,right,top);break;
          } else {//toughness<=20
            switch (index) {
              case empty: if (ToughnessBar.ToughnessBarConfig.ClientConfig.empty.get()) drawEmptyIcon(stack,color,i,right,top);break;
              case half: drawHalfIcon(stack,color,i,right,top);break;
              case full: drawFullIcon(stack,color,i,right,top);break;
            }
          }
        }
        ForgeIngameGui.right_height += 10;

        //Revert state
        RenderSystem.popMatrix();
        mc.getTextureManager().bindTexture(GUI_ICONS_LOCATION);
        RenderSystem.disableBlend();
      }
    }
  }

  void calculateIndex(int armorToughness) {
    indexes = new Index[]{Index.empty, Index.empty, Index.empty, Index.empty, Index.empty, Index.empty, Index.empty, Index.empty, Index.empty, Index.empty};
    int modulo = armorToughness % 20;
    if (modulo == 0){
      indexes = new Index[]{Index.full, Index.full, Index.full, Index.full, Index.full, Index.full, Index.full, Index.full, Index.full, Index.full};
      return;
    }
    int fullicons = modulo / 2;
    boolean halficon = armorToughness % 2 == 1;
    for (int i = 0; i < fullicons; i++) {
      indexes[i] = Index.full;
    }
    if (halficon) {
      indexes[fullicons] = Index.half;
    }
    this.lastToughness = armorToughness;
  }

  private int getColor(int index) {
    return index < 0 ? 0xffffff : index >= colors.size() ? colors.get(colors.size() - 1) : colors.get(index);
  }

  private void drawEmptyIcon(MatrixStack stack,int color, int i, int guiLeft, int guiTop) {
    RenderSystem.color3f((color >> 16 & 0xff) / 256f, (color >> 8 & 0xff) / 256f, (color & 0xff) / 256f);
    blit(stack,guiLeft - i * 8, guiTop, 18,27, 9, 9);
  }

  private void drawFullIcon(MatrixStack stack,int color, int i, int guiLeft, int guiTop) {
    RenderSystem.color3f((color >> 16 & 0xff) / 256f, (color >> 8 & 0xff) / 256f, (color & 0xff) / 256f);
    blit(stack,guiLeft- i * 8, guiTop, 9, 18, 9, 9);
  }

  private void drawHalfIcon(MatrixStack stack,int color, int i, int guiLeft, int guiTop) {
    RenderSystem.color3f((color >> 16 & 0xff) / 256f, (color >> 8 & 0xff) / 256f, (color & 0xff) / 256f);
    if (ToughnessBar.ToughnessBarConfig.ClientConfig.empty.get())drawEmptyIcon(stack,color,i,guiLeft,guiTop);
    blit(stack,guiLeft - i * 8, guiTop, 0, 9, 9, 9);
  }

  private void drawSplitIcon(MatrixStack stack,int color1,int color2, int i, int guiLeft, int guiTop) {
    drawFullIcon(stack,color1,i,guiLeft,guiTop);
    RenderSystem.color3f((color2 >> 16 & 0xff) / 256f, (color2 >> 8 & 0xff) / 256f, (color2 & 0xff) / 256f);
    blit(stack,guiLeft - i * 8, guiTop, 0, 9, 9, 9);
  }

  public static void blit(MatrixStack stack,int x, int y, float u, float v, int width, int height) {
    blit(stack,x,y,0,u,v,width,height,9,27);
  }

  public static void blit(MatrixStack stack,int x, int y, int z, float u, float v, int width, int height, int textureX, int textureY) {
    AbstractGui.blit(stack,x,y,z,u,v,width,height,textureX,textureY);
  }

    enum Index {
    half, full, empty
  }
}