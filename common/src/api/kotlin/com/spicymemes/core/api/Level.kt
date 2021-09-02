package com.spicymemes.core.api

import net.minecraft.server.level.ServerLevel
import net.minecraft.world.level.LevelReader

fun LevelReader.asServerWorld(): ServerLevel = this as ServerLevel
