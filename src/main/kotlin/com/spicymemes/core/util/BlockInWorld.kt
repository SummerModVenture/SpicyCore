package com.spicymemes.core.util

import net.minecraft.block.Block
import net.minecraft.block.BlockState
import net.minecraft.util.math.BlockPos
import net.minecraft.world.World

/**
 * Helper class for packaging world/block/state in one object
 */
data class BlockInWorld(val world: World, val pos: BlockPos) {

    val state: BlockState = world.getBlockState(pos)
    val block: Block = state.block
}