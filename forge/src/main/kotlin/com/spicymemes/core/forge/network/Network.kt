package com.spicymemes.core.forge.network

import com.spicymemes.core.common.MOD_ID
import com.spicymemes.core.forge.api.network.newSimpleChannel
import com.spicymemes.core.forge.api.network.registerPacket
import net.minecraftforge.fmllegacy.network.simple.SimpleChannel

object Network {

    val mainChannel: SimpleChannel = newSimpleChannel("1", MOD_ID)

    fun registerPackets() {
        var id = 0
        mainChannel.registerPacket(id++, PingClientPacketHandler)
        mainChannel.registerPacket(id++, ClientPresentPacketHandler)
    }
}
