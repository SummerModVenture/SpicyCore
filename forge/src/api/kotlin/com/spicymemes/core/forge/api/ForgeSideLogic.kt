package com.spicymemes.core.forge.api

import net.minecraftforge.fml.LogicalSide
import net.minecraftforge.fml.ModList
import net.minecraftforge.fml.util.thread.EffectiveSide
import kotlin.contracts.InvocationKind
import kotlin.contracts.contract

inline fun serverOnly(action: () -> Unit) {
    contract { callsInPlace(action, InvocationKind.AT_MOST_ONCE) }
    if (EffectiveSide.get() == LogicalSide.SERVER)
        action()
}

inline fun clientOnly(action: () -> Unit) {
    contract { callsInPlace(action, InvocationKind.AT_MOST_ONCE) }
    if (EffectiveSide.get() == LogicalSide.CLIENT)
        action()
}

inline fun ifModLoaded(modid: String, action: () -> Unit) {
    contract { callsInPlace(action, InvocationKind.AT_MOST_ONCE) }
    if (ModList.get().isLoaded(modid))
        action()
}
