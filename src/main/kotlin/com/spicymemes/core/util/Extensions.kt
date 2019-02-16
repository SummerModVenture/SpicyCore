package com.spicymemes.core.util

import net.minecraft.block.Block
import net.minecraft.item.Item
import net.minecraft.util.math.BlockPos
import net.minecraft.world.World
import net.minecraftforge.fml.common.FMLCommonHandler
import net.minecraftforge.fml.common.Loader
import net.minecraftforge.fml.relauncher.Side
import kotlin.math.abs
import kotlin.math.pow
import kotlin.math.sqrt

var Item.codename: String
    get() = unlocalizedName
    set(value) = Unit.apply { setUnlocalizedName(value).setRegistryName(value) }

var Block.codename: String
    get() = unlocalizedName
    set(value) = Unit.apply { setUnlocalizedName(value).setRegistryName(value) }

@Deprecated("Use var property", ReplaceWith("codename"))
fun Item.setCodename(name: String): Item {
    return setUnlocalizedName(name).setRegistryName(name)
}

@Deprecated("Use var property", ReplaceWith("codename"))
fun Block.setCodename(name: String): Block {
    return setUnlocalizedName(name).setRegistryName(name)
}

fun BlockPos.distance(other: BlockPos): Double {
    return sqrt((x-other.x.toDouble()).pow(2) + (y-other.y.toDouble()).pow(2) + (z-other.z.toDouble()).pow(2))
}

fun BlockPos.horSquaredDistance(other: BlockPos): Int {
    return abs(x - other.x) + abs(z - other.z)
}

fun BlockPos.getBlocksWithin(range: Int): Iterable<BlockPos> {
    return BlockPos.getAllInBox(x - range, y - range, z - range, x + range, y + range, z + range)
}

fun BlockPos.getBlocksWithinMutable(range: Int): Iterable<BlockPos.MutableBlockPos> {
    return BlockPos.getAllInBoxMutable(x - range, y - range, z - range, x + range, y + range, z + range)
}

fun BlockPos.getBlocksWithinHorizontal(range: Int): Iterable<BlockPos> {
    return BlockPos.getAllInBox(x - range, y, z - range, x + range, y, z + range)
}

fun BlockPos.getBlocksWithinTopCone(range: Int): Iterable<BlockPos> {
    return BlockPos.getAllInBox(x - range, y, z - range, x + range, y + range, z + range)
}

fun BlockPos.getBlocksWithinTopConeMutable(range: Int): Iterable<BlockPos.MutableBlockPos> {
    return BlockPos.getAllInBoxMutable(x - range, y, z - range, x + range, y + range, z + range)
}

fun World.getBlock(pos: BlockPos) = BlockInWorld(this, pos)

inline fun serverOnly(action: () -> Unit) {
    if (FMLCommonHandler.instance().effectiveSide == Side.SERVER)
        action()
}
inline fun serverOnly(world: World, action: () -> Unit) {
    if (!world.isRemote)
        action()
}

inline fun clientOnly(action: () -> Unit) {
    if (FMLCommonHandler.instance().effectiveSide == Side.CLIENT)
        action()
}

inline fun clientOnly(world: World, action: () -> Unit) {
    if (world.isRemote)
        action()
}

inline fun ifModLoaded(modid: String, action: () -> Unit) {
    if (Loader.isModLoaded(modid))
        action()
}