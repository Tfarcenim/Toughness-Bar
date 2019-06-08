package com.tfar.toughnessbar;

import com.google.common.collect.Lists;
import net.minecraftforge.common.ForgeConfigSpec;
import org.apache.commons.lang3.tuple.Pair;

import java.util.ArrayList;
import java.util.List;

public class ToughnessBarConfig {

  public static final ClientConfig CLIENT;
  public static final ForgeConfigSpec CLIENT_SPEC;

  static {final Pair<ClientConfig, ForgeConfigSpec> specPair = new ForgeConfigSpec.Builder().configure(ClientConfig::new);
    CLIENT_SPEC = specPair.getRight();
    CLIENT = specPair.getLeft();
  }
  public static class ClientConfig {
    public static ForgeConfigSpec.BooleanValue empty;
    public static ForgeConfigSpec.BooleanValue showBedrock;
    public static ForgeConfigSpec.ConfigValue<List<? extends String>> colorValues;

    ClientConfig(ForgeConfigSpec.Builder builder) {
      builder.push("general");
      colorValues = builder
              .comment("Toughness Bar Icon Colors")
              .translation("text.toughnessbar.config.colorvalues")
              .defineList("color values", Lists.newArrayList("#FFFFFF", "#FF5500", "#FFC747", "#27FFE3", "#00FF00", "#7F00FF"), o -> o instanceof String);
      empty = builder
              .comment("Show empty armor toughness icons?")
              .translation("text.toughnessbar.config.showemptyarmortoughnessicons")
              .define("Show empty icons", false);
      showBedrock = builder
              .comment("Show bedrock overlay?")
              .translation("text.toughnessbar.config.showbedrockoverlay")
              .define("Show bedrock", true);
      builder.pop();
    }
  }
    public static List<? extends String> colorValues;
    public static boolean bedrock;
    public static boolean empty;
    public static void bake(){
      colorValues = ClientConfig.colorValues.get();
      bedrock = ClientConfig.showBedrock.get();
      empty = ClientConfig.empty.get();
  }
}