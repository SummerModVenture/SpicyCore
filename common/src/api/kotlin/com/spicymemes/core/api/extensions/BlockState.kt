package com.spicymemes.core.api.extensions

import net.minecraft.world.level.block.Block
import net.minecraft.world.level.block.state.BlockState
import net.minecraft.world.level.block.state.StateDefinition
import net.minecraft.world.level.block.state.properties.Property

val StateDefinition<Block, BlockState>.defaultState: BlockState
    get() = any()

operator fun <T : Comparable<T>> BlockState.get(property: Property<T>): T =
    getValue(property)

operator fun <T : Comparable<T>> BlockState.set(property: Property<T>, value: T): BlockState =
    setValue(property, value)
