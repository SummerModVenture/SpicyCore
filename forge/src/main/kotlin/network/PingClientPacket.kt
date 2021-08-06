package com.spicymemes.core.network

import com.spicymemes.api.network.*
import com.spicymemes.core.*
import net.minecraft.network.*
import net.minecraftforge.fmllegacy.network.*

class PingClientPacket(val timestamp: Long) : SpicyPacket {

    companion object Handler : SpicyPacketHandler<PingClientPacket> {

        override fun process(packet: PingClientPacket, ctx: NetworkEvent.Context) {
            ForgeMod.logger.info("Received ping from server, sending response packet.")
            Network.mainChannel.sendToServer(ClientPresentPacket(packet.timestamp))
        }

        override fun encode(packet: PingClientPacket, buf: FriendlyByteBuf) {
            buf.writeLong(packet.timestamp)
        }

        override fun decode(buf: FriendlyByteBuf) = PingClientPacket(buf.readLong())
    }
}