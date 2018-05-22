package com.spicymemes.common.blocks;

import net.minecraft.block.Block;
import net.minecraft.block.material.MapColor;
import net.minecraft.block.material.Material;
import net.minecraft.creativetab.CreativeTabs;

/**
 * Created by Spencer on 5/22/18.
 */
public class TestBlock extends Block {

    public TestBlock() {
        super(Material.ROCK, MapColor.EMERALD);
        this.setUnlocalizedName("testblock");
        this.setRegistryName("testblock");
        this.setCreativeTab(CreativeTabs.BUILDING_BLOCKS);
    }


}
