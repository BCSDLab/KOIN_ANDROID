package `in`.koreatech.koin.domain.usecase.user

import `in`.koreatech.koin.domain.model.user.AuthToken
import `in`.koreatech.koin.domain.model.user.UserType
import `in`.koreatech.koin.domain.repository.FakeTokenRepository
import `in`.koreatech.koin.domain.repository.FakeUserRepository
import kotlinx.coroutines.test.runTest
import org.junit.Assert
import org.junit.Before
import org.junit.Test

class UserLoginUseCaseTest {
    private lateinit var userRepository: FakeUserRepository
    private lateinit var tokenRepository: FakeTokenRepository

    @Before
    fun setUp() {
        userRepository = FakeUserRepository()
        tokenRepository = FakeTokenRepository()
    }

    @Test
    fun `학생 계정으로 로그인을 시도할 경우, 학생 정보를 가져온다`() = runTest {
        val accessToken = "test_access_token"
        val refreshToken = "test_refresh_token"
        userRepository.setAccessToken(AuthToken(accessToken, refreshToken, UserType.STUDENT.name))

        val userLoginUseCase = UserLoginUseCase(userRepository, tokenRepository)
        val email = "student@koreatech.ac.kr"
        val password = "credential"

        val result = userLoginUseCase(email, password)

        Assert.assertTrue(result.isSuccess)
        Assert.assertEquals(accessToken, tokenRepository.getAccessToken())
        Assert.assertEquals(refreshToken, tokenRepository.getRefreshToken())
        Assert.assertEquals(UserType.STUDENT, userRepository.userType)
    }

    @Test
    fun `일반인 계정으로 로그인을 시도할 경우, 일반인 정보를 가져온다`() = runTest {
        val accessToken = "test_access_token"
        val refreshToken = "test_refresh_token"
        userRepository.setAccessToken(AuthToken(accessToken, refreshToken, UserType.GENERAL.name))

        val userLoginUseCase = UserLoginUseCase(userRepository, tokenRepository)
        val email = "general@bcsdlab.com"
        val password = "credential"

        val result = userLoginUseCase(email, password)

        Assert.assertTrue(result.isSuccess)
        Assert.assertEquals(accessToken, tokenRepository.getAccessToken())
        Assert.assertEquals(refreshToken, tokenRepository.getRefreshToken())
        Assert.assertEquals(UserType.GENERAL, userRepository.userType)
    }

    @Test
    fun `로그인 실패 시 Result failure를 반환한다`() = runTest {
        userRepository.setAccessToken(null)

        val userLoginUseCase = UserLoginUseCase(userRepository, tokenRepository)
        val email = "general@bcsdlab.com"
        val password = "credential"

        val result = userLoginUseCase(email, password)

        Assert.assertTrue(result.isFailure)
    }
}
