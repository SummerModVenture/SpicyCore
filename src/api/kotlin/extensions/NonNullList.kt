package com.spicymemes.api.extensions

import net.minecraft.core.*
import net.minecraft.world.item.*

fun List<ItemStack>.asNonNullList(): NonNullList<ItemStack> = when {
    isEmpty() -> NonNullList.create()
    else -> NonNullList.of(first(), *drop(1).toTypedArray())
}
