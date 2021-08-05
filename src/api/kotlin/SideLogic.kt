package com.spicymemes.api

import net.minecraft.client.multiplayer.*
import net.minecraft.server.level.*
import net.minecraft.world.level.*
import net.minecraftforge.fml.*
import net.minecraftforge.fml.util.thread.*
import kotlin.contracts.*

inline fun serverOnly(action: () -> Unit) {
    contract { callsInPlace(action, InvocationKind.AT_MOST_ONCE) }
    if (EffectiveSide.get() == LogicalSide.SERVER)
        action()
}

inline fun serverOnly(level: LevelReader, action: (level: ServerLevel) -> Unit) {
    contract { callsInPlace(action, InvocationKind.AT_MOST_ONCE) }
    if (!level.isClientSide)
        action(level as ServerLevel)
}

inline fun clientOnly(action: () -> Unit) {
    contract { callsInPlace(action, InvocationKind.AT_MOST_ONCE) }
    if (EffectiveSide.get() == LogicalSide.CLIENT)
        action()
}

inline fun clientOnly(level: LevelReader, action: (level: ClientLevel) -> Unit) {
    contract { callsInPlace(action, InvocationKind.AT_MOST_ONCE) }
    if (level.isClientSide)
        action(level as ClientLevel)
}

inline fun ifModLoaded(modid: String, action: () -> Unit) {
    contract { callsInPlace(action, InvocationKind.AT_MOST_ONCE) }
    if (ModList.get().isLoaded(modid))
        action()
}