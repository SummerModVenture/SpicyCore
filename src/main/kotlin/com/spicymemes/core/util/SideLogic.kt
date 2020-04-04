@file:JvmName("SideLogic")

package com.spicymemes.core.util

import net.minecraft.world.*
import net.minecraftforge.fml.*
import net.minecraftforge.fml.common.thread.*

inline fun serverOnly(action: () -> Unit) {
    if (EffectiveSide.get() == LogicalSide.SERVER)
        action()
}

inline fun serverOnly(world: IWorld, action: () -> Unit) {
    if (!world.isRemote)
        action()
}

inline fun clientOnly(action: () -> Unit) {
    if (EffectiveSide.get() == LogicalSide.CLIENT)
        action()
}

inline fun clientOnly(world: IWorld, action: () -> Unit) {
    if (world.isRemote)
        action()
}

inline fun ifModLoaded(modid: String, action: () -> Unit) {
    if (ModList.get().isLoaded(modid))
        action()
}