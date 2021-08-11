package com.spicymemes.core.api

import net.minecraft.core.*
import net.minecraft.resources.*
import net.minecraft.world.item.*
import net.minecraft.world.level.block.*

val Block.registryName: ResourceLocation
    get() = Registry.BLOCK.getKey(this)

val Item.registryName: ResourceLocation
    get() = Registry.ITEM.getKey(this)

val ItemStack.registryName: ResourceLocation
    get() = Registry.ITEM.getKey(item)
