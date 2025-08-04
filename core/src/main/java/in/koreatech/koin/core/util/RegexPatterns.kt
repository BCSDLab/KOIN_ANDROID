package `in`.koreatech.koin.core.util

object RegexPatterns {

    val rgb = """rgb\(([0-9]+), ([0-9]+), ([0-9]+)\)""".toRegex()

    val fileSize = """[0-9]+\s?[KMG]?B""".toRegex()

    val phone = """(010)-?([0-9]{4})-?([0-9]{4})""".toRegex()
}
