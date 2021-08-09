package com.spicymemes.core.fabric

import com.spicymemes.core.api.serialization.*
import com.spicymemes.core.common.*
import com.spicymemes.core.common.network.*
import com.spicymemes.core.fabric.api.network.*
import net.fabricmc.api.*
import net.fabricmc.fabric.api.client.networking.v1.*
import net.fabricmc.fabric.api.networking.v1.*
import java.time.*
import java.util.concurrent.*

object FabricEntrypoint : ModInitializer, ClientModInitializer {

    override fun onInitialize() {
        ServerLoginNetworking.registerGlobalReceiver(ClientPresentPacket.identifier) { _, _, understood, buf, _, _ ->
            if (understood) {
                val packet: ClientPresentPacket = decodeFromByteBuf(buf)
                SpicyCoreLogger.info(
                    "Received response from client, $MOD_NAME is present! Client took " +
                        "${Instant.now().toEpochMilli() - packet.timestamp} ms to respond."
                )
            }
        }

        ServerLoginConnectionEvents.QUERY_START.register { handler, server, sender, sync ->
            sender.sendPacket(PingClientPacket())
        }
    }

    override fun onInitializeClient() {
        ClientLoginNetworking.registerGlobalReceiver(PingClientPacket.identifier) { _, _, buf, _ ->
            val packet: PingClientPacket = decodeFromByteBuf(buf)
            SpicyCoreLogger.info("Received ping from server, sending response packet.")

            CompletableFuture.completedFuture(encodeToByteBuf(ClientPresentPacket(packet.timestamp)))
        }
    }
}
