@file:JvmName("Block")

package com.spicymemes.core.extensions

import net.minecraft.block.Block

@Deprecated("Use registryName instead.")
var Block.codename: String
    get() = registryName!!.namespace
    set(value) = Unit.apply { setRegistryName(value) }

@Deprecated("Use registryName instead.", ReplaceWith("setRegistryName(name)"))
fun Block.setCodename(name: String): Block {
    return setRegistryName(name)
}
