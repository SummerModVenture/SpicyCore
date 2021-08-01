package com.spicymemes.api

import net.minecraft.core.*
import net.minecraft.world.level.*
import net.minecraft.world.level.block.*
import net.minecraft.world.level.block.entity.*
import net.minecraft.world.level.block.state.*

/**
 * Helper class for packaging world/block/state in one object
 */
data class BlockInWorld(val world: Level, val pos: BlockPos) {

    val state: BlockState by lazy { world.getBlockState(pos) }
    val block: Block = state.block
    val tile: BlockEntity? by lazy { world.getBlockEntity(pos) }
}