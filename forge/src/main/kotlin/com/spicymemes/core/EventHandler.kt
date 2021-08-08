package com.spicymemes.core

import com.spicymemes.common.*
import com.spicymemes.common.network.*
import com.spicymemes.core.network.*
import net.minecraft.server.level.*
import net.minecraftforge.event.entity.player.PlayerEvent.*
import net.minecraftforge.eventbus.api.*
import net.minecraftforge.fml.common.*
import net.minecraftforge.fmllegacy.network.*
import java.time.*

@Mod.EventBusSubscriber(modid = MOD_ID, bus = Mod.EventBusSubscriber.Bus.FORGE)
object EventHandler {

    @SubscribeEvent
    @JvmStatic
    fun onPlayerJoinServer(event: PlayerLoggedInEvent) {
        Network.mainChannel.send(
            PacketDistributor.PLAYER.with { event.player as ServerPlayer },
                PingClientPacket(Instant.now().toEpochMilli())
        )
    }
}