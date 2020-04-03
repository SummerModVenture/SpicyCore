package com.spicymemes.core

import com.spicymemes.core.network.packets.PingClientPacket
import net.minecraft.entity.player.ServerPlayerEntity
import net.minecraftforge.event.entity.player.PlayerEvent.PlayerLoggedInEvent
import net.minecraftforge.eventbus.api.SubscribeEvent
import net.minecraftforge.fml.network.PacketDistributor
import java.time.Instant

object EventHandler {

    @SubscribeEvent
    fun onPlayerJoinServer(event: PlayerLoggedInEvent) {
        MainMod.INSTANCE.send(PacketDistributor.PLAYER.with { event.player as ServerPlayerEntity },
                PingClientPacket(Instant.now().toEpochMilli())
        )
    }
}