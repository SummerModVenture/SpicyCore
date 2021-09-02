package com.spicymemes.core.forge.api.network

import net.minecraftforge.fmllegacy.network.NetworkEvent
import java.util.function.Supplier

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
