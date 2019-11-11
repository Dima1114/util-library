package venus.utillibrary.json

import com.fasterxml.jackson.core.JsonParser
import com.fasterxml.jackson.databind.DeserializationContext
import com.fasterxml.jackson.databind.JsonDeserializer
import com.fasterxml.jackson.databind.node.TextNode
import java.time.LocalDate
import java.time.format.DateTimeFormatter

class JsonLocalDateDeserializer : JsonDeserializer<LocalDate>() {
    companion object {
        private val FORMATTER = DateTimeFormatter.ofPattern("yyyy-MM-dd")
    }

    override fun deserialize(jp: JsonParser, ctxt: DeserializationContext): LocalDate {
        val oc = jp.codec
        val node = oc.readTree<TextNode>(jp)
        val dateString = node.textValue()
        return LocalDate.parse(dateString, FORMATTER)
    }
}
