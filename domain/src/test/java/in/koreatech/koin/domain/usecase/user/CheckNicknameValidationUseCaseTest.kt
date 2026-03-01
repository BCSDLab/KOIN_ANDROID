package `in`.koreatech.koin.domain.usecase.user

import `in`.koreatech.koin.domain.error.user.FakeUserErrorHandler
import `in`.koreatech.koin.domain.repository.FakeUserRepository
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertFalse
import org.junit.Assert.assertNotNull
import org.junit.Assert.assertNull
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Test

class CheckNicknameValidationUseCaseTest {

    private lateinit var userRepository: FakeUserRepository
    private lateinit var userErrorHandler: FakeUserErrorHandler

    @Before
    fun setUp() {
        userRepository = FakeUserRepository()
        userErrorHandler = FakeUserErrorHandler()
    }

    @Test
    fun `사용 가능한 닉네임이면 false와 null 에러를 반환한다`() = runTest {
        userRepository.setNicknameDuplicated(false)

        val result = CheckNicknameValidationUseCase(userRepository, userErrorHandler)("코인유저")

        assertFalse(result.first!!)
        assertNull(result.second)
    }

    @Test
    fun `이미 사용 중인 닉네임이면 true와 null 에러를 반환한다`() = runTest {
        userRepository.setNicknameDuplicated(true)

        val result = CheckNicknameValidationUseCase(userRepository, userErrorHandler)("코인유저")

        assertTrue(result.first!!)
        assertNull(result.second)
    }

    @Test
    fun `영어 닉네임은 유효하다`() = runTest {
        userRepository.setNicknameDuplicated(false)

        val result = CheckNicknameValidationUseCase(userRepository, userErrorHandler)("KoinUser")

        assertFalse(result.first!!)
        assertNull(result.second)
    }

    @Test
    fun `숫자 닉네임은 유효하다`() = runTest {
        userRepository.setNicknameDuplicated(false)

        val result = CheckNicknameValidationUseCase(userRepository, userErrorHandler)("123456")

        assertFalse(result.first!!)
        assertNull(result.second)
    }

    @Test
    fun `영어와 숫자 혼합 닉네임은 유효하다`() = runTest {
        userRepository.setNicknameDuplicated(false)

        val result = CheckNicknameValidationUseCase(userRepository, userErrorHandler)("Koin2024")

        assertFalse(result.first!!)
        assertNull(result.second)
    }

    @Test
    fun `닉네임에 공백이 포함되면 null과 ErrorHandler를 반환한다`() = runTest {
        val result = CheckNicknameValidationUseCase(userRepository, userErrorHandler)("코인 유저")

        assertNull(result.first)
        assertNotNull(result.second)
    }

    @Test
    fun `닉네임에 특수문자가 포함되면 null과 ErrorHandler를 반환한다`() = runTest {
        val result = CheckNicknameValidationUseCase(userRepository, userErrorHandler)("코인@유저")

        assertNull(result.first)
        assertNotNull(result.second)
    }

    @Test
    fun `빈 문자열 닉네임이면 null과 ErrorHandler를 반환한다`() = runTest {
        val result = CheckNicknameValidationUseCase(userRepository, userErrorHandler)("")

        assertNull(result.first)
        assertNotNull(result.second)
    }
}
