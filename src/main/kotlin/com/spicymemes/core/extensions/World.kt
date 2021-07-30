package com.spicymemes.core.extensions

import com.spicymemes.core.util.*
import net.minecraft.core.*
import net.minecraft.world.level.*

fun Level.getBlock(pos: BlockPos) = BlockInWorld(this, pos)
