package com.spicymemes.api.network

import net.minecraftforge.fmllegacy.network.*
import java.util.function.*

interface ForgePacketHandler<M> {

    fun process(packet: M, ctx: NetworkEvent.Context)

    fun handle(message: M, ctxSupplier: Supplier<NetworkEvent.Context>) {
        ctxSupplier.get().also { ctx ->
            ctx.enqueueWork {
                process(message, ctx)
                ctx.packetHandled = true
            }
        }
    }
}
