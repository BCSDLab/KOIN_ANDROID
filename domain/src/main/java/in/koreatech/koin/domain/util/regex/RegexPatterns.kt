package `in`.koreatech.koin.domain.util.regex

object RegexPatterns {

    val phone = """(010)-?([0-9]{4})-?([0-9]{4})""".toRegex()

    val phoneExact = """^(010)-?([0-9]{4})-?([0-9]{4})${'$'}""".toRegex()

    val businessNumber = """([0-9]{3})([0-9]{2})([0-9]{5})""".toRegex()

    val email = """^[a-zA-Z0-9+_.-]+@[a-zA-Z0-9-]+\.[a-zA-Z0-9-.]+${'$'}""".toRegex()

    val schoolEmail = """^[a-zA-Z0-9._%+-]+@koreatech\.ac\.kr${'$'}""".toRegex()

    val businessEmail = """^[a-zA-Z0-9._%+-]+@[a-zA-Z0-9.-]+\.[a-zA-Z]{2,}${'$'}""".toRegex()

    val space = """\s""".toRegex()

    val url = """^https?://.*""".toRegex()

    val loginId = """^[a-z0-9_.-]+${'$'}""".toRegex()

    val instagramUrl = """^https?://(www\.|l\.)?instagram\.com/[a-zA-Z0-9].*""".toRegex()

    val googleFormUrl = """^https?://(docs\.google\.com/forms|forms\.gle)/[a-zA-Z0-9].*""".toRegex()

    val openChatUrl = """^https?://open\.kakao\.com/[a-zA-Z0-9].*""".toRegex()

    val name = """^[ㄱ-ㅎ가-힣a-zA-Z0-9]+${'$'}""".toRegex()

    val nickname = """^[ㄱ-ㅎ가-힣a-zA-Z0-9]+${'$'}""".toRegex()

    val korean = """[ㄱ-ㅎㅏ-ㅣ가-힣]""".toRegex()
}
