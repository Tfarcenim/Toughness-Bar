package com.tfar.toughnessbar;

import net.minecraftforge.common.config.Config;

@Config(modid = ToughnessBarConstants.MOD_ID)
public class ToughnessBarConfig {
    @Config.Name("Toughness Bar Icon Colors")
    @Config.Comment("Colors must be specified in #RRGGBB format")
    public static String[] colorValues = new String[]{
        "#FFFFFF",
        "#FF5500",
        "#FFC747",
        "#27FFE3",
        "#00FF00",
        "#7F00FF",
    };
    @Config.Name("Show empty armor icons?")
    public static boolean showEmptyArmorToughnessIcons = false;
    public static boolean showBedrock = true;
}