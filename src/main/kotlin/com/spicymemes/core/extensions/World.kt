@file:JvmName("World")

package com.spicymemes.core.extensions

import com.spicymemes.core.util.*
import net.minecraft.util.math.*
import net.minecraft.world.*

fun World.getBlock(pos: BlockPos) = BlockInWorld(this, pos)
