package com.spicymemes.core.worldgen;

import com.spicymemes.core.blocks.ModBlocks;
import net.minecraft.block.state.pattern.BlockMatcher;
import net.minecraft.init.Blocks;
import net.minecraftforge.fml.common.registry.GameRegistry;

/**
 * Created by Spencer on 5/22/18.
 */
public class ModGenerators {
    public static void init(){
        GameRegistry.registerWorldGenerator(new OreGenerator(ModBlocks.testBlock, BlockMatcher.forBlock(Blocks.STONE), 0, 8, 20, 128, 64), 0);
    }
}
