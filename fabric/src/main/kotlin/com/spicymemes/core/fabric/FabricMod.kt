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
        ServerLoginConnectionEvents.INIT.register { handler, server ->
            ServerLoginNetworking.registerGlobalReceiver(ClientPresentPacket.identifier) { server, handler, understood, buf, synchronizer, responseSender ->
                val packet: ClientPresentPacket = decodeFromByteBuf(buf)
                SpicyCoreLogger.info(
                    "Received response from client, SpicyCore is present! Client took " +
                        "${Instant.now().toEpochMilli() - packet.timestamp} ms to respond."
                )
            }
        }

        ServerLoginConnectionEvents.QUERY_START.register { handler, server, sender, sync ->
            sender.sendPacket(PingClientPacket())
        }
    }
}
