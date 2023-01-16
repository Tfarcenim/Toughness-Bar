package tfar.toughnessbar;

import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiComponent;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraftforge.client.gui.ForgeIngameGui;
import net.minecraftforge.client.gui.IIngameOverlay;

import java.util.ArrayList;
import java.util.List;


public class EventHandlerClient {

  static ResourceLocation TEXTURE = new ResourceLocation(ToughnessBar.MOD_ID, "textures/gui/toughness.png");
  private static final List<Integer> colors = new ArrayList<>();
  private static final Minecraft mc = Minecraft.getInstance();
  static int lastToughness = 0;

  static Index[] indexes;

  static IIngameOverlay ingameOverlay = EventHandlerClient::onRenderArmorToughnessEvent;

  public static void onRenderArmorToughnessEvent(ForgeIngameGui gui, PoseStack matrices, float partialTick, int width, int height) {
    if (mc.getCameraEntity() instanceof LivingEntity viewEntity) {
      int armorToughness = Mth.floor(viewEntity.getAttribute(Attributes.ARMOR_TOUGHNESS).getValue());
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
      //RenderSystem.pushMatrix();

      int top = height - gui.right_height;
      int right = width / 2 + 82;
      bind(TEXTURE);
      for (int i = 0; i < 10; i++) {
        Index index = indexes[i];
        if (layer > 0)//toughness>20
          switch (index) {
            case empty -> drawFullIcon(matrices, previous, i, right, top);
            case half -> drawSplitIcon(matrices, previous, color, i, right, top);
            case full -> drawFullIcon(matrices, color, i, right, top);
          }
        else {//toughness<=20
          switch (index) {
            case empty: if (ToughnessBar.ToughnessBarConfig.ClientConfig.empty.get()) drawEmptyIcon(matrices,color,i,right,top);break;
            case half: drawHalfIcon(matrices,color,i,right,top);break;
            case full: drawFullIcon(matrices,color,i,right,top);break;
          }
        }
      }
      gui.right_height += 10;

      //Revert state
      //RenderSystem.popMatrix();
      bind(GuiComponent.GUI_ICONS_LOCATION);
      RenderSystem.disableBlend();
    }
  }

  public static void bind(ResourceLocation tex) {
    RenderSystem.setShaderTexture(0,tex);
  }

  public static void color(float r,float g,float b) {
    RenderSystem.setShaderColor(r,g,b,1);
  }

  static void calculateIndex(int armorToughness) {
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
    lastToughness = armorToughness;
  }

  private static int getColor(int index) {
    return index < 0 ? 0xffffff : index >= colors.size() ? colors.get(colors.size() - 1) : colors.get(index);
  }

  private static void drawEmptyIcon(PoseStack stack,int color, int i, int guiLeft, int guiTop) {
    color((color >> 16 & 0xff) / 256f, (color >> 8 & 0xff) / 256f, (color & 0xff) / 256f);
    blit(stack,guiLeft - i * 8, guiTop, 18,27, 9, 9);
  }

  private static void drawFullIcon(PoseStack stack,int color, int i, int guiLeft, int guiTop) {
    color((color >> 16 & 0xff) / 256f, (color >> 8 & 0xff) / 256f, (color & 0xff) / 256f);
    blit(stack,guiLeft- i * 8, guiTop, 9, 18, 9, 9);
  }

  private static void drawHalfIcon(PoseStack stack,int color, int i, int guiLeft, int guiTop) {
    color((color >> 16 & 0xff) / 256f, (color >> 8 & 0xff) / 256f, (color & 0xff) / 256f);
    if (ToughnessBar.ToughnessBarConfig.ClientConfig.empty.get())drawEmptyIcon(stack,color,i,guiLeft,guiTop);
    blit(stack,guiLeft - i * 8, guiTop, 0, 9, 9, 9);
  }

  private static void drawSplitIcon(PoseStack stack,int color1,int color2, int i, int guiLeft, int guiTop) {
    drawFullIcon(stack,color1,i,guiLeft,guiTop);
    color((color2 >> 16 & 0xff) / 256f, (color2 >> 8 & 0xff) / 256f, (color2 & 0xff) / 256f);
    blit(stack,guiLeft - i * 8, guiTop, 0, 9, 9, 9);
  }

  public static void blit(PoseStack stack,int x, int y, float u, float v, int width, int height) {
    blit(stack,x,y,0,u,v,width,height,9,27);
  }

  public static void blit(PoseStack stack,int x, int y, int z, float u, float v, int width, int height, int textureX, int textureY) {
    GuiComponent.blit(stack,x,y,z,u,v,width,height,textureY,textureX);
  }

    enum Index {
    half, full, empty
  }
}