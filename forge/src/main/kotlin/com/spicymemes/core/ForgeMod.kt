package com.spicymemes.core

import com.spicymemes.common.*
import com.spicymemes.core.network.*
import net.minecraftforge.eventbus.api.*
import net.minecraftforge.fml.*
import net.minecraftforge.fml.common.*
import net.minecraftforge.fml.event.lifecycle.*

@Mod(MOD_ID)
@Mod.EventBusSubscriber(modid = MOD_ID, bus = Mod.EventBusSubscriber.Bus.MOD)
class ForgeMod {

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
        fun setupClient(event: FMLClientSetupEvent) {}

        @SubscribeEvent
        @JvmStatic
        fun setupServer(event: FMLDedicatedServerSetupEvent) {}

        @SubscribeEvent
        @JvmStatic
        fun enqueueIMC(event: InterModEnqueueEvent) {}

        @SubscribeEvent
        @JvmStatic
        fun processIMC(event: InterModProcessEvent) {}

        @SubscribeEvent
        @JvmStatic
        fun setupComplete(event: FMLLoadCompleteEvent) {}
    }
}
