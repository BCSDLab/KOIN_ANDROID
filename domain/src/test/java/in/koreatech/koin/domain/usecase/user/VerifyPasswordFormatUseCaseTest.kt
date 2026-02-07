package `in`.koreatech.koin.domain.usecase.user

import junit.framework.TestCase.assertFalse
import junit.framework.TestCase.assertTrue
import org.junit.Test

class VerifyPasswordFormatUseCaseTest {
    @Test
    fun `비밀번호에 영어가 포함되었는가`() {
        val password = "password"
        val result = VerifyPasswordFormatUseCase().invoke(password)
        assertTrue(result.isIncludeEnglish)
    }

    @Test
    fun `비밀번호에 숫자가 포함되었는가`() {
        val password = "password123"
        val result = VerifyPasswordFormatUseCase().invoke(password)
        assertTrue(result.isIncludeNumber)
    }

    @Test
    fun `비밀번호에 기호가 포함되었는가`() {
        val password = "password!"
        val result = VerifyPasswordFormatUseCase().invoke(password)
        assertTrue(result.isIncludeSymbol)
    }

    @Test
    fun `비밀번호 길이가 6에서 18자가 아닐 경우`() {
        val password = "pass"
        val result = VerifyPasswordFormatUseCase().invoke(password)
        assertFalse(result.isValidLength)
    }
}
