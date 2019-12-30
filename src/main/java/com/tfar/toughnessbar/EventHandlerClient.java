package com.tfar.toughnessbar;

import com.mojang.blaze3d.systems.RenderSystem;
import com.tfar.toughnessbar.ToughnessBar;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.AbstractGui;
import net.minecraft.client.gui.IngameGui;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.SharedMonsterAttributes;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.MathHelper;
import net.minecraftforge.client.event.RenderGameOverlayEvent;
import net.minecraftforge.client.gui.ForgeIngameGui;
import net.minecraftforge.eventbus.api.SubscribeEvent;

import java.util.ArrayList;
import java.util.List;

import static com.tfar.toughnessbar.ToughnessBarConfig.ClientConfig.*;
import static net.minecraft.client.gui.AbstractGui.GUI_ICONS_LOCATION;

public class EventHandlerClient {
  private final ResourceLocation TEXTURE = new ResourceLocation(ToughnessBar.MOD_ID, "textures/gui/toughness.png");
  private final List<Integer> colors = new ArrayList<>();
  private static final Minecraft mc = Minecraft.getInstance();
  int lastToughness = 0;

  static Index[] indexes = new Index[]{Index.empty, Index.empty, Index.empty, Index.empty, Index.empty, Index.empty, Index.empty, Index.empty, Index.empty, Index.empty};

  @SubscribeEvent(receiveCanceled = true)
  public void onRenderArmorToughnessEvent(RenderGameOverlayEvent.Post event) {
    if (event.getType() == RenderGameOverlayEvent.ElementType.FOOD) {
      if (mc.getRenderViewEntity() instanceof LivingEntity) {
        LivingEntity viewEntity = (LivingEntity) mc.getRenderViewEntity();
        int armorToughness = MathHelper.floor(viewEntity.getAttribute(SharedMonsterAttributes.ARMOR_TOUGHNESS).getValue());
        if (armorToughness < 1) {
          return;
        }
        if (colors.isEmpty()) {
          colorValues.get().stream().filter(hexColor -> hexColor.startsWith("#")).forEach(hexColor -> colors.add(Integer.parseInt(hexColor.substring(1), 16)));
          if (colors.isEmpty()) {
            //Add white as a default if nothing was loaded from the config. White doesn't change texture color
            colors.add(0);
          }
        }
        if (lastToughness != armorToughness) calculateIndex(armorToughness);
        int layer = armorToughness / 20;
        int color = getColor(layer);
        int previous = getColor(layer - 1);

        RenderSystem.enableBlend();
        RenderSystem.pushMatrix();

        int top = mc.func_228018_at_().getScaledHeight() - ForgeIngameGui.right_height;
        int right = mc.func_228018_at_().getScaledWidth() / 2 + 82;
        mc.getTextureManager().bindTexture(TEXTURE);
        for (int i = 0; i < 10; i++) {
          Index index = indexes[i];
          switch (index){
            case full:
            case half:
            case empty:
          }
        }
        0xf000f0;
        ForgeIngameGui.right_height += 10;

        //Revert state
        RenderSystem.popMatrix();

        mc.getTextureManager().bindTexture(GUI_ICONS_LOCATION);
        RenderSystem.disableBlend();
      }
    }
  }

  void calculateIndex(int armorToughness) {
    int layer = armorToughness / 20;

    if (layer >= indexes.length) {
      return;
    }
    indexes = new Index[]{Index.empty, Index.empty, Index.empty, Index.empty, Index.empty, Index.empty, Index.empty, Index.empty, Index.empty, Index.empty};
    int modulo = armorToughness % 20;
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
    if (index < 0) {
      return 0xffffff;
    } else if (index >= colors.size()) {
      return showBedrock.get() ? 0xffffff : colors.get(colors.size() - 1);
    }
    return colors.get(index);
  }



  private void drawIcon(int color, int i,Index index, int guiLeft, int guiTop) {
    RenderSystem.color3f((color >> 16 & 0xff) / 256f, (color >> 8 & 0xff) / 256f, (color & 0xff) / 256f);
    blit(guiLeft, guiTop, 0, index.ordinal(), index.ordinal()+1, 9, 9, 9,9);
  }

  public static void blit(int x, int y, int z, float u, float v, int width, int height) {
    blit(x,y,z,u,v,width,height,9,9);
  }

  public static void blit(int x, int y, int z, float u, float v, int width, int height, int textureX, int textureY) {
    AbstractGui.blit(x,y,z,u,v,width,height,textureX,textureY);
  }

    enum Index {
    half, full, empty,
    halfcap, cap
  }
}