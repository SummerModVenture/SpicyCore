package com.spicymemes.core.util

import net.minecraft.world.level.*
import net.minecraftforge.fml.*
import net.minecraftforge.fml.util.thread.*

inline fun serverOnly(action: () -> Unit) {
    if (EffectiveSide.get() == LogicalSide.SERVER)
        action()
}

inline fun serverOnly(world: LevelReader, action: () -> Unit) {
    if (!world.isClientSide)
        action()
}

inline fun clientOnly(action: () -> Unit) {
    if (EffectiveSide.get() == LogicalSide.CLIENT)
        action()
}

inline fun clientOnly(world: LevelReader, action: () -> Unit) {
    if (world.isClientSide)
        action()
}

inline fun ifModLoaded(modid: String, action: () -> Unit) {
    if (ModList.get().isLoaded(modid))
        action()
}