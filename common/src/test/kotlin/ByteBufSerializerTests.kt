
import com.spicymemes.api.serialization.*
import kotlinx.serialization.*
import org.junit.jupiter.api.*
import java.nio.charset.*

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

        assert(buf.readCharSequence(buf.readInt(), Charset.defaultCharset()) == name)
        assert(buf.readInt() == age)
        assert(buf.readBoolean() == state)
        assert(buf.readByte().toInt() == 0)
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

        assert(packet.name == name)
        assert(packet.age == age)
        assert(packet.state == state)
        assert(packet.nullable == null)
    }
}
