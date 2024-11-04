package `in`.koreatech.koin.domain.usecase.user

import `in`.koreatech.koin.domain.error.dept.DeptErrorHandler
import `in`.koreatech.koin.domain.error.user.UserErrorHandler
import `in`.koreatech.koin.domain.model.error.ErrorHandler
import `in`.koreatech.koin.domain.model.user.User
import `in`.koreatech.koin.domain.repository.DeptRepository
import `in`.koreatech.koin.domain.repository.TokenRepository
import `in`.koreatech.koin.domain.repository.UserRepository
import `in`.koreatech.koin.domain.util.deptCode
import javax.inject.Inject

class GetUserInfoUseCase @Inject constructor(
    private val userRepository: UserRepository,
    private val deptRepository: DeptRepository,
    private val tokenRepository: TokenRepository,
    private val userErrorHandler: UserErrorHandler,
    private val deptErrorHandler: DeptErrorHandler
) {
    suspend operator fun invoke(): Pair<User?, ErrorHandler?> {
        return if (tokenRepository.getAccessToken() == null) {
            //익명
            User.Anonymous to null
        } else {
            try {
                val user = userRepository.getUserInfo()

                if (user is User.Student && user.studentNumber != null && user.major == null) {
                    return try {
                        val deptName =
                            deptRepository.getDeptNameFromDeptCode(user.studentNumber.deptCode)

                        user.copy(major = deptName) to null
                    } catch (t: Throwable) {
                        user to deptErrorHandler.getDeptNameFromDeptCodeError(t)
                    }
                }
            } catch (t: Throwable) {
                null to userErrorHandler.handleGetUserInfoError(t)
            }

            return try {
                userRepository.getUserInfo() to null
            } catch (e: Exception) {
                null to userErrorHandler.handleGetUserInfoError(e)
            }
        }
    }
}