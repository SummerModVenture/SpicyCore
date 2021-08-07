package com.spicymemes.api

import net.minecraft.core.*
import net.minecraft.nbt.*
import java.util.*
import kotlin.properties.*
import kotlin.reflect.*

fun CompoundTag.putBlockPos(key: String, pos: BlockPos) {
    putLong(key, pos.asLong())
}

fun CompoundTag.getBlockPos(key: String): BlockPos = BlockPos.of(getLong(key))

operator fun CompoundTag.set(key: String, value: Boolean) = putBoolean(key, value)

operator fun CompoundTag.set(key: String, value: Byte) = putByte(key, value)

operator fun CompoundTag.set(key: String, value: Short) = putShort(key, value)

operator fun CompoundTag.set(key: String, value: Int) = putInt(key, value)

operator fun CompoundTag.set(key: String, value: Long) = putLong(key, value)

operator fun CompoundTag.set(key: String, value: Float) = putFloat(key, value)

operator fun CompoundTag.set(key: String, value: Double) = putDouble(key, value)

operator fun CompoundTag.set(key: String, value: String) = putString(key, value)

operator fun CompoundTag.set(key: String, value: UUID) = putUUID(key, value)

operator fun CompoundTag.set(key: String, value: BlockPos) = putBlockPos(key, value)

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

fun CompoundTag.uuid(): ReadOnlyProperty<Any?, UUID> =
    ReadOnlyProperty { _, property -> retrieve(property.name, CompoundTag::getUUID) }

private fun <T> CompoundTag.retrieve(name: String, getter: CompoundTag.(String) -> T): T {
    return if (contains(name))
        getter(name)
    else
        throw NoSuchElementException("No element named \"$name\" found.")
}

