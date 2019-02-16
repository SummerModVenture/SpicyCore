package com.spicymemes.core.gui.slots

import net.minecraft.inventory.IInventory

class TileSlotSet(
        name: String,
        inventory: IInventory,
        rows: Int,
        cols: Int,
        x: Int,
        y: Int,
        xSpacing: Int = 18,
        ySpacing: Int = 18,
        onChanged: (Int) -> Unit = {}
) : Slots.SlotSet(name, inventory, rows, cols, x, y, xSpacing, ySpacing, onChanged)