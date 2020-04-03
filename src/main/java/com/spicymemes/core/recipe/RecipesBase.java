package com.spicymemes.core.recipe;

import net.minecraft.block.Block;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;

import java.util.HashMap;
import java.util.Map;
import java.util.function.BiConsumer;

/**
 * Represents a registry for single input, single output recipes.
 */
public class RecipesBase {

    private HashMap<ItemStack, ItemStack> recipes = new HashMap<>();
    private HashMap<ItemStack, Float> expReward = new HashMap<>();

    /**
     * Adds a recipe to the registry
     * @param input The input item stack
     * @param output The output item stack
     * @param exp The experience reward to give the player
     */
    public RecipesBase add(ItemStack input, ItemStack output, float exp) {
        recipes.put(input, output);
        expReward.put(input, exp);
        return this;
    }

    /**
     * Adds a recipe to the registry, taking one of the given item, ignoring metadata
     * @param input The input item
     * @param output The output item stack
     * @param exp The experience reward to give the player
     */
    public RecipesBase add(Item input, ItemStack output, float exp) {
        return add(new ItemStack(input, 1), output, exp);
    }

    /**
     * Adds a recipe to the registry, taking one of the given block, ignoring metadata
     * @param input The input block
     * @param output The output item stack
     * @param exp The experience reward to give the player
     */
    public RecipesBase add(Block input, ItemStack output, float exp) {
        return add(input.asItem(), output, exp);
    }

    private static boolean compare(ItemStack input, ItemStack recipe) {
        return input.getItem() == recipe.getItem();
    }

    /**
     * Gets a recipe from the registry
     * @param input The input stack
     * @return The output stack, or ItemStack.EMPTY if the recipe is not found
     */
    public ItemStack getRecipe(ItemStack input) {
        for (Map.Entry<ItemStack, ItemStack> entry : recipes.entrySet()) {
            ItemStack recipeInput = entry.getKey();
            ItemStack recipeOutput = entry.getValue();
            if (compare(input, recipeInput)) {
                return recipeOutput;
            }
        }
        return ItemStack.EMPTY;
    }

    /**
     * Gets the experience reward for a recipe from the registry
     * @param input The input stack
     * @return The experience reward, or 0.0f if the recipe is not found
     */
    public float getRecipeExp(ItemStack input) {
        for (Map.Entry<ItemStack, Float> entry : expReward.entrySet()) {
            ItemStack recipeInput = entry.getKey();
            float expReward = entry.getValue();
            if (compare(input, recipeInput)) {
                return expReward;
            }
        }
        return 0.0f;
    }

    /**
     * Returns true if the registry has the given recipe
     * @param input The input stack
     * @return True if the recipe is found, false otherwise
     */
    public boolean hasRecipe(ItemStack input) {
        return !getRecipe(input).isEmpty();
    }
}
