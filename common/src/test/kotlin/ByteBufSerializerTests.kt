import com.spicymemes.core.api.serialization.bufferedPacket
import com.spicymemes.core.api.serialization.decodeFromByteBuf
import com.spicymemes.core.api.serialization.encodeToByteBuf
import kotlinx.serialization.Serializable
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test
import java.nio.charset.Charset

class ByteBufSerializerTests {

    @Serializable
    data class TestPacket(val name: String, val age: Int, val state: Boolean, val nullable: Int?)

    val name = "Spicy"
    val age = 2
    val state = false
    val nullable: Int? = null

    @Test
    fun `Serialize ByteBuf`() {
        val testPacket = TestPacket(name, age, state, nullable)

        val buf = encodeToByteBuf(testPacket)

        assertEquals(buf.readCharSequence(buf.readInt(), Charset.defaultCharset()), name)
        assertEquals(buf.readInt(), age)
        assertEquals(buf.readBoolean(), state)
        assertEquals(buf.readByte().toInt(), 0)
    }

    @Test
    fun `Deserialize ByteBuf`() {
        val buf = bufferedPacket()

        buf.writeInt(name.length)
        buf.writeCharSequence(name, Charset.defaultCharset())
        buf.writeInt(age)
        buf.writeBoolean(state)
        buf.writeByte(0)

        val packet: TestPacket = decodeFromByteBuf(buf)

        assertEquals(packet.name, name)
        assertEquals(packet.age, age)
        assertEquals(packet.state, state)
        assertEquals(packet.nullable, null)
    }
}
