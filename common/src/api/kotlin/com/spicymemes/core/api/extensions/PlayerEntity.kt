package com.spicymemes.core.api.extensions

import net.minecraft.client.player.RemotePlayer
import net.minecraft.server.level.ServerPlayer
import net.minecraft.world.entity.player.Player

fun Player.asServerPlayer(): ServerPlayer = this as ServerPlayer

fun Player.asRemotePlayer(): RemotePlayer = this as RemotePlayer
