package `in`.koreatech.koin.data.gson

import com.google.gson.Gson
import com.google.gson.TypeAdapter
import com.google.gson.TypeAdapterFactory
import com.google.gson.reflect.TypeToken
import com.google.gson.stream.JsonReader
import com.google.gson.stream.JsonWriter

class SerializeNullsTypeAdapterFactory : TypeAdapterFactory {
    override fun <T> create(gson: Gson, type: TypeToken<T>): TypeAdapter<T> {
        val delegate = gson.getDelegateAdapter(this, type)
        return object : TypeAdapter<T>() {
            override fun write(out: JsonWriter, value: T?) {
                val wasSerializeNulls = out.serializeNulls
                out.serializeNulls = true
                try {
                    delegate.write(out, value)
                } finally {
                    out.serializeNulls = wasSerializeNulls
                }
            }

            override fun read(input: JsonReader): T = delegate.read(input)
        }
    }
}
