package `in`.koreatech.koin.domain.util

object StoreUtil {
    fun generateOpenCloseTimeString(openTime: String, closeTime: String): String {
        val stringBuilder = StringBuilder()
        stringBuilder.append(openTime)
        stringBuilder.append("~")
        stringBuilder.append(closeTime)
        return stringBuilder.toString()
    }
}