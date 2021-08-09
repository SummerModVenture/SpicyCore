package com.spicymemes.core.api.extensions

import net.minecraft.client.player.*
import net.minecraft.server.level.*
import net.minecraft.world.entity.player.*

fun Player.asServerPlayer(): ServerPlayer = this as ServerPlayer

fun Player.asRemotePlayer(): RemotePlayer = this as RemotePlayer
