package venus.utillibrary.json

import com.fasterxml.jackson.core.JsonGenerator
import com.fasterxml.jackson.databind.JsonSerializer
import com.fasterxml.jackson.databind.SerializerProvider
import java.time.LocalDate
import java.time.format.DateTimeFormatter

class JsonLocalDateSerializer : JsonSerializer<LocalDate>() {
    companion object {
        private val FORMATTER = DateTimeFormatter.ofPattern("yyyy-MM-dd")
    }

    override fun serialize(date: LocalDate, generator: JsonGenerator, provider: SerializerProvider) {
        val dateString = date.format(FORMATTER)
        generator.writeString(dateString)
    }
}
