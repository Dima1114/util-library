package venus.utillibrary.json

import com.fasterxml.jackson.core.JsonFactory
import com.fasterxml.jackson.databind.ObjectMapper
import org.amshove.kluent.`should be equal to`
import org.junit.Test
import venus.utillibrary.json.JsonLocalDateTimeSerializer
import java.io.StringWriter
import java.time.LocalDate
import java.time.LocalDateTime
import java.time.LocalTime
import java.time.Month

class JsonLocalDateTimeSerializerTest {
    private val testSubject = JsonLocalDateTimeSerializer()

    @Test
    fun `Serialize LocalDateTime to string`() {
        //given
        val date = LocalDateTime.of(LocalDate.of(2017, Month.APRIL, 26), LocalTime.of(1, 2, 3))
        val jsonWriter = StringWriter()
        val generator = JsonFactory().createGenerator(jsonWriter)
        val provider = ObjectMapper().serializerProvider

        //when
        testSubject.serialize(date, generator, provider)
        generator.flush()

        //then
        jsonWriter.toString() `should be equal to` """"2017-04-26 01:02:03""""
    }
}
