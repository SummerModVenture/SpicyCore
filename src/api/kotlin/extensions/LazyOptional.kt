package com.spicymemes.api.extensions

import net.minecraftforge.common.util.*

fun <T> LazyOptional<T>.unwrap(): T? = orElse(null)
