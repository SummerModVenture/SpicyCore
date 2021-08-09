@file:OptIn(ExperimentalSerializationApi::class)

package com.spicymemes.core.api.serialization

import io.netty.buffer.*
import kotlinx.serialization.*
import kotlinx.serialization.descriptors.*
import kotlinx.serialization.encoding.*
import kotlinx.serialization.modules.*
import net.minecraft.network.*
import java.nio.charset.*

fun <T> encodeToByteBuf(serializer: SerializationStrategy<T>, value: T): FriendlyByteBuf {
    val encoder = ByteBufEncoder()
    encoder.encodeSerializableValue(serializer, value)
    return encoder.buf
}

inline fun <reified T> encodeToByteBuf(value: T) = encodeToByteBuf(serializer(), value)

fun <T> encodeToByteBuf(serializer: SerializationStrategy<T>, value: T, buf: FriendlyByteBuf) {
    val encoder = ByteBufEncoder(buf)
    encoder.encodeSerializableValue(serializer, value)
}

inline fun <reified T> encodeToByteBuf(value: T, buf: FriendlyByteBuf) = encodeToByteBuf(serializer(), value, buf)

fun <T> decodeFromByteBuf(buf: FriendlyByteBuf, deserializer: DeserializationStrategy<T>): T {
    val decoder = ByteBufDecoder(buf)
    return decoder.decodeSerializableValue(deserializer)
}

inline fun <reified T> decodeFromByteBuf(buf: FriendlyByteBuf): T = decodeFromByteBuf(buf, serializer())

fun bufferedPacket() = FriendlyByteBuf(Unpooled.buffer())

private class ByteBufEncoder(val buf: FriendlyByteBuf = bufferedPacket()) : AbstractEncoder() {

    override val serializersModule = EmptySerializersModule

    override fun beginCollection(descriptor: SerialDescriptor, collectionSize: Int): CompositeEncoder {
        return super.beginCollection(descriptor, collectionSize).also {
            buf.writeInt(collectionSize)
        }
    }

    override fun encodeNull() {
        buf.writeByte(0)
    }

    override fun encodeNotNullMark() {
        buf.writeByte(1)
    }

    override fun encodeBoolean(value: Boolean) {
        buf.writeBoolean(value)
    }

    override fun encodeByte(value: Byte) {
        buf.writeByte(value.toInt())
    }

    override fun encodeShort(value: Short) {
        buf.writeShort(value.toInt())
    }

    override fun encodeInt(value: Int) {
        buf.writeInt(value)
    }

    override fun encodeLong(value: Long) {
        buf.writeLong(value)
    }

    override fun encodeFloat(value: Float) {
        buf.writeFloat(value)
    }

    override fun encodeDouble(value: Double) {
        buf.writeDouble(value)
    }

    override fun encodeChar(value: Char) {
        buf.writeChar(value.code)
    }

    override fun encodeString(value: String) {
        buf.writeInt(value.length)
        buf.writeCharSequence(value, Charset.defaultCharset())
    }

    override fun encodeEnum(enumDescriptor: SerialDescriptor, index: Int) {
        buf.writeInt(index)
    }
}

private class ByteBufDecoder(val buf: FriendlyByteBuf) : AbstractDecoder() {

    private var elementIndex = 0

    override val serializersModule = EmptySerializersModule

    override fun decodeElementIndex(descriptor: SerialDescriptor): Int {
        if (elementIndex == descriptor.elementsCount) return CompositeDecoder.DECODE_DONE
        return elementIndex++
    }

    override fun decodeCollectionSize(descriptor: SerialDescriptor): Int = buf.readInt()
    override fun decodeNotNullMark(): Boolean = buf.readByte() != 0.toByte()
    override fun decodeBoolean(): Boolean = buf.readBoolean()
    override fun decodeByte(): Byte = buf.readByte()
    override fun decodeShort(): Short = buf.readShort()
    override fun decodeInt(): Int = buf.readInt()
    override fun decodeLong(): Long = buf.readLong()
    override fun decodeFloat(): Float = buf.readFloat()
    override fun decodeDouble(): Double = buf.readDouble()
    override fun decodeChar(): Char = buf.readChar()
    override fun decodeString(): String = buf.readCharSequence(buf.readInt(), Charset.defaultCharset()).toString()
    override fun decodeEnum(enumDescriptor: SerialDescriptor): Int = buf.readInt()
    override fun decodeNull(): Nothing? = null
}
