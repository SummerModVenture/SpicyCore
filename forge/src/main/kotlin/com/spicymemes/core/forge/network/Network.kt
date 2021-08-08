package com.spicymemes.core.forge.network

import com.spicymemes.core.common.*
import com.spicymemes.core.forge.api.network.*
import net.minecraftforge.fmllegacy.network.simple.*

object Network {

    val mainChannel: SimpleChannel = newSimpleChannel("1", MOD_ID)

    fun registerPackets() {
        var id = 0
        mainChannel.registerPacket(id++, PingClientPacketHandler)
        mainChannel.registerPacket(id++, ClientPresentPacketHandler)
    }
}
