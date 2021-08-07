package com.spicymemes.core

import com.spicymemes.common.network.*
import net.fabricmc.api.*
import net.fabricmc.fabric.api.client.networking.v1.*

object FabricClientMod : ClientModInitializer {

    override fun onInitializeClient() {
        ClientPlayNetworking.registerGlobalReceiver(PingClientPacket.identifier) { minecraft, handler, buf, sender ->

        }
    }
}
