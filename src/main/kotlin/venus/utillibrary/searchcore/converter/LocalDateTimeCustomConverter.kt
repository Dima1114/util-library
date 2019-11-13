package venus.utillibrary.searchcore.converter

import org.springframework.core.convert.converter.Converter
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter
import java.time.format.DateTimeParseException

class LocalDateTimeCustomConverter: Converter<String, LocalDateTime> {

    override fun convert(source: String): LocalDateTime? {
        return tryParse(source)
    }

    private fun tryParse(source: String): LocalDateTime? {
        val patterns = listOf(
                "MM-dd-yyyy'T'HH:mm:ss.SSS","MM-dd-yyyy HH:mm:ss", "MM/dd/yyyy'T'HH:mm:ss.SSS", "MM/dd/yyyy HH:mm:ss",
                "yyyy-MM-dd'T'HH:mm:ss.SSS", "yyyy-MM-dd HH:mm:ss", "yyyy/MM/dd'T'HH:mm:ss.SSS", "yyyy/MM/dd HH:mm:ss")
        var result : LocalDateTime? = null
        patterns.forEach {
            try {
                result = LocalDateTime.parse(source, DateTimeFormatter.ofPattern(it))
                return@forEach
            } catch (ex: DateTimeParseException) { /*continue trying*/ }
        }
        return result
    }
}