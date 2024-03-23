package `in`.koreatech.koin.domain.util.regex

import java.security.MessageDigest
import java.security.NoSuchAlgorithmException
import java.util.regex.Pattern

class PasswordUtil {
    private fun hashString(message: String, algorithm: String): String {
        try {
            val digest = MessageDigest.getInstance(algorithm)
            digest.update(message.toByteArray())
            val hashedBytes = digest.digest()

            // Create Hex String
            val hexString = StringBuilder()
            for (hashedByte in hashedBytes) {
                val h = StringBuilder(Integer.toHexString(0xFF and hashedByte.toInt()))
                while (h.length < 2) h.insert(0, "0")
                hexString.append(h)
            }
            return hexString.toString()
        } catch (ex: NoSuchAlgorithmException) {
            ex.printStackTrace()
        }
        return ""
    }

    fun generateSHA256(message: String): String {
        return PasswordUtil().hashString(message, "SHA-256")
    }

    //비밀번호가 사용 가능한지 체크하는 메서드, 특수문자 1개 이상, 6~18
    fun isPasswordValidate(password: String): Boolean {
        return PASSWORD_REGEX.matcher(password).matches()
    }

    companion object {
        private const val FILTER_PASSWORD =
            "^(?=.*[a-zA-Z])(?=.*[`₩~!@#$%<>^&*()\\-=+_?<>:;\"',.{}|[]/\\\\]])(?=.*[0-9]).{6,18}$"
        val PASSWORD_REGEX: Pattern = Pattern.compile(FILTER_PASSWORD)
    }
}