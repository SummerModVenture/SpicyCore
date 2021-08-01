package com.spicymemes.core.network

import com.spicymemes.api.network.*
import com.spicymemes.core.*

object ModPackets {

    fun registerPackets() {
        var id = 0
        MainMod.INSTANCE.registerPacket(id++, PingClientPacket.Handler)
        MainMod.INSTANCE.registerPacket(id++, ClientPresentPacket.Handler)
    }
}
