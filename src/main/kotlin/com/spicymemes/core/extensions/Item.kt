@file:JvmName("Item")

package com.spicymemes.core.extensions

import net.minecraft.item.Item

@Deprecated("Use registryName instead.")
var Item.codename: String
    get() = registryName!!.namespace
    set(value) = Unit.apply { setRegistryName(value) }

@Deprecated("Use registryName instead.", ReplaceWith("setRegistryName(name)"))
fun Item.setCodename(name: String): Item {
    return setRegistryName(name)
}
