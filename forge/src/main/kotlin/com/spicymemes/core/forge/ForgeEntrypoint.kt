package com.spicymemes.core.forge

import com.spicymemes.core.common.MOD_ID
import com.spicymemes.core.forge.network.Network
import net.minecraftforge.eventbus.api.SubscribeEvent
import net.minecraftforge.fml.ModContainer
import net.minecraftforge.fml.ModLoadingContext
import net.minecraftforge.fml.common.Mod
import net.minecraftforge.fml.event.lifecycle.*

@Mod(MOD_ID)
@Mod.EventBusSubscriber(modid = MOD_ID, bus = Mod.EventBusSubscriber.Bus.MOD)
class ForgeEntrypoint {

    init {
        ModLoadingContext.get().also { context ->
            container = context.activeContainer
        }
    }

    companion object {

        lateinit var container: ModContainer

        @SubscribeEvent
        @JvmStatic
        fun setupCommon(event: FMLCommonSetupEvent) {
            Network.registerPackets()
        }

        @SubscribeEvent
        @JvmStatic
        fun setupClient(event: FMLClientSetupEvent) {
        }

        @SubscribeEvent
        @JvmStatic
        fun setupServer(event: FMLDedicatedServerSetupEvent) {
        }

        @SubscribeEvent
        @JvmStatic
        fun enqueueIMC(event: InterModEnqueueEvent) {
        }

        @SubscribeEvent
        @JvmStatic
        fun processIMC(event: InterModProcessEvent) {
        }

        @SubscribeEvent
        @JvmStatic
        fun setupComplete(event: FMLLoadCompleteEvent) {
        }
    }
}
