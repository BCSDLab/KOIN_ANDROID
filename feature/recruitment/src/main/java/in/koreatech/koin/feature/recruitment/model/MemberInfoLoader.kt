package `in`.koreatech.koin.feature.recruitment.model

import `in`.koreatech.koin.domain.error.recruitment.KoinRecruitmentException
import `in`.koreatech.koin.domain.model.recruitment.TeamRecruitmentProfile
import `in`.koreatech.koin.domain.model.user.User
import `in`.koreatech.koin.domain.usecase.recruitment.GetTeamRecruitmentProfileUseCase
import `in`.koreatech.koin.domain.usecase.user.GetUserInfoUseCase
import org.orbitmvi.orbit.syntax.simple.SimpleSyntax
import org.orbitmvi.orbit.syntax.simple.reduce

suspend fun <S : Any, SE : Any> SimpleSyntax<S, SE>.loadMemberInfoOrFallback(
    getTeamRecruitmentProfileUseCase: GetTeamRecruitmentProfileUseCase,
    getUserInfoUseCase: GetUserInfoUseCase,
    onProfileLoaded: S.(profile: TeamRecruitmentProfile) -> S,
    onUserLoaded: S.(user: User.Student) -> S,
    onError: S.(throwable: Throwable) -> S
) {
    getTeamRecruitmentProfileUseCase()
        .onSuccess { profile ->
            reduce { state.onProfileLoaded(profile) }
        }
        .onFailure { throwable ->
            if (throwable is KoinRecruitmentException.ProfileNotFoundException) {
                getUserInfoUseCase()
                    .onSuccess { user ->
                        if (user is User.Student) {
                            reduce { state.onUserLoaded(user) }
                        }
                    }
                    .onFailure { fallbackThrowable ->
                        reduce { state.onError(fallbackThrowable) }
                    }
            } else {
                reduce { state.onError(throwable) }
            }
        }
}
