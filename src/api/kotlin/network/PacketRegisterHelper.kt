package com.spicymemes.api.network

import net.minecraft.resources.*
import net.minecraftforge.fmllegacy.network.*
import net.minecraftforge.fmllegacy.network.simple.*

fun newSimpleChannel(version: String, modId: String, name: String = "main") = NetworkRegistry.newSimpleChannel(
        ResourceLocation(modId, name),
        { version },
        version::equals,
        version::equals
)

@Suppress("INACCESSIBLE_TYPE")
inline fun <reified M : SpicyPacket> SimpleChannel.registerPacket(
        id: Int,
        handler: SpicyPacketHandler<M>
) {
    registerMessage(
            id,
            M::class.java,
            { packet, buf -> handler.encode(packet, buf) },
            { buf -> handler.decode(buf) },
            { packet, ctx -> handler.handle(packet, ctx) }
    )
}