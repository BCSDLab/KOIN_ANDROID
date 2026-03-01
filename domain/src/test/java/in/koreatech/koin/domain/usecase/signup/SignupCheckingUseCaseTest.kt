package `in`.koreatech.koin.domain.usecase.signup

import `in`.koreatech.koin.domain.state.signup.SignupContinuationState
import org.junit.Assert.assertEquals
import org.junit.Test

class SignupCheckingUseCaseTest {

    private val signupCheckingUseCase = SignupCheckingUseCase()

    // koreatech.ac.kr 학교 이메일, 영문+숫자+특수문자 포함 유효한 비밀번호
    private val validEmail = "student@koreatech.ac.kr"
    private val validPassword = "Pass1!valid"

    @Test
    fun `이메일 형식이 올바르지 않으면 EmailInvalid를 반환한다`() {
        val result = signupCheckingUseCase(
            portalAccount = "invalid-email",
            password = validPassword,
            passwordConfirm = validPassword,
            isAgreedPrivacyTerms = true,
            isAgreedKoinTerms = true
        )
        assertEquals(SignupContinuationState.EmailInvalid, result)
    }

    @Test
    fun `koreatech 학교 이메일이 아니면 EmailInvalid를 반환한다`() {
        val result = signupCheckingUseCase(
            portalAccount = "student@gmail.com",
            password = validPassword,
            passwordConfirm = validPassword,
            isAgreedPrivacyTerms = true,
            isAgreedKoinTerms = true
        )
        assertEquals(SignupContinuationState.EmailInvalid, result)
    }

    @Test
    fun `이메일에 공백이 포함되면 EmailInvalid를 반환한다`() {
        val result = signupCheckingUseCase(
            portalAccount = "student @koreatech.ac.kr",
            password = validPassword,
            passwordConfirm = validPassword,
            isAgreedPrivacyTerms = true,
            isAgreedKoinTerms = true
        )
        assertEquals(SignupContinuationState.EmailInvalid, result)
    }

    @Test
    fun `비밀번호 형식이 올바르지 않으면 PasswordInvalid를 반환한다`() {
        val result = signupCheckingUseCase(
            portalAccount = validEmail,
            password = "password",
            passwordConfirm = "password",
            isAgreedPrivacyTerms = true,
            isAgreedKoinTerms = true
        )
        assertEquals(SignupContinuationState.PasswordInvalid, result)
    }

    @Test
    fun `비밀번호가 6자 미만이면 PasswordInvalid를 반환한다`() {
        val result = signupCheckingUseCase(
            portalAccount = validEmail,
            password = "P1!ab",
            passwordConfirm = "P1!ab",
            isAgreedPrivacyTerms = true,
            isAgreedKoinTerms = true
        )
        assertEquals(SignupContinuationState.PasswordInvalid, result)
    }

    @Test
    fun `비밀번호에 공백이 포함되면 PasswordInvalid를 반환한다`() {
        val result = signupCheckingUseCase(
            portalAccount = validEmail,
            password = "Pass 1!valid",
            passwordConfirm = "Pass 1!valid",
            isAgreedPrivacyTerms = true,
            isAgreedKoinTerms = true
        )
        assertEquals(SignupContinuationState.PasswordInvalid, result)
    }

    @Test
    fun `비밀번호 확인이 일치하지 않으면 PasswordNotMatching을 반환한다`() {
        val result = signupCheckingUseCase(
            portalAccount = validEmail,
            password = validPassword,
            passwordConfirm = "${validPassword}different",
            isAgreedPrivacyTerms = true,
            isAgreedKoinTerms = true
        )
        assertEquals(SignupContinuationState.PasswordNotMatching, result)
    }

    @Test
    fun `개인정보 약관에 동의하지 않으면 PrivacyTermsNotAgreed를 반환한다`() {
        val result = signupCheckingUseCase(
            portalAccount = validEmail,
            password = validPassword,
            passwordConfirm = validPassword,
            isAgreedPrivacyTerms = false,
            isAgreedKoinTerms = true
        )
        assertEquals(SignupContinuationState.PrivacyTermsNotAgreed, result)
    }

    @Test
    fun `코인 서비스 약관에 동의하지 않으면 KoinTermsNotAgreed를 반환한다`() {
        val result = signupCheckingUseCase(
            portalAccount = validEmail,
            password = validPassword,
            passwordConfirm = validPassword,
            isAgreedPrivacyTerms = true,
            isAgreedKoinTerms = false
        )
        assertEquals(SignupContinuationState.KoinTermsNotAgreed, result)
    }

    @Test
    fun `모든 조건이 유효하면 SignupCheckComplete를 반환한다`() {
        val result = signupCheckingUseCase(
            portalAccount = validEmail,
            password = validPassword,
            passwordConfirm = validPassword,
            isAgreedPrivacyTerms = true,
            isAgreedKoinTerms = true
        )
        assertEquals(SignupContinuationState.SignupCheckComplete, result)
    }

    @Test
    fun `이메일 검사가 비밀번호 검사보다 우선한다`() {
        val result = signupCheckingUseCase(
            portalAccount = "invalid-email",
            password = "password",
            passwordConfirm = "password",
            isAgreedPrivacyTerms = false,
            isAgreedKoinTerms = false
        )
        assertEquals(SignupContinuationState.EmailInvalid, result)
    }

    @Test
    fun `비밀번호 검사가 비밀번호 일치 검사보다 우선한다`() {
        val result = signupCheckingUseCase(
            portalAccount = validEmail,
            password = "password",
            passwordConfirm = "different",
            isAgreedPrivacyTerms = true,
            isAgreedKoinTerms = true
        )
        assertEquals(SignupContinuationState.PasswordInvalid, result)
    }
}
