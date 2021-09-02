package com.spicymemes.core.api

import net.minecraft.core.Registry
import net.minecraft.resources.ResourceLocation
import net.minecraft.world.item.Item
import net.minecraft.world.item.ItemStack
import net.minecraft.world.level.block.Block

val Block.registryName: ResourceLocation
    get() = Registry.BLOCK.getKey(this)

val Item.registryName: ResourceLocation
    get() = Registry.ITEM.getKey(this)

val ItemStack.registryName: ResourceLocation
    get() = Registry.ITEM.getKey(item)
