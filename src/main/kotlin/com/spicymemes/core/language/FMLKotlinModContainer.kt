package com.spicymemes.core.language

import net.minecraftforge.eventbus.EventBusErrorMessage
import net.minecraftforge.eventbus.api.BusBuilder
import net.minecraftforge.eventbus.api.Event
import net.minecraftforge.eventbus.api.IEventBus
import net.minecraftforge.eventbus.api.IEventListener
import net.minecraftforge.fml.*
import net.minecraftforge.fml.LifecycleEventProvider.LifecycleEvent
import net.minecraftforge.forgespi.language.IModInfo
import net.minecraftforge.forgespi.language.ModFileScanData
import org.apache.logging.log4j.LogManager
import java.util.*
import java.util.function.Consumer

class FMLKotlinModContainer(
        info: IModInfo,
        val className: String,
        val modClassLoader: ClassLoader,
        val modFileScanResults: ModFileScanData
) : ModContainer(info) {

    private val LOGGER = LogManager.getLogger()
    private val modClass: Class<*>
    private lateinit var modInstance: Any
    val eventBus = BusBuilder.builder().setExceptionHandler(this::onEventFailed).setTrackPhases(false).build()

    init {
        LOGGER.debug(
                Logging.LOADING,
                "Creating FMLModContainer instance for $className with classLoader $modClassLoader & ${javaClass.classLoader}"
        )
        triggerMap[ModLoadingStage.CONSTRUCT] = dummy()
                .andThen(this::beforeEvent)
                .andThen(this::constructMod)
                .andThen(this::afterEvent)
        triggerMap[ModLoadingStage.CREATE_REGISTRIES] = dummy()
                .andThen(this::beforeEvent)
                .andThen(this::fireEvent)
                .andThen(this::afterEvent)
        triggerMap[ModLoadingStage.LOAD_REGISTRIES] = dummy()
                .andThen(this::beforeEvent)
                .andThen(this::fireEvent)
                .andThen(this::afterEvent)
        triggerMap[ModLoadingStage.COMMON_SETUP] = dummy()
                .andThen(this::beforeEvent)
                .andThen(this::preinitMod)
                .andThen(this::fireEvent)
                .andThen(this::afterEvent)
        triggerMap[ModLoadingStage.SIDED_SETUP] = dummy()
                .andThen(this::beforeEvent)
                .andThen(this::fireEvent)
                .andThen(this::afterEvent)
        triggerMap[ModLoadingStage.ENQUEUE_IMC] = dummy()
                .andThen(this::beforeEvent)
                .andThen(this::initMod)
                .andThen(this::fireEvent)
                .andThen(this::afterEvent)
        triggerMap[ModLoadingStage.PROCESS_IMC] = dummy()
                .andThen(this::beforeEvent)
                .andThen(this::fireEvent)
                .andThen(this::afterEvent)
        triggerMap[ModLoadingStage.COMPLETE] = dummy()
                .andThen(this::beforeEvent)
                .andThen(this::completeLoading)
                .andThen(this::fireEvent)
                .andThen(this::afterEvent)
        triggerMap[ModLoadingStage.GATHERDATA] = dummy()
                .andThen(this::beforeEvent)
                .andThen(this::fireEvent)
                .andThen(this::afterEvent)

        configHandler = Optional.of(Consumer { e -> eventBus.post(e) })
        contextExtension
        try {
            modClass = Class.forName(className, true, modClassLoader)
        } catch (e: Throwable) {
            LOGGER.error(Logging.LOADING, "Failed to load class $className", e)
            throw ModLoadingException(info, ModLoadingStage.CONSTRUCT, "fml.modloading.failedtoloadmodclass", e)
        }
    }

    private fun completeLoading(lifecycleEvent: LifecycleEvent) {}

    private fun initMod(lifecycleEvent: LifecycleEvent) {}

    private fun dummy(): Consumer<LifecycleEvent> = Consumer { _ -> }

    private fun beforeEvent(lifecycleEvent: LifecycleEvent) {}

    private fun fireEvent(lifecycleEvent: LifecycleEvent) {
        val event = lifecycleEvent.getOrBuildEvent(this)
        LOGGER.debug(Logging.LOADING, "Firing event for modid $modId : $event")
        try {
            eventBus.post(event)
            LOGGER.debug(Logging.LOADING, "Fired event for modid $modId : $event")
        } catch (e: Throwable) {
            LOGGER.error(Logging.LOADING, "Caught exception during event $event dispatch for modid $modId", e)
            throw ModLoadingException(modInfo, lifecycleEvent.fromStage(), "fml.modloading.errorduringevent", e)
        }
    }

    private fun afterEvent(lifecycleEvent: LifecycleEvent) {
        if (currentState == ModLoadingStage.ERROR) {
            LOGGER.error(Logging.LOADING, "An error occurred while dispatching event {} to {}", lifecycleEvent.fromStage(), getModId())
        }
    }

    private fun preinitMod(lifecycleEvent: LifecycleEvent) {}

    private fun onEventFailed(
            eventBus: IEventBus,
            event: Event,
            eventListeners: Array<IEventListener>,
            i: Int,
            t: Throwable
    ) {
        LOGGER.error(EventBusErrorMessage(event, i, eventListeners, t))
    }

    private fun constructMod(event: LifecycleEventProvider.LifecycleEvent) {
        try {
            LOGGER.debug(Logging.LOADING, "Loading kotlin object mod instance $modId of type ${modClass.name}")
            modInstance = modClass.getField("INSTANCE").get(null)
            LOGGER.debug(Logging.LOADING, "Loaded kotlin object mod instance $modId of type ${modClass.name}")
        } catch (e: Throwable) {
            LOGGER.error(
                    Logging.LOADING,
                    "Failed to find kotlin object instance for mod. ModID: ${modId}, class ${modClass.name}",
                    e
            )
            throw ModLoadingException(modInfo, event.fromStage(), "fml.modloading.failedtoloadmod", e, modClass)
        }
        try {
            LOGGER.debug(Logging.LOADING, "Injecting Automatic event subscribers for $modId")
            AutomaticEventSubscriber.inject(this, modFileScanResults, modClass.classLoader)
            LOGGER.debug(Logging.LOADING, "Completed Automatic event subscribers for $modId")
        } catch (e: Throwable) {
            LOGGER.error(
                    Logging.LOADING,
                    "Failed to register automatic subscribers. ModID: $modId, class ${modClass.name}",
                    e
            )
            throw ModLoadingException(modInfo, event.fromStage(), "fml.modloading.failedtoloadmod", e, modClass)
        }
    }

    override fun getMod() = modInstance

    override fun matches(mod: Any?) = mod == modInstance

    override fun acceptEvent(e: Event) {
        eventBus.post(e)
    }

}
