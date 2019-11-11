package venus.utillibrary.json

import com.fasterxml.jackson.core.JsonGenerator
import com.fasterxml.jackson.databind.JsonSerializer
import com.fasterxml.jackson.databind.SerializerProvider
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter

class JsonLocalDateTimeSerializer : JsonSerializer<LocalDateTime>() {
    companion object {
        private val FORMATTER = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")
    }

    override fun serialize(value: LocalDateTime, generator: JsonGenerator, provider: SerializerProvider) {
        val dateString = value.format(FORMATTER)
        generator.writeString(dateString)
    }
}
