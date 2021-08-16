@file:UseSerializers(BlockPosSerializer::class)

import com.spicymemes.core.api.serialization.*
import kotlinx.serialization.*
import net.minecraft.core.*
import org.junit.jupiter.api.*
import org.junit.jupiter.api.Assertions.*

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
