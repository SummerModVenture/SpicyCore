@file:JvmName("SideLogic")

package com.spicymemes.core.util

import net.minecraft.world.World
import net.minecraftforge.fml.LogicalSide
import net.minecraftforge.fml.ModList
import net.minecraftforge.fml.common.thread.EffectiveSide

inline fun serverOnly(action: () -> Unit) {
    if (EffectiveSide.get() == LogicalSide.SERVER)
        action()
}

inline fun serverOnly(world: World, action: () -> Unit) {
    if (!world.isRemote)
        action()
}

inline fun clientOnly(action: () -> Unit) {
    if (EffectiveSide.get() == LogicalSide.CLIENT)
        action()
}

inline fun clientOnly(world: World, action: () -> Unit) {
    if (world.isRemote)
        action()
}

inline fun ifModLoaded(modid: String, action: () -> Unit) {
    if (ModList.get().isLoaded(modid))
        action()
}