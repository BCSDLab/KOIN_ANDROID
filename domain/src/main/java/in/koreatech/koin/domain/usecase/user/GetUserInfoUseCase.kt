package `in`.koreatech.koin.domain.usecase.user

import `in`.koreatech.koin.domain.model.user.User
import `in`.koreatech.koin.domain.repository.DeptRepository
import `in`.koreatech.koin.domain.repository.TokenRepository
import `in`.koreatech.koin.domain.repository.UserRepository
import `in`.koreatech.koin.domain.util.deptCode
import `in`.koreatech.koin.domain.util.suspendRunCatching
import javax.inject.Inject
import kotlin.Result

class GetUserInfoUseCase @Inject constructor(
    private val userRepository: UserRepository,
    private val deptRepository: DeptRepository,
    private val tokenRepository: TokenRepository
) {
    suspend operator fun invoke(): Result<User> {
        if (tokenRepository.getAccessToken() == null) {
            return Result.success(User.Anonymous)
        }
        return suspendRunCatching {
            val user = userRepository.getUserInfo()
            if (user is User.Student && user.studentNumber != null && user.major == null) {
                deptRepository.getDeptNameFromDeptCode(user.studentNumber.deptCode)
                    .map { deptName -> user.copy(major = deptName) }
                    .getOrDefault(user)
            } else {
                user
            }
        }
    }
}
