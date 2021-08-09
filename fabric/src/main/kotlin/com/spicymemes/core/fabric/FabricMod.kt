package com.spicymemes.core.fabric

import com.spicymemes.core.api.serialization.*
import com.spicymemes.core.common.*
import com.spicymemes.core.common.network.*
import com.spicymemes.core.fabric.api.network.*
import net.fabricmc.api.*
import net.fabricmc.fabric.api.networking.v1.*
import java.time.*

object FabricMod : ModInitializer {

    override fun onInitialize() {
        ServerLoginNetworking.registerGlobalReceiver(ClientPresentPacket.identifier) { server, handler, understood, buf, synchronizer, responseSender ->
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
}
