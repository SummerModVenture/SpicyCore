package com.spicymemes.core

import com.spicymemes.api.serialization.*
import com.spicymemes.common.*
import com.spicymemes.common.network.*
import net.fabricmc.api.*
import net.fabricmc.fabric.api.networking.v1.*
import java.time.*

object FabricMod : ModInitializer {

    override fun onInitialize() {
        ServerLoginNetworking.registerGlobalReceiver(ClientPresentPacket.identifier) { server, handler, understood, buf, synchronizer, responseSender ->
            val packet: ClientPresentPacket = decodeFromByteBuf(buf)
            SpicyCoreLogger.info(
                "Received response from client, SpicyCore is present! Client took " +
                    "${Instant.now().toEpochMilli() - packet.timestamp} ms to respond."
            )
        }

        ServerLoginConnectionEvents.QUERY_START.register { handler, server, sender, sync ->
            sender.sendPacket(PingClientPacket.identifier, encodeToByteBuf(PingClientPacket()))
        }
    }
}
