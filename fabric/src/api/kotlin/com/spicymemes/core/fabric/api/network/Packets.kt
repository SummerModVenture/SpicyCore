package com.spicymemes.core.fabric.api.network

import com.spicymemes.core.api.network.PacketIdentifier
import com.spicymemes.core.api.serialization.encodeToByteBuf
import net.fabricmc.fabric.api.networking.v1.PacketSender

inline fun <reified M : PacketIdentifier> PacketSender.sendPacket(packet: M) =
    sendPacket(packet.identifier, encodeToByteBuf(packet))
