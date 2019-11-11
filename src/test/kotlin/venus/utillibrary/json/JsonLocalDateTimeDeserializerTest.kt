package venus.utillibrary.json

import com.fasterxml.jackson.databind.ObjectMapper
import org.amshove.kluent.`should be equal to`
import org.amshove.kluent.`should be`
import org.junit.Test
import java.time.LocalDateTime
import java.time.Month

class JsonLocalDateTimeDeserializerTest {
    private val objectMapper = ObjectMapper()
    private val testSubject = JsonLocalDateTimeDeserializer()

    @Test
    fun `Deserialize LocalDateTime from string`() {
        //given
        val json = """{"value":"2017-04-26 01:02:03"}"""

        //when
        val result = deserialize(json)

        //then
        result.year `should be equal to` 2017
        result.month `should be` Month.APRIL
        result.dayOfMonth `should be equal to` 26
        result.hour `should be equal to` 1
        result.minute `should be equal to` 2
        result.second `should be equal to` 3
    }

    private fun deserialize(json: String): LocalDateTime {
        json.byteInputStream().use {
            val parser = objectMapper.factory.createParser(it)
            val ctxt = objectMapper.deserializationContext
            parser.nextToken()
            parser.nextToken()
            parser.nextToken()
            return testSubject.deserialize(parser, ctxt)
        }
    }
}
