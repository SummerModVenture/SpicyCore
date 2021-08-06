package com.spicymemes.api

import net.minecraftforge.fml.*
import net.minecraftforge.fml.util.thread.*
import kotlin.contracts.*

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
