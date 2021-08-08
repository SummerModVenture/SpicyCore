package com.spicymemes.api.network

import net.minecraft.resources.*

interface PacketIdentifier {

    val identifier: ResourceLocation
}

class PacketIdentifierImpl(modId: String, identifier: String) : PacketIdentifier {

    override val identifier = ResourceLocation(modId, identifier)
}
