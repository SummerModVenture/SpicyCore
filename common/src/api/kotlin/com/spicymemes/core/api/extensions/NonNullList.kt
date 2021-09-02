package com.spicymemes.core.api.extensions

import net.minecraft.core.NonNullList
import net.minecraft.world.item.ItemStack

fun Collection<ItemStack>.toNonNullList(): NonNullList<ItemStack> = when {
    isEmpty() -> NonNullList.create()
    else -> NonNullList.of(first(), *drop(1).toTypedArray())
}
