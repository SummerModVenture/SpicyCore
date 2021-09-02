package com.spicymemes.core.forge

import com.spicymemes.core.common.MOD_ID
import com.spicymemes.core.common.network.PingClientPacket
import com.spicymemes.core.forge.network.Network
import net.minecraft.server.level.ServerPlayer
import net.minecraftforge.event.entity.player.PlayerEvent
import net.minecraftforge.eventbus.api.SubscribeEvent
import net.minecraftforge.fml.common.Mod
import net.minecraftforge.fmllegacy.network.PacketDistributor
import java.time.Instant

@Mod.EventBusSubscriber(modid = MOD_ID, bus = Mod.EventBusSubscriber.Bus.FORGE)
object EventHandler {

    @SubscribeEvent
    @JvmStatic
    fun onPlayerJoinServer(event: PlayerEvent.PlayerLoggedInEvent) {
        Network.mainChannel.send(
            PacketDistributor.PLAYER.with { event.player as ServerPlayer },
            PingClientPacket(Instant.now().toEpochMilli())
        )
    }
}