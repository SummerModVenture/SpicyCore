@file:JvmName("PacketRegisterHelper")

package com.spicymemes.core.network

import com.spicymemes.core.MainMod
import com.spicymemes.core.network.packets.*
import net.minecraftforge.fml.network.simple.*

@Suppress("INACCESSIBLE_TYPE")
inline fun <reified MSG : SpicyPacket> SimpleChannel.registerPacket(
        id: Int,
        handler: SpicyPacketHandler<MSG>
) {
    registerMessage(
            id,
            MSG::class.java,
            { packet, buf -> handler.encode(packet, buf) },
            { buf -> handler.decode(buf) },
            { packet, ctx -> handler.handle(packet, ctx) }
    )
}

fun registerPackets() {
    var id = 0
    MainMod.INSTANCE.registerPacket(id++, PingClientPacket.Handler)
    MainMod.INSTANCE.registerPacket(id++, ClientPresentPacket.Handler)
}