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
        if(tokenRepository.getAccessToken() == null) return null to null //익명

        try {
            val user = userRepository.getUserInfo()
            if (user.studentNumber != null && user.major == null) {
                return try {
                    val deptName =
                        deptRepository.getDeptNameFromDeptCode(user.studentNumber.deptCode)

                    user.copy(major = deptName) to null
                } catch (t: Throwable) {
                    user to deptErrorHandler.getDeptNameFromDeptCodeError(t)
                }
            }

            return userRepository.getUserInfo() to null
        } catch (t: Throwable) {
            return null to userErrorHandler.handleGetUserInfoError(t)
        }
    }
}