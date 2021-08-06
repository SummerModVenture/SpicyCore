package com.spicymemes.core

import com.spicymemes.core.network.*
import net.minecraft.server.level.*
import net.minecraft.world.entity.*
import net.minecraftforge.event.entity.player.PlayerEvent.PlayerLoggedInEvent
import net.minecraftforge.eventbus.api.SubscribeEvent
import net.minecraftforge.fmllegacy.network.*
import java.time.Instant

object EventHandler {

    @SubscribeEvent
    fun onPlayerJoinServer(event: PlayerLoggedInEvent) {
        Network.mainChannel.send(
            PacketDistributor.PLAYER.with { event.player as ServerPlayer },
                PingClientPacket(Instant.now().toEpochMilli())
        )
    }
}