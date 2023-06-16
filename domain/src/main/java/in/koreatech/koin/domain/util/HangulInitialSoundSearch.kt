package `in`.koreatech.koin.domain.util


object HangulInitialSoundSearch {
    private val initialSounds = arrayOf(
        'ㄱ',
        'ㄲ',
        'ㄴ',
        'ㄷ',
        'ㄸ',
        'ㄹ',
        'ㅁ',
        'ㅂ',
        'ㅃ',
        'ㅅ',
        'ㅆ',
        'ㅇ',
        'ㅈ',
        'ㅉ',
        'ㅊ',
        'ㅋ',
        'ㅌ',
        'ㅍ',
        'ㅎ'
    )
    private const val HANGUL_BEGIN_UNICODE = 44032 // 가
    private const val HANGUL_LAST_UNICODE = 55203 // 힣
    private const val HANGUL_BASE_UNIT = 588 //가~깋...하~힣

    private fun isInitialSound(char: Char): Boolean = char in initialSounds

    private fun getInitialSound(char: Char): Char =
        initialSounds[(char.code - HANGUL_BEGIN_UNICODE) / HANGUL_BASE_UNIT]

    private fun isHangul(char: Char) = char.code in HANGUL_BEGIN_UNICODE..HANGUL_LAST_UNICODE

    private fun getInitialSounds(charSequence: CharSequence): CharSequence {
        return charSequence.map { c ->
            if (isHangul(c)) getInitialSound(c) else c
        }.joinToString("")
    }

    /**
     * 초성검색을 지원하는 문자열 검색에 활용하는 매칭 함수
     *
     * @param value 검색 대상 문자열
     * @param search 검색할 문자열
     */
    fun match(value: String, search: String): Boolean {
        val trimValue = value.replace(Regex("""\s"""), "")
        val trimSearch = search.replace(Regex("""\s"""), "")

        var t = 0
        val seof = trimValue.length - trimSearch.length
        val slen = trimSearch.length

        if (seof < 0) return false

        for (i in 0..seof) {
            t = 0
            while (t < slen) {
                if (isInitialSound(trimSearch[t]) && isHangul(trimValue[i + t])) {
                    if (getInitialSound(trimValue[i + t]) == trimSearch[t]) t++
                    else break
                } else {
                    if (trimValue[i + t] == trimSearch[t]) t++
                    else break
                }
            }
            return t == slen
        }
        return false
    }
}

fun String.match(search: String) = HangulInitialSoundSearch.match(this, search)