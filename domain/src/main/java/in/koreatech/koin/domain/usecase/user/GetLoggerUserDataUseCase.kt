package `in`.koreatech.koin.domain.usecase.user

import `in`.koreatech.koin.domain.model.user.Gender
import `in`.koreatech.koin.domain.model.user.LoggerUserData
import `in`.koreatech.koin.domain.model.user.User
import `in`.koreatech.koin.domain.repository.UserRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject

class GetLoggerUserDataUseCase
    @Inject
    constructor(
        private val userRepository: UserRepository,
    ) {
        operator fun invoke(): Flow<LoggerUserData> =
            userRepository.getUserInfoFlow().map {
                it.toLoggerUserData() ?: LoggerUserData(
                    userId = "",
                    gender = "",
                    major = "",
                )
            }
    }

/**
 * 로깅에 사용하는 유저 데이터로 변환하는 함수
 *
 * @See LoggerUserData
 */
private fun User.toLoggerUserData(): LoggerUserData? =
    when (this) {
        is User.Anonymous -> null
        is User.Student -> {
            val id_prefix = studentNumber?.substring(0..5) ?: "anonymous"
            val id_postfix = id

            LoggerUserData(
                userId = "${id_prefix}_${id_postfix}",
                gender =
                    when (gender) {
                        is Gender.Man -> "0"
                        is Gender.Woman -> "1"
                        is Gender.Unknown -> ""
                    },
                major = major ?: "",
            )
        }
    }
