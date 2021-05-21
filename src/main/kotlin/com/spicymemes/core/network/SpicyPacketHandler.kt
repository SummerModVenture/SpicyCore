package com.spicymemes.core.network

import net.minecraft.network.PacketBuffer
import net.minecraftforge.fml.network.NetworkEvent
import java.util.function.Supplier

interface SpicyPacketHandler<MSG : SpicyPacket> {

    fun process(packet: MSG, ctx: NetworkEvent.Context)

    fun encode(packet: MSG, buf: PacketBuffer)

    fun decode(buf: PacketBuffer): MSG

    fun handle(message: MSG, ctxSupplier: Supplier<NetworkEvent.Context>) {
        ctxSupplier.get().also { ctx ->
            ctx.enqueueWork {
                process(message, ctx)
                ctx.packetHandled = true
            }
        }
    }
}
