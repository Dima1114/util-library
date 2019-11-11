package venus.utillibrary.json

import com.fasterxml.jackson.databind.ObjectMapper
import org.amshove.kluent.`should be equal to`
import org.amshove.kluent.`should be`
import org.junit.Test
import java.time.LocalDate
import java.time.Month

class JsonLocalDateDeserializerTest {
    private val objectMapper = ObjectMapper()
    private val testSubject = JsonLocalDateDeserializer()

    @Test
    fun `Deserialize LocalDate from string`() {
        //given
        val json = """{"value":"2017-04-26"}"""

        //when
        val result = deserialize(json)

        //then
        result.year `should be equal to` 2017
        result.month `should be` Month.APRIL
        result.dayOfMonth `should be equal to` 26
    }

    private fun deserialize(json: String): LocalDate {
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
