package `in`.koreatech.koin.feature.profile

import androidx.lifecycle.ViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import `in`.koreatech.koin.domain.model.user.User
import `in`.koreatech.koin.domain.usecase.notification.GetNotificationsFlowUseCase
import `in`.koreatech.koin.domain.usecase.timetable.GetLocalTimetableLecturesUseCase
import `in`.koreatech.koin.domain.usecase.timetable.GetTimetableFramesUseCase
import `in`.koreatech.koin.domain.usecase.timetable.GetTimetableLecturesUseCase
import `in`.koreatech.koin.domain.usecase.timetable.GetUserSemestersUseCase
import `in`.koreatech.koin.domain.usecase.user.GetUserStatusUseCase
import `in`.koreatech.koin.domain.usecase.user.UserLogoutUseCase
import `in`.koreatech.koin.feature.profile.mapper.toProfileTimetableLectures
import javax.inject.Inject
import kotlin.math.pow
import kotlinx.collections.immutable.persistentListOf
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.firstOrNull
import kotlinx.coroutines.flow.retryWhen
import org.orbitmvi.orbit.ContainerHost
import org.orbitmvi.orbit.annotation.OrbitExperimental
import org.orbitmvi.orbit.syntax.simple.intent
import org.orbitmvi.orbit.syntax.simple.postSideEffect
import org.orbitmvi.orbit.syntax.simple.reduce
import org.orbitmvi.orbit.syntax.simple.subIntent
import org.orbitmvi.orbit.viewmodel.container
import timber.log.Timber

@HiltViewModel
class ProfileViewModel @Inject constructor(
    private val getUserStatusUseCase: GetUserStatusUseCase,
    private val userLogoutUseCase: UserLogoutUseCase,
    private val getUserSemestersUseCase: GetUserSemestersUseCase,
    private val getTimetableFramesUseCase: GetTimetableFramesUseCase,
    private val getTimetableLecturesUseCase: GetTimetableLecturesUseCase,
    private val getLocalTimetableLecturesUseCase: GetLocalTimetableLecturesUseCase,
    private val getNotificationsFlowUseCase: GetNotificationsFlowUseCase
) : ViewModel(), ContainerHost<ProfileState, ProfileSideEffect> {

    override val container = container<ProfileState, ProfileSideEffect>(ProfileState())

    init {
        observeUserStatus()
        observeNotifications()
    }

    private fun observeUserStatus() = intent {
        getUserStatusUseCase()
            .retryWhen { cause, attempt ->
                Timber.e(cause)
                val backoffMs = (INITIAL_RETRY_DELAY_MS * 2.0.pow(attempt.toDouble()))
                    .toLong()
                    .coerceAtMost(MAX_RETRY_DELAY_MS)
                delay(backoffMs)
                true
            }
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
                        loadTimetable(isAnonymous = false)
                    }

                    is User.General -> {
                        reduce {
                            state.copy(
                                isLoggedIn = true,
                                name = user.name,
                                studentNumber = ""
                            )
                        }
                        loadTimetable(isAnonymous = false)
                    }

                    User.Anonymous -> {
                        reduce {
                            state.copy(
                                isLoggedIn = false,
                                name = "",
                                studentNumber = ""
                            )
                        }
                        loadTimetable(isAnonymous = true)
                    }
                }
            }
    }

    private fun observeNotifications() = intent {
        getNotificationsFlowUseCase()
            .catch { Timber.e(it) }
            .collect { notifications ->
                reduce { state.copy(isNewNotificationReceived = notifications.any { !it.isRead }) }
            }
    }

    fun refreshTimetable() = intent {
        loadTimetable(isAnonymous = !state.isLoggedIn)
    }

    @OptIn(OrbitExperimental::class)
    private suspend fun loadTimetable(isAnonymous: Boolean) = subIntent {
        val semesters = getUserSemestersUseCase(isAnonymous)
            .catch { Timber.e(it) }
            .firstOrNull()
            .orEmpty()
        val semester = semesters.firstOrNull() ?: run {
            reduce { state.copy(timetable = persistentListOf()) }
            return@subIntent
        }

        val result = if (isAnonymous) {
            getLocalTimetableLecturesUseCase(semester)
        } else {
            val frames = getTimetableFramesUseCase(semester)
                .catch { Timber.e(it) }
                .firstOrNull()
                .orEmpty()
            val mainFrame = frames.find { it.isMain } ?: run {
                reduce { state.copy(timetable = persistentListOf()) }
                return@subIntent
            }
            getTimetableLecturesUseCase(mainFrame.id)
        }

        result
            .onSuccess { timetableLectures ->
                reduce { state.copy(timetable = timetableLectures.toProfileTimetableLectures()) }
            }
            .onFailure {
                Timber.e(it)
                reduce { state.copy(timetable = persistentListOf()) }
            }
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

    companion object {
        private const val INITIAL_RETRY_DELAY_MS = 1000L
        private const val MAX_RETRY_DELAY_MS = 30_000L
    }
}
