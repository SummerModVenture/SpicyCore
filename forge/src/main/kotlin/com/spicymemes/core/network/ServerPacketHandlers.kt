package com.spicymemes.core.network

import com.spicymemes.api.network.*
import com.spicymemes.common.*
import com.spicymemes.common.network.*
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