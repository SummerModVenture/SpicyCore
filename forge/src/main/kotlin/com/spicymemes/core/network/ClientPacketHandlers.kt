package com.spicymemes.core.network

import com.spicymemes.api.network.*
import com.spicymemes.common.*
import com.spicymemes.common.network.*
import net.minecraftforge.fmllegacy.network.*

object PingClientPacketHandler : ForgePacketHandler<PingClientPacket> {

    override fun process(packet: PingClientPacket, ctx: NetworkEvent.Context) {
        SpicyCoreLogger.info("Received ping from server, sending response packet.")
        Network.mainChannel.sendToServer(ClientPresentPacket(packet.timestamp))
    }
}