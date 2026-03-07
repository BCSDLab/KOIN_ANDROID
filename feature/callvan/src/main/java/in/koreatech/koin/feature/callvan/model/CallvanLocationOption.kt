package `in`.koreatech.koin.feature.callvan.model

enum class CallvanLocationOption(
    val displayName: String,
    val type: String
) {
    FRONT_GATE("정문", "FRONT_GATE"),
    BACK_GATE("후문", "BACK_GATE"),
    CHEONAN_TERMINAL("천안터미널", "CHEONAN_TERMINAL"),
    CHEONAN_STATION("천안역", "CHEONAN_STATION"),
    OSONG_STATION("오송역", "OSONG_STATION"),
    CHEONGJU_TERMINAL("청주터미널", "CHEONGJU_TERMINAL"),
    CHEONGJU_STATION("청주역", "CHEONGJU_STATION"),
    SEOUL_EXPRESS_TERMINAL("서울고속터미널", "SEOUL_EXPRESS_TERMINAL"),
    DONGSEOUL_TERMINAL("동서울터미널", "DONGSEOUL_TERMINAL"),
    OTHER("기타", "OTHER")
}
