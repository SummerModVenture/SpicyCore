package com.spicymemes.core.network

import com.spicymemes.api.network.*
import com.spicymemes.core.*
import net.minecraft.network.*
import net.minecraftforge.fmllegacy.network.*
import java.time.Instant

class ClientPresentPacket(val timestamp: Long) : SpicyPacket {

    companion object Handler : SpicyPacketHandler<ClientPresentPacket> {

        override fun process(packet: ClientPresentPacket, ctx: NetworkEvent.Context) {
            ForgeMod.logger.info(
                    "Received response from client, SpicyCore is present! Client took " +
                            "${Instant.now().toEpochMilli() - packet.timestamp} ms to respond."
            )
        }

        override fun encode(packet: ClientPresentPacket, buf: FriendlyByteBuf) {
            buf.writeLong(packet.timestamp)
        }

        override fun decode(buf: FriendlyByteBuf) = ClientPresentPacket(buf.readLong())
    }
}