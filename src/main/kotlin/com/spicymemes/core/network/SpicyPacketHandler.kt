package com.spicymemes.core.network

import net.minecraft.network.*
import net.minecraftforge.fmllegacy.network.*
import java.util.function.Supplier

interface SpicyPacketHandler<MSG : SpicyPacket> {

    fun process(packet: MSG, ctx: NetworkEvent.Context)

    fun encode(packet: MSG, buf: FriendlyByteBuf)

    fun decode(buf: FriendlyByteBuf): MSG

    fun handle(message: MSG, ctxSupplier: Supplier<NetworkEvent.Context>) {
        ctxSupplier.get().also { ctx ->
            ctx.enqueueWork(Runnable {
                process(message, ctx)
                ctx.packetHandled = true
            })
        }
    }
}
