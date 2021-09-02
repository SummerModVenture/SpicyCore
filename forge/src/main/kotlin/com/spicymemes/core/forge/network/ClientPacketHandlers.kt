package com.spicymemes.core.forge.network

import com.spicymemes.core.common.SpicyCoreLogger
import com.spicymemes.core.common.network.ClientPresentPacket
import com.spicymemes.core.common.network.PingClientPacket
import com.spicymemes.core.forge.api.network.ForgePacketHandler
import net.minecraftforge.fmllegacy.network.NetworkEvent

object PingClientPacketHandler : ForgePacketHandler<PingClientPacket> {

    override fun process(packet: PingClientPacket, ctx: NetworkEvent.Context) {
        SpicyCoreLogger.info("Received ping from server, sending response packet.")
        Network.mainChannel.sendToServer(ClientPresentPacket(packet.timestamp))
    }
}