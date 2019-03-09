package com.tfar.toughnessbar;

import com.google.common.collect.Lists;
import net.minecraftforge.common.ForgeConfigSpec;
import org.apache.commons.lang3.tuple.Pair;

import java.util.List;

public class ToughnessBarConfig {

  public static String[] colorValues={"#FFFFFF",
          "#FF5500",
          "#FFC747",
          "#27FFE3",
          "#00FF00",
          "#7F00FF"};
  public static boolean showEmptyArmorToughnessIcons;

  public static final ClientConfig CLIENT;
  public static final ForgeConfigSpec CLIENT_SPEC;

  static {final Pair<ClientConfig, ForgeConfigSpec> specPair = new ForgeConfigSpec.Builder().configure(ClientConfig::new);
    CLIENT_SPEC = specPair.getRight();
    CLIENT = specPair.getLeft();
  }
  public static class ClientConfig{
    public ForgeConfigSpec.BooleanValue showEmptyArmorIcons;
    public ForgeConfigSpec.ConfigValue<List<? extends String>> colorValues;

    ClientConfig(ForgeConfigSpec.Builder builder) {
      builder.push("general");
      colorValues = builder
              .comment("Toughness Bar Icon Colors")
              .translation("text.toughnessbar.config.colorvalues")
              .defineList("color values", Lists.newArrayList("#FFFFFF", "#FF5500", "#FFC747", "#27FFE3", "#00FF00", "#7F00FF"), o -> o instanceof String);
      showEmptyArmorIcons = builder
              .comment("Show empty armor icons?")
              .translation("text.toughnessbar.config.showemptyarmortoughnessicons")
              .define("Show empty icons", false);
      builder.pop();
    }
  }
}