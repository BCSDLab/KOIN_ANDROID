package `in`.koreatech.koin.feature.home.profile

import androidx.lifecycle.ViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import `in`.koreatech.koin.domain.model.user.User
import `in`.koreatech.koin.domain.repository.TimetableRepository
import `in`.koreatech.koin.domain.usecase.timetable.GetTimetableFramesUseCase
import `in`.koreatech.koin.domain.usecase.timetable.GetUserSemestersUseCase
import `in`.koreatech.koin.domain.usecase.user.GetUserStatusUseCase
import `in`.koreatech.koin.domain.usecase.user.UserLogoutUseCase
import `in`.koreatech.koin.feature.home.profile.mapper.toProfileTimetableLectures
import javax.inject.Inject
import kotlinx.collections.immutable.persistentListOf
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.firstOrNull
import org.orbitmvi.orbit.ContainerHost
import org.orbitmvi.orbit.syntax.simple.intent
import org.orbitmvi.orbit.syntax.simple.postSideEffect
import org.orbitmvi.orbit.syntax.simple.reduce
import org.orbitmvi.orbit.viewmodel.container
import timber.log.Timber

@HiltViewModel
class ProfileViewModel @Inject constructor(
    private val getUserStatusUseCase: GetUserStatusUseCase,
    private val userLogoutUseCase: UserLogoutUseCase,
    private val getUserSemestersUseCase: GetUserSemestersUseCase,
    private val getTimetableFramesUseCase: GetTimetableFramesUseCase,
    private val timetableRepository: TimetableRepository
) : ViewModel(), ContainerHost<ProfileState, ProfileSideEffect> {

    override val container = container<ProfileState, ProfileSideEffect>(ProfileState())

    init {
        observeUserStatus()
    }

    private fun observeUserStatus() = intent {
        getUserStatusUseCase()
            .catch { Timber.e(it) }
            .collectLatest { user ->
                when (user) {
                    is User.Student -> {
                        reduce {
                            state.copy(
                                isLoggedIn = true,
                                name = user.name.orEmpty(),
                                studentNumber = user.studentNumber.orEmpty()
                            )
                        }
                        loadTimetable()
                    }

                    is User.General -> {
                        reduce {
                            state.copy(
                                isLoggedIn = true,
                                name = user.name,
                                studentNumber = ""
                            )
                        }
                        loadTimetable()
                    }

                    User.Anonymous -> reduce {
                        state.copy(
                            isLoggedIn = false,
                            name = "",
                            studentNumber = "",
                            timetable = persistentListOf()
                        )
                    }
                }
            }
    }

    private fun loadTimetable() = intent {
        val semesters = getUserSemestersUseCase(false)
            .catch { Timber.e(it) }
            .firstOrNull()
            .orEmpty()
        val semester = semesters.firstOrNull() ?: return@intent

        val frames = getTimetableFramesUseCase(semester)
            .catch { Timber.e(it) }
            .firstOrNull()
            .orEmpty()
        val mainFrame = frames.find { it.isMain } ?: return@intent

        timetableRepository.getTimetableLectures(mainFrame.id)
            .onSuccess { timetableLectures ->
                reduce { state.copy(timetable = timetableLectures.toProfileTimetableLectures()) }
            }
            .onFailure { Timber.e(it) }
    }
    fun onLogoutClick() = intent {
        userLogoutUseCase()
            .onSuccess {
                postSideEffect(ProfileSideEffect.LogoutSuccess)
            }
            .onFailure {
                Timber.e(it)
            }
    }
}
