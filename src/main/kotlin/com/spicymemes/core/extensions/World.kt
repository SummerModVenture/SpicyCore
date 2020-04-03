@file:JvmName("World")

package com.spicymemes.core.extensions

import com.spicymemes.core.util.BlockInWorld
import net.minecraft.util.math.BlockPos
import net.minecraft.world.World

fun World.getBlock(pos: BlockPos) = BlockInWorld(this, pos)
