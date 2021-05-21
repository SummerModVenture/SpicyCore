package com.spicymemes.core.extensions

import net.minecraft.client.entity.player.*
import net.minecraft.entity.player.*

@Deprecated("", ReplaceWith("asServerPlayerEntity()"))
fun PlayerEntity.toServerPlayerEntity(): ServerPlayerEntity = asServerPlayerEntity()

fun PlayerEntity.asServerPlayerEntity(): ServerPlayerEntity = this as ServerPlayerEntity

@Deprecated("", ReplaceWith("asClientPlayerEntity()"))
fun PlayerEntity.toClientPlayerEntity(): ClientPlayerEntity = asClientPlayerEntity()

fun PlayerEntity.asClientPlayerEntity(): ClientPlayerEntity = this as ClientPlayerEntity