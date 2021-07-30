package com.spicymemes.core.util

import net.minecraft.core.*
import net.minecraft.world.level.*
import net.minecraft.world.level.block.*
import net.minecraft.world.level.block.state.*

/**
 * Helper class for packaging world/block/state in one object
 */
data class BlockInWorld(val world: Level, val pos: BlockPos) {

    val state: BlockState = world.getBlockState(pos)
    val block: Block = state.block
}