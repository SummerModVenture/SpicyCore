package com.spicymemes.core.fabric.api.network

import com.spicymemes.core.api.network.*
import com.spicymemes.core.api.serialization.*
import net.fabricmc.fabric.api.networking.v1.*

inline fun <reified M : PacketIdentifier> PacketSender.sendPacket(packet: M) =
    sendPacket(packet.identifier, encodeToByteBuf(packet))
