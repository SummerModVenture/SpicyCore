package com.spicymemes.core.network.packets

import com.spicymemes.core.MainMod
import com.spicymemes.core.network.SpicyPacket
import com.spicymemes.core.network.SpicyPacketHandler
import net.minecraft.network.PacketBuffer
import net.minecraftforge.fml.network.NetworkEvent
import java.time.Instant

class ClientPresentPacket(val timestamp: Long) : SpicyPacket {

    companion object Handler : SpicyPacketHandler<ClientPresentPacket> {

        override fun process(packet: ClientPresentPacket, ctx: NetworkEvent.Context) {
            MainMod.logger.info(
                    "Received response from client, SpicyCore is present! Client took " +
                            "${Instant.now().toEpochMilli() - packet.timestamp} ms to respond."
            )
        }

        override fun encode(packet: ClientPresentPacket, buf: PacketBuffer) {
            buf.writeLong(packet.timestamp)
        }

        override fun decode(buf: PacketBuffer) = ClientPresentPacket(buf.readLong())
    }
}