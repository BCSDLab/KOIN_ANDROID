package `in`.koreatech.koin.data.util

import com.google.gson.JsonElement
import com.google.gson.JsonNull
import com.google.gson.JsonPrimitive
import com.google.gson.JsonSerializationContext
import com.google.gson.JsonSerializer
import java.lang.reflect.Type

class EmptyStringToNullAdapter : JsonSerializer<String> {
    override fun serialize(src: String?, typeOfSrc: Type?, context: JsonSerializationContext?): JsonElement {
        return if (src.isNullOrEmpty()) {
            JsonNull.INSTANCE
        } else {
            JsonPrimitive(src)
        }
    }
}
