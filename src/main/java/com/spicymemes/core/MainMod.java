package com.spicymemes.core;

import com.spicymemes.core.network.PacketRegisterHelper;
import net.minecraft.block.Block;
import net.minecraft.util.ResourceLocation;
import net.minecraft.world.gen.feature.Feature;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.ExtensionPoint;
import net.minecraftforge.fml.ModLoadingContext;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;
import net.minecraftforge.fml.event.lifecycle.InterModEnqueueEvent;
import net.minecraftforge.fml.event.lifecycle.InterModProcessEvent;
import net.minecraftforge.fml.event.server.FMLServerStartingEvent;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import net.minecraftforge.fml.network.FMLNetworkConstants;
import net.minecraftforge.fml.network.NetworkRegistry;
import net.minecraftforge.fml.network.simple.SimpleChannel;
import org.apache.commons.lang3.tuple.Pair;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.stream.Collectors;

import static net.minecraftforge.fml.ExtensionPoint.DISPLAYTEST;

@Mod(MainMod.MODID)
public class MainMod
{
    public static final String MODID = "spicycore";
    public static final String NAME = "Spicy Core";
    public static final String VERSION = "2.0.0";

    public static Logger logger = LogManager.getLogger("SpicyCore");

    private static String PROTOCOL_VERSION = "1";
    public static SimpleChannel INSTANCE = NetworkRegistry.newSimpleChannel(
            new ResourceLocation(MODID, "main"),
            () -> PROTOCOL_VERSION,
            PROTOCOL_VERSION::equals,
            PROTOCOL_VERSION::equals
    );

    public MainMod() {
        IEventBus modbus = FMLJavaModLoadingContext.get().getModEventBus();
        modbus.addListener(this::setup);
        MinecraftForge.EVENT_BUS.register(EventHandler.INSTANCE);
        ModLoadingContext.get().registerExtensionPoint(
                ExtensionPoint.DISPLAYTEST,
                () -> Pair.of(() -> FMLNetworkConstants.IGNORESERVERONLY, (a, b) -> true)
        );

//        ModLoadingContext.get().getActiveContainer().registerExtensionPoint(DISPLAYTEST, ()-> Pair.of(
//                ()-> FMLNetworkConstants.IGNORESERVERONLY, // ignore me, I'm a server only mod
//                (s,b)->b // i accept anything from the server or the save, if I'm asked
//        ));
    }

    private void setup(FMLCommonSetupEvent event) {
        logger.info("Hello World!");
        PacketRegisterHelper.registerPackets();
    }

    private void doClientStuff(FMLClientSetupEvent event) {
        // do something that can only be done on the client
        //logger.info("Got game settings {}", event.getMinecraftSupplier().get().sett);
    }

    private void enqueueIMC(InterModEnqueueEvent event)
    {
        // some example code to dispatch IMC to another mod
        //InterModComms.sendTo("examplemod", "helloworld", () -> { logger.info("Hello world from the MDK"); return "Hello world";});
    }

    private void processIMC(InterModProcessEvent event) {
        // some example code to receive and process InterModComms from other mods
        logger.info("Got IMC {}", event.getIMCStream().map(m->m.getMessageSupplier().get()).collect(Collectors.toList()));
    }
    // You can use SubscribeEvent and let the Event Bus discover methods to call
    @SubscribeEvent
    public void onServerStarting(FMLServerStartingEvent event) {
        // do something when the server starts
        logger.info("HELLO from server starting");
    }

    // You can use EventBusSubscriber to automatically subscribe events on the contained class (this is subscribing to the MOD
    // Event bus for receiving Registry Events)
    @Mod.EventBusSubscriber(bus = Mod.EventBusSubscriber.Bus.MOD)
    public static class RegistryEvents {
        @SubscribeEvent
        public static void onBlocksRegistry(RegistryEvent.Register<Block> blockRegistryEvent) {
            // register a new block here
            logger.info("HELLO from Register Block");
        }

        @SubscribeEvent
        public static void onFeatureRegistry(RegistryEvent.Register<Feature<?>> event) {

        }
    }
}
