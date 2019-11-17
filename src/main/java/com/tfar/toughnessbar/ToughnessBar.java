package com.tfar.toughnessbar;

import com.tfar.toughnessbar.client.EventHandlerClient;

import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.ModLoadingContext;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.config.ModConfig;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import static com.tfar.toughnessbar.ToughnessBar.MOD_ID;

@Mod(value = MOD_ID)
public class ToughnessBar {
    public static final String MOD_ID = "toughnessbar";
    public static Logger logger = LogManager.getLogger(MOD_ID);

    public ToughnessBar(){
        FMLJavaModLoadingContext.get().getModEventBus().addListener(this::setup);
        ModLoadingContext.get().registerConfig(ModConfig.Type.CLIENT, ToughnessBarConfig.CLIENT_SPEC);
    }

    public void setup(FMLClientSetupEvent event) {
        MinecraftForge.EVENT_BUS.register(new EventHandlerClient());
    }

}