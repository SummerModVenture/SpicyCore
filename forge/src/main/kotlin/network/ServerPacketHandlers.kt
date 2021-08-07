package com.spicymemes.core.network

import com.spicymemes.api.network.*
import com.spicymemes.common.network.ClientPresentPacket
import com.spicymemes.core.*
import net.minecraftforge.fmllegacy.network.*
import java.time.*

object ClientPresentPacketHandler : ForgePacketHandler<ClientPresentPacket> {

    override fun process(packet: ClientPresentPacket, ctx: NetworkEvent.Context) {
        ForgeMod.logger.info(
            "Received response from client, SpicyCore is present! Client took " +
                "${Instant.now().toEpochMilli() - packet.timestamp} ms to respond."
        )
    }
}