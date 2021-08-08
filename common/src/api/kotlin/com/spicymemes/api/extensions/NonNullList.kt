package com.spicymemes.api.extensions

import net.minecraft.core.*
import net.minecraft.world.item.*

fun Collection<ItemStack>.toNonNullList(): NonNullList<ItemStack> = when {
    isEmpty() -> NonNullList.create()
    else -> NonNullList.of(first(), *drop(1).toTypedArray())
}
