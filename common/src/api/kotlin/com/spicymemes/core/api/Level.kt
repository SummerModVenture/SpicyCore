package com.spicymemes.core.api

import net.minecraft.server.level.*
import net.minecraft.world.level.*

fun LevelReader.asServerWorld(): ServerLevel = this as ServerLevel
