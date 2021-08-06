package com.spicymemes.api.network

import net.minecraft.network.*
import net.minecraftforge.fmllegacy.network.*
import java.util.function.Supplier

interface SpicyPacketHandler<M : SpicyPacket> {

    fun process(packet: M, ctx: NetworkEvent.Context)

    fun encode(packet: M, buf: FriendlyByteBuf)

    fun decode(buf: FriendlyByteBuf): M

    fun handle(message: M, ctxSupplier: Supplier<NetworkEvent.Context>) {
        ctxSupplier.get().also { ctx ->
            ctx.enqueueWork(Runnable {
                process(message, ctx)
                ctx.packetHandled = true
            })
        }
    }
}
