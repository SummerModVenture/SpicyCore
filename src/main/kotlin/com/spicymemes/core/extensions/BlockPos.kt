@file:JvmName("BlockPos")

package com.spicymemes.core.extensions

import net.minecraft.util.math.BlockPos
import java.util.stream.*
import kotlin.math.abs
import kotlin.math.pow
import kotlin.math.sqrt
import kotlin.streams.*

fun BlockPos.distance(other: BlockPos): Double {
    return sqrt((x-other.x.toDouble()).pow(2) + (y-other.y.toDouble()).pow(2) + (z-other.z.toDouble()).pow(2))
}

fun BlockPos.horSquaredDistance(other: BlockPos): Int {
    return abs(x - other.x) + abs(z - other.z)
}

fun BlockPos.getBlocksWithin(range: Int): Sequence<BlockPos> {
    return BlockPos.getAllInBox(x - range, y - range, z - range, x + range, y + range, z + range)
            .map { it.toImmutable() }
            .asSequence()
}

fun BlockPos.getBlocksWithinMutable(range: Int): Sequence<BlockPos> {
    return BlockPos.getAllInBox(x - range, y - range, z - range, x + range, y + range, z + range)
            .asSequence()
}

fun BlockPos.getBlocksWithinHorizontal(range: Int): Sequence<BlockPos> {
    return BlockPos.getAllInBox(x - range, y, z - range, x + range, y, z + range)
            .map { it.toImmutable() }
            .asSequence()
}

fun BlockPos.getBlocksWithinTopCone(range: Int): Sequence<BlockPos> {
    return BlockPos.getAllInBox(x - range, y, z - range, x + range, y + range, z + range)
            .map { it.toImmutable() }
            .asSequence()
}

fun BlockPos.getBlocksWithinTopConeMutable(range: Int): Sequence<BlockPos> {
    return BlockPos.getAllInBox(x - range, y, z - range, x + range, y + range, z + range)
            .asSequence()
}
