package com.spicymemes.core.language

import net.minecraftforge.fml.Logging.SCAN
import net.minecraftforge.forgespi.language.ILifecycleEvent
import net.minecraftforge.forgespi.language.IModInfo
import net.minecraftforge.forgespi.language.IModLanguageProvider
import net.minecraftforge.forgespi.language.ModFileScanData
import org.apache.logging.log4j.LogManager
import org.objectweb.asm.Type
import java.util.function.Consumer
import java.util.function.Supplier

class FMLKotlinModLanguageProvider : IModLanguageProvider {

    private class FMLModTarget(val className: String, val modId: String) : IModLanguageProvider.IModLanguageLoader {

        override fun <T : Any> loadMod(
                info: IModInfo,
                modClassLoader: ClassLoader,
                modFileScanResults: ModFileScanData
        ): T {
            TODO()
        }
    }

    override fun getFileVisitor(): Consumer<ModFileScanData> = Consumer { scanResult ->
        val modTargetMap = scanResult.annotations
                .filter { it.annotationType == MODANNOTATION }
                .map {
                    it.also {
                        LOGGER.debug(
                                SCAN,
                                "Found @Mod class ${it.classType.className} with id ${it.annotationData["value"]}"
                        )
                    }
                }
                .map { FMLModTarget(it.classType.className, it.annotationData["value"].toString()) }
                .map { it.modId to it }
                .toMap()

        scanResult.addLanguageLoader(modTargetMap)
    }

    override fun <R : ILifecycleEvent<R>> consumeLifecycleEvent(consumeEvent: Supplier<R>?) {

    }

    override fun name() = "kotlinfml"

    companion object {
        val MODANNOTATION = Type.getType("Lnet/minecraftforge/fml/common/Mod;")

        private val LOGGER = LogManager.getLogger()
    }
}
