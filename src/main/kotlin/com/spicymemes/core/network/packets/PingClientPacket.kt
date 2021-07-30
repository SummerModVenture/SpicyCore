package com.spicymemes.core.network.packets

import com.spicymemes.core.MainMod
import com.spicymemes.core.network.*
import net.minecraft.network.*
import net.minecraftforge.fmllegacy.network.*

class PingClientPacket(val timestamp: Long) : SpicyPacket {

    companion object Handler : SpicyPacketHandler<PingClientPacket> {

        override fun process(packet: PingClientPacket, ctx: NetworkEvent.Context) {
            MainMod.logger.info("Received ping from server, sending response packet.")
            MainMod.INSTANCE.sendToServer(ClientPresentPacket(packet.timestamp))
        }

        override fun encode(packet: PingClientPacket, buf: FriendlyByteBuf) {
            buf.writeLong(packet.timestamp)
        }

        override fun decode(buf: FriendlyByteBuf) = PingClientPacket(buf.readLong())
    }
}