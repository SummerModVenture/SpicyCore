package com.spicymemes.api.network

import com.spicymemes.api.serialization.*
import net.fabricmc.fabric.api.networking.v1.*

inline fun <reified M : PacketIdentifier> PacketSender.sendPacket(packet: M) =
    sendPacket(packet.identifier, encodeToByteBuf(packet))
