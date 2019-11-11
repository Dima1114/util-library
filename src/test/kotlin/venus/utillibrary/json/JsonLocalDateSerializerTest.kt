package venus.utillibrary.json

import com.fasterxml.jackson.core.JsonFactory
import com.fasterxml.jackson.databind.ObjectMapper
import org.amshove.kluent.`should be equal to`
import org.junit.Test
import venus.utillibrary.json.JsonLocalDateSerializer
import java.io.StringWriter
import java.time.LocalDate
import java.time.Month

class JsonLocalDateSerializerTest {
    private val testSubject = JsonLocalDateSerializer()

    @Test
    fun `Serialize LocalDate to string`() {
        //given
        val date = LocalDate.of(2017, Month.APRIL, 26)
        val jsonWriter = StringWriter()
        val generator = JsonFactory().createGenerator(jsonWriter)
        val provider = ObjectMapper().serializerProvider

        //when
        testSubject.serialize(date, generator, provider)
        generator.flush()

        //then
        jsonWriter.toString() `should be equal to` """"2017-04-26""""
    }
}
