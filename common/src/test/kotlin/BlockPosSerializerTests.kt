@file:UseSerializers(BlockPosSerializer::class)

import com.spicymemes.core.api.serialization.BlockPosSerializer
import com.spicymemes.core.api.serialization.decodeFromByteBuf
import com.spicymemes.core.api.serialization.encodeToByteBuf
import kotlinx.serialization.Serializable
import kotlinx.serialization.UseSerializers
import net.minecraft.core.BlockPos
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test

class BlockPosSerializerTests {

    @Serializable
    data class TestPacket(val pos: BlockPos)

    @Test
    fun `Serialize BlockPos`() {
        val randomBlocks = sequence {
            val horizontalRange = (-29_999_999..29_999_999)
            val verticalRange = (-2048..2047)
            for (i in 0 until 1000)
                yield(BlockPos(horizontalRange.random(), verticalRange.random(), horizontalRange.random()))
        }.map { TestPacket(it) }.toList()

        val randomBlocksSerialized = randomBlocks
            .map { encodeToByteBuf(it) }
            .map { decodeFromByteBuf<TestPacket>(it) }
            .toList()

        assertEquals(randomBlocks, randomBlocksSerialized)
    }
}
