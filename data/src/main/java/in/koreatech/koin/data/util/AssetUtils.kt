package `in`.koreatech.koin.data.util

import android.content.Context
import com.google.gson.Gson
import java.io.IOException

inline fun <reified T> Context.readData(assetName: String): T? {
    return try {
        val inputStream = this.resources.assets.open(assetName)
        val buffer = ByteArray(inputStream.available())
        inputStream.read(buffer)
        inputStream.close()

        val gson = Gson()
        gson.fromJson(String(buffer), T::class.java)
    } catch (e: IOException) {
        e.message
        null
    } catch (e: Exception) {
        e.message
        null
    }
}