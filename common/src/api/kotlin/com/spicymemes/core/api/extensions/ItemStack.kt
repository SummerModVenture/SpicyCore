package com.spicymemes.core.api.extensions

import net.minecraft.world.item.ItemStack

infix fun ItemStack.canMergeWith(other: ItemStack): Boolean =
    ItemStack.isSame(this, other) && ItemStack.isSameItemSameTags(this, other)
