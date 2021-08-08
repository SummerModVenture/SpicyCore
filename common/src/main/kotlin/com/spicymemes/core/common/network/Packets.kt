package com.spicymemes.core.common.network

import com.spicymemes.core.api.network.*
import com.spicymemes.core.common.*
import kotlinx.serialization.*
import java.time.*

fun id(identifier: String): PacketIdentifier = PacketIdentifierImpl(MOD_ID, identifier)

@Serializable
data class PingClientPacket(val timestamp: Long = Instant.now().epochSecond) : PacketIdentifier by Companion {
    companion object : PacketIdentifier by id("ping_client")
}

@Serializable
data class ClientPresentPacket(val timestamp: Long) : PacketIdentifier by Companion {
    companion object : PacketIdentifier by id("client_present")
}
