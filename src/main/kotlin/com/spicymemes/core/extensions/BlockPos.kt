@file:JvmName("BlockPos")

package com.spicymemes.core.extensions

import net.minecraft.util.math.BlockPos
import kotlin.math.abs
import kotlin.math.pow
import kotlin.math.sqrt
import kotlin.streams.toList

fun BlockPos.distance(other: BlockPos): Double {
    return sqrt((x-other.x.toDouble()).pow(2) + (y-other.y.toDouble()).pow(2) + (z-other.z.toDouble()).pow(2))
}

fun BlockPos.horSquaredDistance(other: BlockPos): Int {
    return abs(x - other.x) + abs(z - other.z)
}

fun BlockPos.getBlocksWithin(range: Int): Set<BlockPos> {
    return BlockPos.getAllInBox(x - range, y - range, z - range, x + range, y + range, z + range).toList().toSet()
}

fun BlockPos.getBlocksWithinMutable(range: Int): MutableSet<BlockPos> {
    return BlockPos.getAllInBoxMutable(x - range, y - range, z - range, x + range, y + range, z + range).toList().toMutableSet()
}

fun BlockPos.getBlocksWithinHorizontal(range: Int): Set<BlockPos> {
    return BlockPos.getAllInBox(x - range, y, z - range, x + range, y, z + range).toList().toSet()
}

fun BlockPos.getBlocksWithinTopCone(range: Int): Set<BlockPos> {
    return BlockPos.getAllInBox(x - range, y, z - range, x + range, y + range, z + range).toList().toSet()
}

fun BlockPos.getBlocksWithinTopConeMutable(range: Int): MutableSet<BlockPos> {
    return BlockPos.getAllInBoxMutable(x - range, y, z - range, x + range, y + range, z + range).toList().toMutableSet()
}
