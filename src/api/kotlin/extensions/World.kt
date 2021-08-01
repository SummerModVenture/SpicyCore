package com.spicymemes.api.extensions

import com.spicymemes.api.*
import net.minecraft.core.*
import net.minecraft.world.level.*

fun Level.getBlock(pos: BlockPos) = BlockInWorld(this, pos)
