package com.spicymemes.core.api.extensions

import net.minecraft.core.*
import kotlin.math.*

fun BlockPos.distance(other: BlockPos): Double {
    return sqrt((x-other.x.toDouble()).pow(2) + (y-other.y.toDouble()).pow(2) + (z-other.z.toDouble()).pow(2))
}

fun BlockPos.horSquaredDistance(other: BlockPos): Int {
    return abs(x - other.x) + abs(z - other.z)
}

fun BlockPos.getBlocksWithin(range: Int): Sequence<BlockPos> {
    return BlockPos.betweenClosed(x - range, y - range, z - range, x + range, y + range, z + range)
            .map { it.immutable() }
            .asSequence()
}

fun BlockPos.getBlocksWithinMutable(range: Int): Sequence<BlockPos> {
    return BlockPos.betweenClosed(x - range, y - range, z - range, x + range, y + range, z + range)
            .asSequence()
}

fun BlockPos.getBlocksWithinHorizontal(range: Int): Sequence<BlockPos> {
    return BlockPos.betweenClosed(x - range, y, z - range, x + range, y, z + range)
            .map { it.immutable() }
            .asSequence()
}

fun BlockPos.getBlocksWithinTopCone(range: Int): Sequence<BlockPos> {
    return BlockPos.betweenClosed(x - range, y, z - range, x + range, y + range, z + range)
            .map { it.immutable() }
            .asSequence()
}

fun BlockPos.getBlocksWithinTopConeMutable(range: Int): Sequence<BlockPos> {
    return BlockPos.betweenClosed(x - range, y, z - range, x + range, y + range, z + range)
            .asSequence()
}
