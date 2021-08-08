package com.spicymemes.core.forge.api.extensions

import net.minecraftforge.common.util.*

fun <T> LazyOptional<T>.unwrap(): T? = orElse(null)
