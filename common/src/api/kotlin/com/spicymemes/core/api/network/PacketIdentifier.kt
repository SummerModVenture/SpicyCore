package com.spicymemes.core.api.network

import net.minecraft.resources.ResourceLocation

interface PacketIdentifier {

    val identifier: ResourceLocation
}

class PacketIdentifierImpl(modId: String, identifier: String) : PacketIdentifier {

    override val identifier = ResourceLocation(modId, identifier)
}
