package com.spicymemes.core.api

import net.minecraft.core.BlockPos
import net.minecraft.world.level.Level
import net.minecraft.world.level.block.Block
import net.minecraft.world.level.block.entity.BlockEntity
import net.minecraft.world.level.block.state.BlockState

/**
 * Represents the state of a block in a level at the time it was grabbed.
 */
data class BlockInWorld(val world: Level, val pos: BlockPos) {

    val state: BlockState by lazy { world.getBlockState(pos) }
    val block: Block = state.block
    val blockEntity: BlockEntity? by lazy { world.getBlockEntity(pos) }

    @Deprecated("Use blockEntity property.")
    val tile: BlockEntity? by this::blockEntity
}

/**
 * Gets the [BlockInWorld] at the specified [pos]
 */
fun Level.getBlock(pos: BlockPos) = BlockInWorld(this, pos)
