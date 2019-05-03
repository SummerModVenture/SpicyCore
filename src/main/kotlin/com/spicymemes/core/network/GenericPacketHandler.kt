package com.spicymemes.core.network

import com.spicymemes.core.MainMod
import net.minecraft.client.Minecraft
import net.minecraftforge.fml.common.network.simpleimpl.IMessage
import net.minecraftforge.fml.common.network.simpleimpl.IMessageHandler
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext
import net.minecraftforge.fml.relauncher.Side

abstract class GenericPacketHandler<REQ : IMessage> : IMessageHandler<REQ, IMessage> {

    override fun onMessage(message: REQ, ctx: MessageContext): IMessage? {
        val task = Runnable { processMessage(message, ctx) }

        if (ctx.side == Side.CLIENT)
            Minecraft.getMinecraft().addScheduledTask(task)
        else if (ctx.side == Side.SERVER) {
            val player = ctx.serverHandler.player
            if (player != null)
                player.server!!.addScheduledTask(task)
            else
                MainMod.logger.warn("GenericPacketHandler error: EntityPlayerMP is null!")
        }

        return null
    }

    abstract fun processMessage(message: REQ, ctx: MessageContext)
}