package com.spicymemes.core.gui.slots

import net.minecraft.inventory.IInventory
import net.minecraft.inventory.Slot
import net.minecraft.item.ItemStack

object Slots {

    const val HOTBAR_SLOT_COUNT = 9
    const val PLAYER_INVENTORY_ROW_COUNT = 3
    const val PLAYER_INVENTORY_COLUMN_COUNT = 9
    const val PLAYER_INVENTORY_SLOT_COUNT = PLAYER_INVENTORY_COLUMN_COUNT * PLAYER_INVENTORY_ROW_COUNT
    const val VANILLA_SLOT_COUNT = HOTBAR_SLOT_COUNT + PLAYER_INVENTORY_SLOT_COUNT
    const val VANILLA_FIRST_SLOT_INDEX = 0
    const val PLAYER_INVENTORY_FIRST_SLOT_INDEX = VANILLA_FIRST_SLOT_INDEX + HOTBAR_SLOT_COUNT
    const val MOD_FIRST_SLOT_INDEX = VANILLA_FIRST_SLOT_INDEX + VANILLA_SLOT_COUNT

    fun slotIndex(offset: Int) = MOD_FIRST_SLOT_INDEX + offset

    fun isPlayerInventory(slot: Int) = slot in 0 until MOD_FIRST_SLOT_INDEX
    fun isMod(slot: Int) = slot >= MOD_FIRST_SLOT_INDEX

    abstract class SlotSet(
            val name: String,
            val inventory: IInventory,
            val numRows: Int,
            val numCols: Int,
            val startXPos: Int,
            val startYPos: Int,
            val xSpacing: Int = 18,
            val ySpacing: Int = 18,
            val onChanged: (Int) -> Unit = {}
    ) {

        val numSlots = numRows * numCols

        fun buildSlots(firstSlotIndex: Int): Array<Slot> {
            val slots = mutableListOf<Slot>()
            var slotNumber: Int
            for (y in 0 until numRows) {
                for (x in 0 until numCols) {
                    slotNumber = x + y + firstSlotIndex
                    val xPos = startXPos + x * xSpacing
                    val yPos = startYPos + y * ySpacing
                    slots.add(object : Slot(inventory, slotNumber, xPos, yPos) {
                        override fun isItemValid(stack: ItemStack): Boolean {
                            return inventory.isItemValidForSlot(slotNumber, stack)
                        }

                        override fun onSlotChanged() {
                            super.onSlotChanged()
                            onChanged(slotNumber)
                        }
                    })
                }
            }
            return slots.toTypedArray()
        }
    }
}