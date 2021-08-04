package com.spicymemes.api

import net.minecraft.client.multiplayer.*
import net.minecraft.server.level.*
import net.minecraft.world.level.*
import net.minecraftforge.fml.*
import net.minecraftforge.fml.util.thread.*

inline fun serverOnly(action: () -> Unit) {
    if (EffectiveSide.get() == LogicalSide.SERVER)
        action()
}

inline fun serverOnly(level: LevelReader, action: (level: ServerLevel) -> Unit) {
    if (!level.isClientSide)
        action(level as ServerLevel)
}

inline fun clientOnly(action: () -> Unit) {
    if (EffectiveSide.get() == LogicalSide.CLIENT)
        action()
}

inline fun clientOnly(level: LevelReader, action: (level: ClientLevel) -> Unit) {
    if (level.isClientSide)
        action(level as ClientLevel)
}

inline fun ifModLoaded(modid: String, action: () -> Unit) {
    if (ModList.get().isLoaded(modid))
        action()
}