package com.tfar.toughnessbar.client;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Gui;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.SharedMonsterAttributes;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.MathHelper;
import net.minecraftforge.client.GuiIngameForge;
import net.minecraftforge.client.event.RenderGameOverlayEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

public class EventHandlerClient {
    private static ResourceLocation TOUGHNESS_ICON1 = new ResourceLocation("toughnessbar:textures/gui/icon1.png");
    private static ResourceLocation TOUGHNESS_ICON2 = new ResourceLocation("toughnessbar:textures/gui/icon2.png");
    private static ResourceLocation TOUGHNESS_ICON3 = new ResourceLocation("toughnessbar:textures/gui/icon3.png");
    private static ResourceLocation TOUGHNESS_ICON4 = new ResourceLocation("toughnessbar:textures/gui/icon4.png");
    private static ResourceLocation TOUGHNESS_ICON5 = new ResourceLocation("toughnessbar:textures/gui/icon5.png");
    private static ResourceLocation TOUGHNESS_ICON6 = new ResourceLocation("toughnessbar:textures/gui/icon6.png");
    private static ResourceLocation TOUGHNESS_ICON7 = new ResourceLocation("toughnessbar:textures/gui/icon7.png");
    private static ResourceLocation TOUGHNESS_ICON8 = new ResourceLocation("toughnessbar:textures/gui/icon8.png");

    @SubscribeEvent
    public void onRenderArmorToughnessEvent(RenderGameOverlayEvent.Post event) {
        if (event.getType() == RenderGameOverlayEvent.ElementType.FOOD) {
            if (Minecraft.getMinecraft().getRenderViewEntity() instanceof EntityLivingBase) {
                EntityLivingBase viewEntity = (EntityLivingBase) Minecraft.getMinecraft().getRenderViewEntity();
                int armorToughness = MathHelper.floor(viewEntity.getEntityAttribute(SharedMonsterAttributes.ARMOR_TOUGHNESS).getAttributeValue());
                int width = event.getResolution().getScaledWidth();
                int height = event.getResolution().getScaledHeight();
                if (armorToughness > 0 && armorToughness < 21) {
                    render(width, height, armorToughness, TOUGHNESS_ICON1);
                } else if (armorToughness > 20 && armorToughness < 41) {
                    render(width, height, armorToughness - 20, TOUGHNESS_ICON2);
                } else if (armorToughness > 40 && armorToughness < 61) {
                    render(width, height, armorToughness - 40, TOUGHNESS_ICON3);
                } else if (armorToughness > 60 && armorToughness < 81) {
                    render(width, height, armorToughness - 60, TOUGHNESS_ICON4);
                } else if (armorToughness > 80 && armorToughness < 101) {
                    render(width, height, armorToughness - 80, TOUGHNESS_ICON5);
                } else if (armorToughness > 100 && armorToughness < 121) {
                    render(width, height, armorToughness - 100, TOUGHNESS_ICON6);
                } else if (armorToughness > 120 && armorToughness < 141) {
                    render(width, height, armorToughness - 120, TOUGHNESS_ICON7);
                } else if (armorToughness > 140) {
                    render(width, height, armorToughness - 140, TOUGHNESS_ICON8);
                }
            }
        }
    }

    private void render(int width, int height, int armorToughness, ResourceLocation texture) {
        Minecraft.getMinecraft().getTextureManager().bindTexture(texture);
        GlStateManager.enableBlend();

        int toughnessRows = MathHelper.ceil(armorToughness / 20.0F);
        int rowHeight = Math.min(Math.max(10 - (toughnessRows - 2), 3), 10);

        int top = (height - GuiIngameForge.right_height) - ((toughnessRows * rowHeight) - 10);
        for (int i = toughnessRows - 1; i >= 0; i--) {
            int right = width / 2 + 82;
            int mult = 20 * i;
            for (int j = 1; j < 20; j += 2) {
                int x = 0;
                if (mult + j < armorToughness) {
                    x = 18;
                } else if (mult + j == armorToughness) {
                    x = 9;
                } //else if (mult + j > armorToughness) x = 0
                Gui.drawModalRectWithCustomSizedTexture(right, top, x, 0, 9, 9, 27, 9);
                right -= 8;
            }
            top += rowHeight;
            GuiIngameForge.right_height += rowHeight;
        }
        if (rowHeight < 10) {
            GuiIngameForge.right_height += (10 - rowHeight);
        }

        Minecraft.getMinecraft().getTextureManager().bindTexture(Gui.ICONS);
        GlStateManager.disableBlend();
    }
}