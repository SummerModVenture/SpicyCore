package com.spicymemes.core.api.serialization

import kotlinx.serialization.ExperimentalSerializationApi
import kotlinx.serialization.KSerializer
import kotlinx.serialization.Serializer
import kotlinx.serialization.descriptors.PrimitiveKind
import kotlinx.serialization.descriptors.PrimitiveSerialDescriptor
import kotlinx.serialization.encoding.Decoder
import kotlinx.serialization.encoding.Encoder
import net.minecraft.core.BlockPos

@OptIn(ExperimentalSerializationApi::class)
@Serializer(forClass = BlockPos::class)
object BlockPosSerializer : KSerializer<BlockPos> {

    override val descriptor = PrimitiveSerialDescriptor("net.minecraft.core.BlockPos", PrimitiveKind.LONG)

    override fun serialize(encoder: Encoder, value: BlockPos) = encoder.encodeLong(value.asLong())
    override fun deserialize(decoder: Decoder): BlockPos = BlockPos.of(decoder.decodeLong())
}
