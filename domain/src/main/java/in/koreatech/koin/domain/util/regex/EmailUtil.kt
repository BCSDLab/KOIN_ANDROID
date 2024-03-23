package `in`.koreatech.koin.domain.util.regex

import java.util.regex.Pattern

class EmailUtil {

    // 이메일이 portal email과 같은지 체크하는 메서드, 1글자 이상 9글자 이하 특수 문자 '-' 만 허용
    fun isEmailValidate(email: String): Boolean {
        return EMAIL_REGEX.matcher(email).matches()
    }

    companion object {
        private const val FILTER_EMAIL = "^[a-zA-Z0-9._%+-]+@koreatech\\.ac\\.kr\$"
        val EMAIL_REGEX: Pattern = Pattern.compile(FILTER_EMAIL)
    }
}