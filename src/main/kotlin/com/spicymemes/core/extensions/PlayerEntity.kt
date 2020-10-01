package com.spicymemes.core.extensions

import net.minecraft.client.entity.player.*
import net.minecraft.entity.player.*

fun PlayerEntity.toServerPlayerEntity(): ServerPlayerEntity = this as ServerPlayerEntity

fun PlayerEntity.toClientPlayerEntity(): ClientPlayerEntity = this as ClientPlayerEntity
