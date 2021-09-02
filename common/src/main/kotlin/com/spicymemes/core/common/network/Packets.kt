package com.spicymemes.core.common.network

import com.spicymemes.core.api.network.PacketIdentifier
import com.spicymemes.core.api.network.PacketIdentifierImpl
import com.spicymemes.core.common.MOD_ID
import kotlinx.serialization.Serializable
import java.time.Instant

fun id(identifier: String): PacketIdentifier = PacketIdentifierImpl(MOD_ID, identifier)

@Serializable
data class PingClientPacket(val timestamp: Long = Instant.now().toEpochMilli()) : PacketIdentifier by Companion {
    companion object : PacketIdentifier by id("ping_client")
}

@Serializable
data class ClientPresentPacket(val timestamp: Long) : PacketIdentifier by Companion {
    companion object : PacketIdentifier by id("ping_client")
}
