package com.spicymemes.core.api.extensions

import net.minecraft.world.level.block.*
import net.minecraft.world.level.block.state.*
import net.minecraft.world.level.block.state.properties.*

val StateDefinition<Block, BlockState>.defaultState: BlockState
    get() = any()

operator fun <T : Comparable<T>> BlockState.get(property: Property<T>): T =
    getValue(property)

operator fun <T : Comparable<T>> BlockState.set(property: Property<T>, value: T): BlockState =
    setValue(property, value)
