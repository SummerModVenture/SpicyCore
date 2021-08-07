package com.spicymemes.common.network

import com.spicymemes.api.network.*
import com.spicymemes.common.*
import kotlinx.serialization.*
import java.time.*

fun id(identifier: String): PacketIdentifier = PacketIdentifierImpl(MOD_ID, identifier)

@Serializable
data class PingClientPacket(val timestamp: Long = Instant.now().epochSecond) {
    companion object : PacketIdentifier by id("ping_client")
}

@Serializable
data class ClientPresentPacket(val timestamp: Long) {
    companion object : PacketIdentifier by id("client_present")
}
