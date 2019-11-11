package venus.utillibrary.json

import com.fasterxml.jackson.core.JsonParser
import com.fasterxml.jackson.databind.DeserializationContext
import com.fasterxml.jackson.databind.JsonDeserializer
import com.fasterxml.jackson.databind.node.TextNode
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter

class JsonLocalDateTimeDeserializer : JsonDeserializer<LocalDateTime>() {
    companion object {
        private val FORMATTER = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")
    }

    override fun deserialize(jp: JsonParser, ctxt: DeserializationContext): LocalDateTime {
        val oc = jp.codec
        val node = oc.readTree<TextNode>(jp)
        val dateString = node.textValue()
        return LocalDateTime.parse(dateString, FORMATTER)
    }
}
