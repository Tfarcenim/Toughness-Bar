package com.tfar.toughnessbar.client;

import com.tfar.toughnessbar.ToughnessBar;
import com.tfar.toughnessbar.ToughnessBarConfig;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Gui;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.SharedMonsterAttributes;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.MathHelper;
import net.minecraftforge.client.GuiIngameForge;
import net.minecraftforge.client.event.RenderGameOverlayEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;

import java.awt.Color;
import java.util.ArrayList;
import java.util.List;

import static com.tfar.toughnessbar.ToughnessBarConfig.*;

public class EventHandlerClient {
  private final ResourceLocation EMPTY = new ResourceLocation(ToughnessBar.MOD_ID, "textures/gui/empty.png");
  private final ResourceLocation HALF = new ResourceLocation(ToughnessBar.MOD_ID, "textures/gui/half.png");
  private final ResourceLocation FULL = new ResourceLocation(ToughnessBar.MOD_ID, "textures/gui/full.png");
  private final ResourceLocation HALF_CAPPED = new ResourceLocation(ToughnessBar.MOD_ID, "textures/gui/half_capped.png");
  private final ResourceLocation CAPPED = new ResourceLocation(ToughnessBar.MOD_ID, "textures/gui/capped.png");
  private final List<Color> colors = new ArrayList<>();
  private final Minecraft mc = Minecraft.getInstance();


  @SubscribeEvent
  public void onRenderArmorToughnessEvent(RenderGameOverlayEvent.Post event) {
    if (event.getType() == RenderGameOverlayEvent.ElementType.FOOD) {
      if (mc.getRenderViewEntity() instanceof EntityLivingBase) {
        EntityLivingBase viewEntity = (EntityLivingBase) mc.getRenderViewEntity();
        int armorToughness = MathHelper.floor(viewEntity.getAttribute(SharedMonsterAttributes.ARMOR_TOUGHNESS).getValue());
        if (armorToughness <= 0) {
          return;
        }
        if (colors.isEmpty()) {
          for (String hexColor : ToughnessBarConfig.colorValues) {
            if (hexColor.startsWith("#")) {
              try {
                colors.add(new Color(Integer.parseInt(hexColor.substring(1), 16)));
              } catch (Exception ignored) {
              }
            }
          }

          if (colors.isEmpty()) {
            //Add white as a default if nothing was loaded from the config. White doesn't change texture color
            colors.add(Color.WHITE);
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

        int top = mc.mainWindow.getScaledHeight() - GuiIngameForge.right_height;
        int right = mc.mainWindow.getScaledWidth() / 2 + 82;
        for (int i = 1; i < 20; i += 2) {
          if (previous.isCapped()) {
            //The entire bar is capped
            lastTexture = fullIcon(CAPPED, previous, lastTexture, right, top, 9);
          } else if (i < armorToughness) {
            //Full
            lastTexture = fullIcon(color.isCapped() ? CAPPED : FULL, color, lastTexture, right, top, 9);
          } else //if (i > armorToughness)
            //Empty
            if (i == armorToughness) {
              //Half
              lastTexture = halfIcon(color.isCapped() ? HALF_CAPPED : HALF, color, previous, lastTexture, right, top);
            } else if (empty || index > 0)
              lastTexture = fullIcon(previous.isEmpty() ? EMPTY : FULL, previous, lastTexture, right, top, 9);
          right -= 8;
        }
        GuiIngameForge.right_height += 10;

        //Revert state
        GlStateManager.popMatrix();

        mc.getTextureManager().bindTexture(Gui.ICONS);
        GlStateManager.disableBlend();
      }
    }
  }

  private ToughnessColor getColor(int index) {
    if (index < 0) {
      return new ToughnessColor(true);
    } else if (index >= colors.size()) {
      return bedrock ? new ToughnessColor(false) : new ToughnessColor(colors.get(colors.size() - 1));
    }
    return new ToughnessColor(colors.get(index));
  }

  private ResourceLocation fullIcon(ResourceLocation icon, ToughnessColor color, ResourceLocation lastIcon, int right, int top, int width) {
    if (!icon.equals(lastIcon)) {
      mc.getTextureManager().bindTexture(icon);
    }
    GlStateManager.color4f(color.getRed(), color.getGreen(), color.getBlue(), color.getAlpha());
    Gui.drawModalRectWithCustomSizedTexture(right, top, 0, 0, width, 9, 9, 9);
    return icon;
  }

  private ResourceLocation halfIcon(ResourceLocation icon, ToughnessColor color, ToughnessColor previous, ResourceLocation lastIcon, int right, int top) {
    //Previous tier's half icon
    fullIcon(previous.isEmpty() ? EMPTY : FULL, previous, lastIcon, right, top, 4);

    //This ones half icon
    mc.getTextureManager().bindTexture(icon);
    GlStateManager.color4f(color.getRed(), color.getGreen(), color.getBlue(), color.getAlpha());
    Gui.drawModalRectWithCustomSizedTexture(right + 4, top, 0, 0, 5, 9, 5, 9);
    return icon;
  }

  private class ToughnessColor {
    private Color color;
    //Empty icon or capped icon. Only is used if color is null
    private boolean empty = true;

    private ToughnessColor(Color color) {
      this.color = color;
    }

    private ToughnessColor(boolean empty) {
      this.empty = empty;
    }

    private boolean isEmpty() {
      return color == null && empty;
    }

    private boolean isCapped() {
      return color == null && !empty;
    }

    private float getRed() {
      return color == null ? empty ? colors.get(0).getRed() : 1 : color.getRed() / 256F;
    }

    private float getBlue() {
      return color == null ? empty ? colors.get(0).getBlue() : 1 : color.getBlue() / 256F;
    }

    private float getGreen() {
      return color == null ? empty ? colors.get(0).getGreen() : 1 : color.getGreen() / 256F;
    }

    private float getAlpha() {
      return color == null ? empty ? colors.get(0).getAlpha() : 1 : color.getAlpha() / 256F;
    }
  }
}