package com.tfar.toughnessbar.client;

import com.tfar.toughnessbar.ToughnessBarConfig;
import com.tfar.toughnessbar.ToughnessBarConstants;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Gui;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.SharedMonsterAttributes;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.MathHelper;
import net.minecraftforge.client.GuiIngameForge;
import net.minecraftforge.client.event.RenderGameOverlayEvent;
import net.minecraftforge.common.config.Config;
import net.minecraftforge.common.config.ConfigManager;
import net.minecraftforge.fml.client.event.ConfigChangedEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import org.lwjgl.opengl.GL11;

import java.awt.Color;
import java.util.ArrayList;
import java.util.List;

import static com.tfar.toughnessbar.ToughnessBarConfig.showEmptyArmorToughnessIcons;

public class EventHandlerClient {
    private final ResourceLocation EMPTY = new ResourceLocation(ToughnessBarConstants.MOD_ID, "textures/gui/empty.png");
    private final ResourceLocation HALF = new ResourceLocation(ToughnessBarConstants.MOD_ID, "textures/gui/half.png");
    private final ResourceLocation FULL = new ResourceLocation(ToughnessBarConstants.MOD_ID, "textures/gui/full.png");
    private final ResourceLocation HALF_CAPPED = new ResourceLocation(ToughnessBarConstants.MOD_ID, "textures/gui/half_capped.png");
    private final ResourceLocation CAPPED = new ResourceLocation(ToughnessBarConstants.MOD_ID, "textures/gui/capped.png");
    private final List<Color> colors = new ArrayList<>();

    @SubscribeEvent
    public void onConfigChanged(ConfigChangedEvent.OnConfigChangedEvent event) {
        //Only process events for this mod
        if (event.getModID().equals(ToughnessBarConstants.MOD_ID)) {
            ConfigManager.sync(ToughnessBarConstants.MOD_ID, Config.Type.INSTANCE);
            colors.clear();
        }
    }

    @SubscribeEvent
    public void onRenderArmorToughnessEvent(RenderGameOverlayEvent.Post event) {
        if (event.getType() == RenderGameOverlayEvent.ElementType.FOOD) {
            if (Minecraft.getMinecraft().getRenderViewEntity() instanceof EntityLivingBase) {
                EntityLivingBase viewEntity = (EntityLivingBase) Minecraft.getMinecraft().getRenderViewEntity();
                int armorToughness = MathHelper.floor(viewEntity.getEntityAttribute(SharedMonsterAttributes.ARMOR_TOUGHNESS).getAttributeValue());
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
                GL11.glPushMatrix();
                GL11.glPushAttrib(GL11.GL_ALL_ATTRIB_BITS);

                int top = event.getResolution().getScaledHeight() - GuiIngameForge.right_height;
                int right = event.getResolution().getScaledWidth() / 2 + 82;
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
                    } else if (showEmptyArmorToughnessIcons || index > 0)
                            lastTexture = fullIcon(previous.isEmpty() ? EMPTY : FULL, previous, lastTexture, right, top, 9);
                    right -= 8;
                }
                GuiIngameForge.right_height += 10;

                //Revert state
                GL11.glPopMatrix();
                GL11.glPopAttrib();

                Minecraft.getMinecraft().getTextureManager().bindTexture(Gui.ICONS);
                GlStateManager.disableBlend();
            }
        }
    }

    private ToughnessColor getColor(int index) {
        if (index < 0) {
            return new ToughnessColor(true);
        } else if (index >= colors.size()) {
            return new ToughnessColor(false);
        }
        return new ToughnessColor(colors.get(index));
    }

    private ResourceLocation fullIcon(ResourceLocation icon, ToughnessColor color, ResourceLocation lastIcon, int right, int top, int width) {
        if (!icon.equals(lastIcon)) {
            Minecraft.getMinecraft().getTextureManager().bindTexture(icon);
        }
        GL11.glColor4f(color.getRed(), color.getGreen(), color.getBlue(), color.getAlpha());
        Gui.drawModalRectWithCustomSizedTexture(right, top, 0, 0, width, 9, 9, 9);
        return icon;
    }

    private ResourceLocation halfIcon(ResourceLocation icon, ToughnessColor color, ToughnessColor previous, ResourceLocation lastIcon, int right, int top) {
        //Previous tier's half icon
        fullIcon(previous.isEmpty() ? EMPTY : FULL, previous, lastIcon, right, top, 4);

        //This ones half icon
        Minecraft.getMinecraft().getTextureManager().bindTexture(icon);
        GL11.glColor4f(color.getRed(), color.getGreen(), color.getBlue(), color.getAlpha());
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
            return color == null ? 1 : color.getRed() / 256F;
        }

        private float getBlue() {
            return color == null ? 1 : color.getBlue() / 256F;
        }

        private float getGreen() {
            return color == null ? 1 : color.getGreen() / 256F;
        }

        private float getAlpha() {
            return color == null ? 1 : color.getAlpha() / 256F;
        }
    }
}