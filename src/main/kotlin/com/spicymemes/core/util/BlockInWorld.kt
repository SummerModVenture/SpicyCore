package com.spicymemes.core.util

import net.minecraft.block.*
import net.minecraft.util.math.*
import net.minecraft.world.*

/**
 * Helper class for packaging world/block/state in one object
 */
data class BlockInWorld(val world: World, val pos: BlockPos) {

    val state: BlockState = world.getBlockState(pos)
    val block: Block = state.block
}