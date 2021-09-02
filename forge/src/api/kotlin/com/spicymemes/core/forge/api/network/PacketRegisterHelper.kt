package com.spicymemes.core.forge.api.network

import com.spicymemes.core.api.serialization.decodeFromByteBuf
import com.spicymemes.core.api.serialization.encodeToByteBuf
import net.minecraft.resources.ResourceLocation
import net.minecraftforge.fmllegacy.network.NetworkRegistry
import net.minecraftforge.fmllegacy.network.simple.SimpleChannel

fun newSimpleChannel(version: String, modId: String, name: String = "main") = NetworkRegistry.newSimpleChannel(
    ResourceLocation(modId, name),
    { version },
    version::equals,
    version::equals
)

@Suppress("INACCESSIBLE_TYPE")
inline fun <reified M> SimpleChannel.registerPacket(id: Int, handler: ForgePacketHandler<M>) {
    registerMessage(
        id,
        M::class.java,
        { packet, buf -> encodeToByteBuf(packet, buf) },
        { buf -> decodeFromByteBuf(buf) },
        { packet, ctx -> handler.handle(packet, ctx) }
    )
}