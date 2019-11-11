package venus.utillibrary.json

import com.fasterxml.jackson.databind.module.SimpleModule
import java.time.LocalDate
import java.time.LocalDateTime

class LocalDateModule : SimpleModule("LocalDateModule") {

    init {
        addSerializer(LocalDate::class.java, JsonLocalDateSerializer())
        addDeserializer(LocalDate::class.java, JsonLocalDateDeserializer())
        addSerializer(LocalDateTime::class.java, JsonLocalDateTimeSerializer())
        addDeserializer(LocalDateTime::class.java, JsonLocalDateTimeDeserializer())
    }
}