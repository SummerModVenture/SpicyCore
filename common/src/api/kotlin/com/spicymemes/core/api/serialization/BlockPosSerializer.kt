package com.spicymemes.core.api.serialization

import kotlinx.serialization.*
import kotlinx.serialization.descriptors.*
import kotlinx.serialization.encoding.*
import net.minecraft.core.*

@OptIn(ExperimentalSerializationApi::class)
@Serializer(forClass = BlockPos::class)
object BlockPosSerializer : KSerializer<BlockPos> {

    override val descriptor = PrimitiveSerialDescriptor("net.minecraft.core.BlockPos", PrimitiveKind.LONG)

    override fun serialize(encoder: Encoder, value: BlockPos) = encoder.encodeLong(value.asLong())
    override fun deserialize(decoder: Decoder): BlockPos = BlockPos.of(decoder.decodeLong())
}
