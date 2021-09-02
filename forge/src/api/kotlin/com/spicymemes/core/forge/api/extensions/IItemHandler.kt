package com.spicymemes.core.forge.api.extensions

import net.minecraft.world.item.ItemStack
import net.minecraftforge.items.IItemHandler

fun IItemHandler.insert(stack: ItemStack, simulate: Boolean = false): ItemStack {
    var remainder = stack
    for (slot in 0 until slots) {
        remainder = insertItem(slot, remainder, simulate)
        if (remainder.isEmpty)
            break
    }

    return remainder
}

val IItemHandler.allItems: List<ItemStack>
    get() = (0 until slots)
        .map { getStackInSlot(it) }
        .filter { !it.isEmpty }
