package `in`.koreatech.koin.domain.util.regex

class EmailUtil {
    // 이메일이 portal email과 같은지 체크하는 메서드, 1글자 이상 9글자 이하 특수 문자 '-' 만 허용
    fun isEmailValidate(email: String): Boolean {
        return RegexPatterns.schoolEmail.matches(email)
    }

    fun isBusinessEmailValidate(email: String): Boolean {
        return RegexPatterns.businessEmail.matches(email)
    }
}

fun String.isOwnerEmailValid(): Boolean {
    return this.matches(RegexPatterns.email)
}

fun String.isOwnerNotEmailValid() = !isOwnerEmailValid()
