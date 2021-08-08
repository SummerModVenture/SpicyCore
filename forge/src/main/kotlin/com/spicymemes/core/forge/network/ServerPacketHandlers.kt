package com.spicymemes.core.forge.network

import com.spicymemes.core.common.*
import com.spicymemes.core.common.network.*
import com.spicymemes.core.forge.api.network.*
import net.minecraftforge.fmllegacy.network.*
import java.time.*

object ClientPresentPacketHandler : ForgePacketHandler<ClientPresentPacket> {

    override fun process(packet: ClientPresentPacket, ctx: NetworkEvent.Context) {
        SpicyCoreLogger.info(
            "Received response from client, SpicyCore is present! Client took " +
                "${Instant.now().toEpochMilli() - packet.timestamp} ms to respond."
        )
    }
}