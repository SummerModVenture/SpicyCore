package com.spicymemes.core.api

import net.minecraft.world.item.Item
import net.minecraft.world.item.ItemStack
import net.minecraft.world.level.block.Block

class RecipesBase {

    private val recipes: MutableMap<ItemStack, ItemStack> = mutableMapOf()
    private val expReward: MutableMap<ItemStack, Float> = mutableMapOf()

    /**
     * Adds a recipe to the registry
     * @param input The input item stack
     * @param output The output item stack
     * @param exp The experience reward to give the player
     */
    fun add(input: ItemStack, output: ItemStack, exp: Float): RecipesBase {
        recipes[input] = output
        expReward[input] = exp
        return this
    }

    /**
     * Adds a recipe to the registry, taking one of the given item, ignoring metadata
     * @param input The input item
     * @param output The output item stack
     * @param exp The experience reward to give the player
     */
    fun add(input: Item, output: ItemStack, exp: Float): RecipesBase {
        return add(ItemStack(input, 1), output, exp)
    }

    /**
     * Adds a recipe to the registry, taking one of the given block, ignoring metadata
     * @param input The input block
     * @param output The output item stack
     * @param exp The experience reward to give the player
     */
    fun add(input: Block, output: ItemStack, exp: Float): RecipesBase {
        return add(input.asItem(), output, exp)
    }

    private fun compare(input: ItemStack, recipe: ItemStack): Boolean {
        return input.item === recipe.item
    }

    /**
     * Gets a recipe from the registry
     * @param input The input stack
     * @return The output stack, or ItemStack.EMPTY if the recipe is not found
     */
    fun getRecipe(input: ItemStack): ItemStack {
        for ((recipeInput, recipeOutput) in recipes) {
            if (compare(input, recipeInput)) {
                return recipeOutput
            }
        }
        return ItemStack.EMPTY
    }

    /**
     * Gets the experience reward for a recipe from the registry
     * @param input The input stack
     * @return The experience reward, or 0.0f if the recipe is not found
     */
    fun getRecipeExp(input: ItemStack): Float {
        for ((recipeInput, expReward) in expReward) {
            if (compare(input, recipeInput)) {
                return expReward
            }
        }
        return 0.0f
    }

    /**
     * Returns true if the registry has the given recipe
     * @param input The input stack
     * @return True if the recipe is found, false otherwise
     */
    fun hasRecipe(input: ItemStack): Boolean {
        return !getRecipe(input).isEmpty
    }
}
