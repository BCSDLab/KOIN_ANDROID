package `in`.koreatech.koin.util.ext

fun String.splitPhoneNumber(): Triple<String, String, String> {
    return this.split("-").let {
        Triple(it[0], it[1], it[2])
    }
}