package com.spicymemes.core.network

import com.spicymemes.api.network.*
import com.spicymemes.common.*
import com.spicymemes.core.*
import net.minecraftforge.fmllegacy.network.simple.*

object Network {

    val mainChannel: SimpleChannel = newSimpleChannel("1", MOD_ID)

    fun registerPackets() {
        var id = 0
        mainChannel.registerPacket(id++, PingClientPacket.Handler)
        mainChannel.registerPacket(id++, ClientPresentPacket.Handler)
    }
}
