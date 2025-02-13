package `in`.koreatech.koin.domain.usecase.user

import `in`.koreatech.koin.domain.model.user.User
import `in`.koreatech.koin.domain.repository.UserRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject

class GetLoggerUserIdUseCase @Inject constructor(
    private val userRepository: UserRepository
) {
    operator fun invoke(): Flow<String?> = userRepository.getUserInfoFlow().map { it.toUserId() }
}

/**
 * 로깅에 사용하는 user_id 로 변환하는 함수
 *
 * @return {학번 앞 7자리}_{익명 닉네임 숫자 13자리} 문자열
 */
private fun User.toUserId(): String? = when (this) {
    is User.Anonymous -> null
    is User.Student -> {
        val prefix = studentNumber?.substring(0..6) ?: "0000000"
        val postfix = anonymousNickname?.substring(3) ?: "0000000000000"

        "${prefix}-${postfix}"
    }
}