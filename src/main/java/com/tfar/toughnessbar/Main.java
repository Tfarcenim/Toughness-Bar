package com.tfar.toughnessbar;

import com.tfar.toughnessbar.client.EventHandlerClient;
import com.tfar.toughnessbar.util.Reference;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.Mod.EventHandler;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
@Mod(clientSideOnly = true, modid = "toughnessbar", name = Reference.NAME, version = Reference.VERSION,
        acceptedMinecraftVersions = Reference.MC_VERSION)
public class Main {
    @EventHandler
    public void PreInit(FMLPreInitializationEvent preEvent) {
        MinecraftForge.EVENT_BUS.register(new EventHandlerClient());
    }
}