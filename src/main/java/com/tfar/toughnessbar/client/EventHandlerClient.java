package com.tfar.toughnessbar.client;

import com.mojang.blaze3d.platform.GlStateManager;
import com.tfar.toughnessbar.ToughnessBar;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.IngameGui;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.SharedMonsterAttributes;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.MathHelper;
import net.minecraftforge.client.ForgeIngameGui;
import net.minecraftforge.client.event.RenderGameOverlayEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;

import java.util.ArrayList;
import java.util.List;

import static com.tfar.toughnessbar.ToughnessBarConfig.ClientConfig.*;
import static net.minecraft.client.gui.AbstractGui.GUI_ICONS_LOCATION;

public class EventHandlerClient {
  private final ResourceLocation EMPTY = new ResourceLocation(ToughnessBar.MOD_ID, "textures/gui/empty.png");
  private final ResourceLocation HALF = new ResourceLocation(ToughnessBar.MOD_ID, "textures/gui/half.png");
  private final ResourceLocation FULL = new ResourceLocation(ToughnessBar.MOD_ID, "textures/gui/full.png");
  private final ResourceLocation HALF_CAPPED = new ResourceLocation(ToughnessBar.MOD_ID, "textures/gui/half_capped.png");
  private final ResourceLocation CAPPED = new ResourceLocation(ToughnessBar.MOD_ID, "textures/gui/capped.png");
  private final List<Integer> colors = new ArrayList<>();
  private final Minecraft mc = Minecraft.getInstance();


  @SubscribeEvent
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
        armorToughness--;
        int index = armorToughness / 20;
        armorToughness = armorToughness % 20;
        armorToughness++;
        ToughnessColor color = getColor(index);
        ToughnessColor previous = getColor(index - 1);
        ResourceLocation lastTexture = null;

        GlStateManager.enableBlend();
        GlStateManager.pushMatrix();

        int top = mc.mainWindow.getScaledHeight() - ForgeIngameGui.right_height;
        int right = mc.mainWindow.getScaledWidth() / 2 + 82;
        for (int i = 1; i < 20; i += 2) {
          if (isCapped(index)) {
            //The entire bar is capped
            lastTexture = fullIcon(CAPPED, previous, lastTexture, right, top, 9);
          } else if (i < armorToughness) {
            //Full
            lastTexture = fullIcon(isCapped(index) ? CAPPED : FULL, color, lastTexture, right, top, 9);
          } else //if (i > armorToughness)
            //Empty
            if (i == armorToughness) {
              //Half
              lastTexture = halfIcon(isCapped(index) ? HALF_CAPPED : HALF, color, previous, lastTexture, right, top,index);
            } else if (empty.get() || index > 0)
              lastTexture = fullIcon(isEmpty(index) ? EMPTY : FULL, previous, lastTexture, right, top, 9);
          right -= 8;
        }
        ForgeIngameGui.right_height += 10;

        //Revert state
        GlStateManager.popMatrix();

        mc.getTextureManager().bindTexture(GUI_ICONS_LOCATION);
        GlStateManager.disableBlend();
      }
    }
  }

  private ToughnessColor getColor(int index) {
    if (index < 0) {
      return WHITE;
    } else if (index >= colors.size()) {
      return showBedrock.get() ? WHITE : new ToughnessColor(colors.get(colors.size() - 1));
    }
    return new ToughnessColor(colors.get(index));
  }

  private static final ToughnessColor WHITE = new ToughnessColor(0xFFFFFF);

  private ResourceLocation fullIcon(ResourceLocation icon, ToughnessColor color, ResourceLocation lastIcon, int right, int top, int width) {
    if (!icon.equals(lastIcon)) {
      mc.getTextureManager().bindTexture(icon);
    }
    GlStateManager.color3f(color.getRed(), color.getGreen(), color.getBlue());
    IngameGui.blit(right, top, 0, 0, width, 9, 9, 9);
    return icon;
  }

  private ResourceLocation halfIcon(ResourceLocation icon, ToughnessColor color, ToughnessColor previous, ResourceLocation lastIcon, int right, int top, int index) {
    //Previous tier's half icon
    fullIcon(isEmpty(index) ? EMPTY : FULL, previous, lastIcon, right, top, 4);

    //This ones half icon
    mc.getTextureManager().bindTexture(icon);
    GlStateManager.color3f(color.getRed(), color.getGreen(), color.getBlue());
    IngameGui.blit(right + 4, top, 0, 0, 5, 9, 5, 9);
    return icon;
  }

  private boolean isEmpty(int index){
    return index < 0;
  }

  private boolean isCapped(int index){
    return index > colors.size();
  }

  private static class ToughnessColor {

    public final int color;

    private ToughnessColor(int color) {
      this.color = color;
    }

    private float getRed() {
      return ((color & 0xFF0000) >> 16)/256f;
    }

    private float getBlue() {
      return ((color & 0x00FF00) >> 8)/256f;

    }

    private float getGreen() {
      return (color & 0x0000FF) /256f;
    }
  }
}