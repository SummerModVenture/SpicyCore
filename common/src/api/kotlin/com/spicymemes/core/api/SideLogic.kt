package com.spicymemes.core.api

import net.minecraft.client.multiplayer.ClientLevel
import net.minecraft.server.level.ServerLevel
import net.minecraft.world.level.LevelReader
import kotlin.contracts.InvocationKind
import kotlin.contracts.contract

inline fun serverOnly(level: LevelReader, action: (level: ServerLevel) -> Unit) {
    contract { callsInPlace(action, InvocationKind.AT_MOST_ONCE) }
    if (!level.isClientSide)
        action(level as ServerLevel)
}

inline fun clientOnly(level: LevelReader, action: (level: ClientLevel) -> Unit) {
    contract { callsInPlace(action, InvocationKind.AT_MOST_ONCE) }
    if (level.isClientSide)
        action(level as ClientLevel)
}