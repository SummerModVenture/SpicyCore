package com.spicymemes.core.forge.network

import com.spicymemes.core.common.*
import com.spicymemes.core.common.network.*
import com.spicymemes.core.forge.api.network.*
import net.minecraftforge.fmllegacy.network.*

object PingClientPacketHandler : ForgePacketHandler<PingClientPacket> {

    override fun process(packet: PingClientPacket, ctx: NetworkEvent.Context) {
        SpicyCoreLogger.info("Received ping from server, sending response packet.")
        Network.mainChannel.sendToServer(ClientPresentPacket(packet.timestamp))
    }
}