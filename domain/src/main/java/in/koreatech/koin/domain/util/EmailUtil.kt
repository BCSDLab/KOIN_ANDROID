package `in`.koreatech.koin.domain.util

import java.util.regex.Pattern

object EmailUtil {
    private const val FILTER_EMAIL = "^[a-z_0-9]{1,12}$"

    // 이메일이 portal email과 같은지 체크하는 메서드, 1글자 이상 9글자 이하 특수 문자 '-' 만 허용
    fun isEmailValidate(email: String): Boolean {
        val matcher = Pattern.compile(FILTER_EMAIL).matcher(email)
        return matcher.matches()
    }

    fun String.isOwnerEmailValid(): Boolean {
        val emailRegex = "^[a-zA-Z0-9+-_.]+@[a-zA-Z0-9-]+\\.[a-zA-Z0-9-.]+$"
        return this.matches(emailRegex.toRegex())
    }

    fun String.isOwnerNotEmailValid() = !isOwnerEmailValid()
}