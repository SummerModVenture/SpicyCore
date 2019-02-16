package com.spicymemes.core.gui.slots

import net.minecraft.entity.player.InventoryPlayer

class HotbarSlotSet(inventory: InventoryPlayer, x: Int, y: Int) : Slots.SlotSet("hotbar", inventory, 1, 9, x, y), IVanillaSlotSet

class PlayerInventorySlotSet(inventory: InventoryPlayer, x: Int, y: Int) : Slots.SlotSet("player_inventory", inventory, 3, 9, x, y), IVanillaSlotSet