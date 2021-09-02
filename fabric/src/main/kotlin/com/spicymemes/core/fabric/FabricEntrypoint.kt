package com.spicymemes.core.fabric

import com.spicymemes.core.api.serialization.decodeFromByteBuf
import com.spicymemes.core.api.serialization.encodeToByteBuf
import com.spicymemes.core.common.MOD_NAME
import com.spicymemes.core.common.SpicyCoreLogger
import com.spicymemes.core.common.network.ClientPresentPacket
import com.spicymemes.core.common.network.PingClientPacket
import com.spicymemes.core.fabric.api.network.sendPacket
import net.fabricmc.api.ClientModInitializer
import net.fabricmc.api.ModInitializer
import net.fabricmc.fabric.api.client.networking.v1.ClientLoginNetworking
import net.fabricmc.fabric.api.networking.v1.ServerLoginConnectionEvents
import net.fabricmc.fabric.api.networking.v1.ServerLoginNetworking
import java.time.Instant
import java.util.concurrent.CompletableFuture

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
