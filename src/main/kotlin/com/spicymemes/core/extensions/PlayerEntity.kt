package com.spicymemes.core.extensions

import com.spicymemes.core.MainMod.*
import net.minecraft.client.entity.player.*
import net.minecraft.entity.player.*
import java.lang.IllegalStateException

fun PlayerEntity.toServerPlayerEntity(): ServerPlayerEntity {
    return try {
        this as ServerPlayerEntity
    } catch (e: Throwable) {
        "Could not cast PlayerEntity to ServerPlayerEntity.".let {
            logger.error(it, e)
            throw IllegalStateException(it, e)
        }
    }
}

fun PlayerEntity.toClientPlayerEntity(): ClientPlayerEntity {
    return try {
        this as ClientPlayerEntity
    } catch (e: Throwable) {
        "Could not cast PlayerEntity to ClientPlayerEntity.".let {
            logger.error(it, e)
            throw IllegalStateException(it, e)
        }
    }
}
