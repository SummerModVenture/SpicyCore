package com.spicymemes.api

import net.minecraft.core.*
import net.minecraft.nbt.*
import kotlin.properties.*
import kotlin.reflect.*

operator fun <V : Any> CompoundTag.getValue(thisRef: Any?, property: KProperty<*>): V =
    delegate<V>().getValue(thisRef, property)

@Suppress("unchecked_cast")
fun <V : Any> CompoundTag.delegate(): ReadOnlyProperty<Any?, V> =
    ReadOnlyProperty { _, property ->
        when (val tag = get(property.name)) {
            is NumericTag -> when (tag) {
                is ByteTag -> tag.asByte as V
                is ShortTag -> tag.asShort as V
                is IntTag -> tag.asInt as V
                is LongTag -> tag.asLong as V
                is FloatTag -> tag.asFloat as V
                is DoubleTag -> tag.asDouble as V
                else -> error("Unknown NumericTag: $tag")
            }
            is StringTag -> tag.asString as V
            is CollectionTag<*> -> when (tag) {
                is ListTag -> tag as V
                is ByteArrayTag -> tag.asByteArray as V
                is IntArrayTag -> tag.asIntArray as V
                is LongArrayTag -> tag.asLongArray as V
                else -> error("Unknown CollectionTag: $tag")
            }
            is CompoundTag -> tag as V
            else -> TODO("Not all types are implemented in the CompoundType delegate.")
        }
    }

fun CompoundTag.ints(): ReadOnlyProperty<Any?, Int> =
    ReadOnlyProperty { _, property -> retrieve(property.name, CompoundTag::getInt) }

fun CompoundTag.blockPos(): ReadOnlyProperty<Any?, BlockPos> =
    ReadOnlyProperty { _, property -> retrieve(property.name, CompoundTag::getBlockPos) }

private fun <T> CompoundTag.retrieve(name: String, getter: CompoundTag.(String) -> T): T {
    return if (contains(name))
        getter(name)
    else
        throw NoSuchElementException("No element named \"$name\" found.")
}

fun CompoundTag.putBlockPos(key: String, pos: BlockPos) {
    putLong(key, pos.asLong())
}

fun CompoundTag.getBlockPos(key: String): BlockPos = BlockPos.of(getLong(key))

