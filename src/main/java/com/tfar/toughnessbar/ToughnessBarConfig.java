package com.tfar.toughnessbar;

import net.minecraftforge.common.config.Config;

import java.awt.Color;

@Config(modid = ToughnessBarConstants.MOD_ID)
public class ToughnessBarConfig {
    @Config.Name("Toughness Bar Icon Colors")
    @Config.Comment("Colors must be specified in #RRGGBB format")
    public static String[] colorValues = new String[]{
            getHex(Color.WHITE),
            getHex(Color.ORANGE),
            getHex(Color.YELLOW),
            getHex(Color.CYAN),
            getHex(Color.GREEN),
            getHex(Color.MAGENTA)
    };

    private static String getHex(Color color) {
        return "#" + Integer.toHexString(color.getRGB()).substring(2).toUpperCase();
    }
}