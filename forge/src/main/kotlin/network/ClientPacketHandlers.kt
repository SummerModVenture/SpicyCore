package com.spicymemes.core.network

import com.spicymemes.api.network.*
import com.spicymemes.common.network.*
import com.spicymemes.core.*
import net.minecraftforge.fmllegacy.network.*

object PingClientPacketHandler : ForgePacketHandler<PingClientPacket> {

    override fun process(packet: PingClientPacket, ctx: NetworkEvent.Context) {
        ForgeMod.logger.info("Received ping from server, sending response packet.")
        Network.mainChannel.sendToServer(ClientPresentPacket(packet.timestamp))
    }
}