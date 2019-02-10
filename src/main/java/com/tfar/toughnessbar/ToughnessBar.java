package com.tfar.toughnessbar;

import com.tfar.toughnessbar.client.EventHandlerClient;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.Mod.EventHandler;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;

@Mod(clientSideOnly = true, modid = ToughnessBarConstants.MOD_ID, name = ToughnessBarConstants.NAME, version = ToughnessBarConstants.VERSION,
        acceptedMinecraftVersions = ToughnessBarConstants.MC_VERSION)
public class ToughnessBar {
    @EventHandler
    public void preInit(FMLPreInitializationEvent event) {
        MinecraftForge.EVENT_BUS.register(new EventHandlerClient());
    }
}