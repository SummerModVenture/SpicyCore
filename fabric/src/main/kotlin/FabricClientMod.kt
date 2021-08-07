package com.spicymemes.core

import com.spicymemes.api.network.*
import com.spicymemes.api.serialization.*
import com.spicymemes.common.*
import com.spicymemes.common.network.*
import net.fabricmc.api.*
import net.fabricmc.fabric.api.client.networking.v1.*
import java.time.*
import java.util.concurrent.*

object FabricClientMod : ClientModInitializer {

    override fun onInitializeClient() {
        ClientLoginNetworking.registerGlobalReceiver(PingClientPacket.identifier) { minecraft, handler, buf, listenerAdder ->
            val packet: PingClientPacket = decodeFromByteBuf(buf)
            SpicyCoreLogger.info("Received ping from server, sending response packet.")

            CompletableFuture.completedFuture(encodeToByteBuf(ClientPresentPacket(packet.timestamp)))
        }
    }
}
