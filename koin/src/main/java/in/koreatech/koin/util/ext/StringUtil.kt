package `in`.koreatech.koin.util.ext

fun String.splitPhoneNumber(): Triple<String, String, String> {
    return this.split("-").let {
        Triple(it[0], it[1], it[2])
    }
}

fun String.hasJongSungAtLastChar(): Boolean {
    if (this.isEmpty()) return false

    val lastChar = this.last()
    val hangulStart = 0xAC00
    val hangulEnd = 0xD7A3
    val jongSungCount = 28

    if (lastChar.code in hangulStart..hangulEnd) {
        val index = lastChar.code - hangulStart
        return index % jongSungCount > 0
    }
    return false
}