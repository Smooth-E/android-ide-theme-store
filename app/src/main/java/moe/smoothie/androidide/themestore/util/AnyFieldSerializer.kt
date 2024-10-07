package moe.smoothie.androidide.themestore.util

import kotlinx.serialization.KSerializer
import kotlinx.serialization.SerializationException
import kotlinx.serialization.descriptors.SerialDescriptor
import kotlinx.serialization.descriptors.buildClassSerialDescriptor
import kotlinx.serialization.encoding.Decoder
import kotlinx.serialization.encoding.Encoder
import kotlinx.serialization.json.JsonDecoder
import kotlinx.serialization.json.JsonPrimitive
import kotlinx.serialization.json.double
import kotlinx.serialization.json.doubleOrNull
import kotlinx.serialization.json.float
import kotlinx.serialization.json.floatOrNull
import kotlinx.serialization.json.int
import kotlinx.serialization.json.intOrNull

object AnyFieldSerializer : KSerializer<Any> {
    override val descriptor: SerialDescriptor = buildClassSerialDescriptor("AnyField")

    override fun serialize(encoder: Encoder, value: Any) {
        when (value) {
            is String -> encoder.encodeString(value)
            is Int -> encoder.encodeInt(value)
            else -> throw SerializationException("Unknown type")
        }
    }

    override fun deserialize(decoder: Decoder): Any {
        val input = decoder as? JsonDecoder ?: throw SerializationException("This can only be used with JSON")
        val element = input. decodeJsonElement()

        return when {
            element is JsonPrimitive && element.isString -> element.content
            element is JsonPrimitive && element.intOrNull != null -> element.int
            element is JsonPrimitive && element.floatOrNull != null -> element.float
            element is JsonPrimitive && element.doubleOrNull != null -> element.double
            else -> throw SerializationException("Unknown JSON format $element")
        }
    }
}
