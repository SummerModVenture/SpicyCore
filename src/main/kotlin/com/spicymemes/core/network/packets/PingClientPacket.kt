package com.spicymemes.core.network.packets

import com.spicymemes.core.MainMod
import com.spicymemes.core.network.SpicyPacket
import com.spicymemes.core.network.SpicyPacketHandler
import net.minecraft.network.PacketBuffer
import net.minecraftforge.fml.network.NetworkEvent

class PingClientPacket(val timestamp: Long) : SpicyPacket {

    companion object Handler : SpicyPacketHandler<PingClientPacket> {

        override fun process(packet: PingClientPacket, ctx: NetworkEvent.Context) {
            MainMod.logger.info("Received ping from server, sending response packet.")
            MainMod.INSTANCE.sendToServer(ClientPresentPacket(packet.timestamp))
        }

        override fun encode(packet: PingClientPacket, buf: PacketBuffer) {
            buf.writeLong(packet.timestamp)
        }

        override fun decode(buf: PacketBuffer) = PingClientPacket(buf.readLong())
    }
}