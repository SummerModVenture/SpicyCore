package com.spicymemes.core.forge.network

import com.spicymemes.core.common.SpicyCoreLogger
import com.spicymemes.core.common.network.ClientPresentPacket
import com.spicymemes.core.forge.api.network.ForgePacketHandler
import net.minecraftforge.fmllegacy.network.NetworkEvent
import java.time.Instant

object ClientPresentPacketHandler : ForgePacketHandler<ClientPresentPacket> {

    override fun process(packet: ClientPresentPacket, ctx: NetworkEvent.Context) {
        SpicyCoreLogger.info(
            "Received response from client, SpicyCore is present! Client took " +
                "${Instant.now().toEpochMilli() - packet.timestamp} ms to respond."
        )
    }
}