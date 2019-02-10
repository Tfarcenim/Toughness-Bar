package com.tfar.toughnessbar.client;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Gui;
import net.minecraft.client.gui.GuiIngame;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.SharedMonsterAttributes;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.MathHelper;
import net.minecraftforge.client.GuiIngameForge;
import net.minecraftforge.client.event.RenderGameOverlayEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public class EventHandlerClient {

    public static ResourceLocation TOUGHNESS_ICON1 = new ResourceLocation("toughnessbar:textures/gui/icon1.png");
    public static ResourceLocation TOUGHNESS_ICON2 = new ResourceLocation("toughnessbar:textures/gui/icon2.png");
    public static ResourceLocation TOUGHNESS_ICON3 = new ResourceLocation("toughnessbar:textures/gui/icon3.png");
    public static ResourceLocation TOUGHNESS_ICON4 = new ResourceLocation("toughnessbar:textures/gui/icon4.png");
    public static ResourceLocation TOUGHNESS_ICON5 = new ResourceLocation("toughnessbar:textures/gui/icon5.png");
    public static ResourceLocation TOUGHNESS_ICON6 = new ResourceLocation("toughnessbar:textures/gui/icon6.png");
    public static ResourceLocation TOUGHNESS_ICON7 = new ResourceLocation("toughnessbar:textures/gui/icon7.png");
    public static ResourceLocation TOUGHNESS_ICON8 = new ResourceLocation("toughnessbar:textures/gui/icon8.png");

    @SideOnly(Side.CLIENT)
    @SubscribeEvent
    public void onRenderArmorToughnessEvent(RenderGameOverlayEvent.Post event) {
        if (event.getType() == RenderGameOverlayEvent.ElementType.FOOD) {
            if (Minecraft.getMinecraft().getRenderViewEntity() instanceof EntityLivingBase) {
                EntityLivingBase viewEntity = (EntityLivingBase) Minecraft.getMinecraft().getRenderViewEntity();
                int armorToughness = MathHelper.floor(viewEntity.getEntityAttribute(SharedMonsterAttributes.ARMOR_TOUGHNESS).getAttributeValue());
                if (armorToughness > 0 && armorToughness <21) {

                    int width = event.getResolution().getScaledWidth();
                    int height = event.getResolution().getScaledHeight();
                    GuiIngame gui = Minecraft.getMinecraft().ingameGUI;
                    Minecraft.getMinecraft().getTextureManager().bindTexture(TOUGHNESS_ICON1);
                    GlStateManager.enableBlend();

                    int toughnessRows = MathHelper.ceil(armorToughness / 20.0F);
                    int rowHeight = Math.min(Math.max(10 - (toughnessRows - 2), 3), 10);

                    int top = (height - GuiIngameForge.right_height) - ((toughnessRows * rowHeight) - 10);
                    for (int i = toughnessRows - 1; i >= 0; i--) {
                        int right = width / 2 + 82;
                        for (int j = 1; j < 20; j += 2) {
                            if (j + (i * 20) < armorToughness) {
                                gui.drawTexturedModalRect(right, top, 34, 0, 9, 9);
                            } else if (j + (i * 20) == armorToughness) {
                                gui.drawTexturedModalRect(right, top, 25, 0, 9, 9);
                            } else if (j + (i * 20) > armorToughness) {
                                gui.drawTexturedModalRect(right, top, 16, 0, 9, 9);
                            }
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
                } else if (armorToughness > 20 && armorToughness < 41) {
                    int armorToughness2 = armorToughness-20;
                    int width = event.getResolution().getScaledWidth();
                    int height = event.getResolution().getScaledHeight();
                    GuiIngame gui = Minecraft.getMinecraft().ingameGUI;
                    Minecraft.getMinecraft().getTextureManager().bindTexture(TOUGHNESS_ICON2);
                    GlStateManager.enableBlend();

                    int toughnessRows = MathHelper.ceil(armorToughness2 / 20.0F);
                    int rowHeight = Math.min(Math.max(10 - (toughnessRows - 2), 3), 10);

                    int top = (height - GuiIngameForge.right_height) - ((toughnessRows * rowHeight) - 10);
                    for (int i = toughnessRows - 1; i >= 0; i--) {
                        int right = width / 2 + 82;
                        for (int j = 1; j < 20; j += 2) {
                            if (j + (i * 20) < armorToughness2) {
                                gui.drawTexturedModalRect(right, top, 34, 0, 9, 9);
                            } else if (j + (i * 20) == armorToughness2) {
                                gui.drawTexturedModalRect(right, top, 25, 0, 9, 9);
                            } else if (j + (i * 20) > armorToughness2) {
                                gui.drawTexturedModalRect(right, top, 16, 0, 9, 9);
                            }
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

                }else if (armorToughness > 40 && armorToughness < 61) {
                    int armorToughness2 = armorToughness-40;
                    int width = event.getResolution().getScaledWidth();
                    int height = event.getResolution().getScaledHeight();
                    GuiIngame gui = Minecraft.getMinecraft().ingameGUI;
                    Minecraft.getMinecraft().getTextureManager().bindTexture(TOUGHNESS_ICON3);
                    GlStateManager.enableBlend();

                    int toughnessRows = MathHelper.ceil(armorToughness2 / 20.0F);
                    int rowHeight = Math.min(Math.max(10 - (toughnessRows - 2), 3), 10);

                    int top = (height - GuiIngameForge.right_height) - ((toughnessRows * rowHeight) - 10);
                    for (int i = toughnessRows - 1; i >= 0; i--) {
                        int right = width / 2 + 82;
                        for (int j = 1; j < 20; j += 2) {
                            if (j + (i * 20) < armorToughness2) {
                                gui.drawTexturedModalRect(right, top, 34, 0, 9, 9);
                            } else if (j + (i * 20) == armorToughness2) {
                                gui.drawTexturedModalRect(right, top, 25, 0, 9, 9);
                            } else if (j + (i * 20) > armorToughness2) {
                                gui.drawTexturedModalRect(right, top, 16, 0, 9, 9);
                            }
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
                else if (armorToughness > 60 && armorToughness < 81) {
                    int armorToughness2 = armorToughness-60;
                    int width = event.getResolution().getScaledWidth();
                    int height = event.getResolution().getScaledHeight();
                    GuiIngame gui = Minecraft.getMinecraft().ingameGUI;
                    Minecraft.getMinecraft().getTextureManager().bindTexture(TOUGHNESS_ICON4);
                    GlStateManager.enableBlend();

                    int toughnessRows = MathHelper.ceil(armorToughness2 / 20.0F);
                    int rowHeight = Math.min(Math.max(10 - (toughnessRows - 2), 3), 10);

                    int top = (height - GuiIngameForge.right_height) - ((toughnessRows * rowHeight) - 10);
                    for (int i = toughnessRows - 1; i >= 0; i--) {
                        int right = width / 2 + 82;
                        for (int j = 1; j < 20; j += 2) {
                            if (j + (i * 20) < armorToughness2) {
                                gui.drawTexturedModalRect(right, top, 34, 0, 9, 9);
                            } else if (j + (i * 20) == armorToughness2) {
                                gui.drawTexturedModalRect(right, top, 25, 0, 9, 9);
                            } else if (j + (i * 20) > armorToughness2) {
                                gui.drawTexturedModalRect(right, top, 16, 0, 9, 9);
                            }
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
                else if (armorToughness > 80 && armorToughness < 101) {
                    int armorToughness2 = armorToughness-80;
                    int width = event.getResolution().getScaledWidth();
                    int height = event.getResolution().getScaledHeight();
                    GuiIngame gui = Minecraft.getMinecraft().ingameGUI;
                    Minecraft.getMinecraft().getTextureManager().bindTexture(TOUGHNESS_ICON5);
                    GlStateManager.enableBlend();

                    int toughnessRows = MathHelper.ceil(armorToughness2 / 20.0F);
                    int rowHeight = Math.min(Math.max(10 - (toughnessRows - 2), 3), 10);

                    int top = (height - GuiIngameForge.right_height) - ((toughnessRows * rowHeight) - 10);
                    for (int i = toughnessRows - 1; i >= 0; i--) {
                        int right = width / 2 + 82;
                        for (int j = 1; j < 20; j += 2) {
                            if (j + (i * 20) < armorToughness2) {
                                gui.drawTexturedModalRect(right, top, 34, 0, 9, 9);
                            } else if (j + (i * 20) == armorToughness2) {
                                gui.drawTexturedModalRect(right, top, 25, 0, 9, 9);
                            } else if (j + (i * 20) > armorToughness2) {
                                gui.drawTexturedModalRect(right, top, 16, 0, 9, 9);
                            }
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
                else if (armorToughness > 100 && armorToughness < 121) {
                    int armorToughness2 = armorToughness-100;
                    int width = event.getResolution().getScaledWidth();
                    int height = event.getResolution().getScaledHeight();
                    GuiIngame gui = Minecraft.getMinecraft().ingameGUI;
                    Minecraft.getMinecraft().getTextureManager().bindTexture(TOUGHNESS_ICON6);
                    GlStateManager.enableBlend();

                    int toughnessRows = MathHelper.ceil(armorToughness2 / 20.0F);
                    int rowHeight = Math.min(Math.max(10 - (toughnessRows - 2), 3), 10);

                    int top = (height - GuiIngameForge.right_height) - ((toughnessRows * rowHeight) - 10);
                    for (int i = toughnessRows - 1; i >= 0; i--) {
                        int right = width / 2 + 82;
                        for (int j = 1; j < 20; j += 2) {
                            if (j + (i * 20) < armorToughness2) {
                                gui.drawTexturedModalRect(right, top, 34, 0, 9, 9);
                            } else if (j + (i * 20) == armorToughness2) {
                                gui.drawTexturedModalRect(right, top, 25, 0, 9, 9);
                            } else if (j + (i * 20) > armorToughness2) {
                                gui.drawTexturedModalRect(right, top, 16, 0, 9, 9);
                            }
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
                else if (armorToughness > 120 && armorToughness < 141) {
                    int armorToughness2 = armorToughness-120;
                    int width = event.getResolution().getScaledWidth();
                    int height = event.getResolution().getScaledHeight();
                    GuiIngame gui = Minecraft.getMinecraft().ingameGUI;
                    Minecraft.getMinecraft().getTextureManager().bindTexture(TOUGHNESS_ICON7);
                    GlStateManager.enableBlend();

                    int toughnessRows = MathHelper.ceil(armorToughness2 / 20.0F);
                    int rowHeight = Math.min(Math.max(10 - (toughnessRows - 2), 3), 10);

                    int top = (height - GuiIngameForge.right_height) - ((toughnessRows * rowHeight) - 10);
                    for (int i = toughnessRows - 1; i >= 0; i--) {
                        int right = width / 2 + 82;
                        for (int j = 1; j < 20; j += 2) {
                            if (j + (i * 20) < armorToughness2) {
                                gui.drawTexturedModalRect(right, top, 34, 0, 9, 9);
                            } else if (j + (i * 20) == armorToughness2) {
                                gui.drawTexturedModalRect(right, top, 25, 0, 9, 9);
                            } else if (j + (i * 20) > armorToughness2) {
                                gui.drawTexturedModalRect(right, top, 16, 0, 9, 9);
                            }
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
                }   else if (armorToughness > 140) {
                    int armorToughness2 = armorToughness-140;
                    int width = event.getResolution().getScaledWidth();
                    int height = event.getResolution().getScaledHeight();
                    GuiIngame gui = Minecraft.getMinecraft().ingameGUI;
                    Minecraft.getMinecraft().getTextureManager().bindTexture(TOUGHNESS_ICON8);
                    GlStateManager.enableBlend();

                    int toughnessRows = MathHelper.ceil(armorToughness2 / 20.0F);
                    int rowHeight = Math.min(Math.max(10 - (toughnessRows - 2), 3), 10);

                    int top = (height - GuiIngameForge.right_height) - ((toughnessRows * rowHeight) - 10);
                    for (int i = toughnessRows - 1; i >= 0; i--) {
                        int right = width / 2 + 82;
                        for (int j = 1; j < 20; j += 2) {
                            if (j + (i * 20) < armorToughness2) {
                                gui.drawTexturedModalRect(right, top, 34, 0, 9, 9);
                            } else if (j + (i * 20) == armorToughness2) {
                                gui.drawTexturedModalRect(right, top, 25, 0, 9, 9);
                            } else if (j + (i * 20) > armorToughness2) {
                                gui.drawTexturedModalRect(right, top, 16, 0, 9, 9);
                            }
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
        }
    }
}