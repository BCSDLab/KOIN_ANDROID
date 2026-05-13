package `in`.koreatech.koin.feature.lostandfound.ui.keyword

import androidx.lifecycle.ViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import `in`.koreatech.koin.domain.model.notification.SubscribesType
import `in`.koreatech.koin.domain.usecase.article.lostandfound.DeleteLostItemKeywordUseCase
import `in`.koreatech.koin.domain.usecase.article.lostandfound.FetchLostItemKeywordSuggestionsUseCase
import `in`.koreatech.koin.domain.usecase.article.lostandfound.FetchLostItemKeywordUseCase
import `in`.koreatech.koin.domain.usecase.article.lostandfound.SaveLostItemKeywordUseCase
import `in`.koreatech.koin.domain.usecase.notification.DeleteNotificationSubscriptionUseCase
import `in`.koreatech.koin.domain.usecase.notification.GetNotificationPermissionInfoUseCase
import `in`.koreatech.koin.domain.usecase.notification.UpdateNotificationSubscriptionUseCase
import `in`.koreatech.koin.domain.usecase.user.GetUserStatusUseCase
import `in`.koreatech.koin.feature.lostandfound.R
import javax.inject.Inject
import kotlinx.collections.immutable.persistentListOf
import kotlinx.collections.immutable.toPersistentList
import kotlinx.coroutines.flow.catch
import org.orbitmvi.orbit.ContainerHost
import org.orbitmvi.orbit.syntax.simple.blockingIntent
import org.orbitmvi.orbit.syntax.simple.intent
import org.orbitmvi.orbit.syntax.simple.postSideEffect
import org.orbitmvi.orbit.syntax.simple.reduce
import org.orbitmvi.orbit.syntax.simple.repeatOnSubscription
import org.orbitmvi.orbit.viewmodel.container
import timber.log.Timber

@Suppress("LongParameterList", "TooManyFunctions")
@HiltViewModel
class LostAndFoundKeywordViewModel @Inject constructor(
    private val fetchLostItemKeywordUseCase: FetchLostItemKeywordUseCase,
    private val saveLostItemKeywordUseCase: SaveLostItemKeywordUseCase,
    private val deleteLostItemKeywordUseCase: DeleteLostItemKeywordUseCase,
    private val fetchLostItemKeywordSuggestionsUseCase: FetchLostItemKeywordSuggestionsUseCase,
    private val updateNotificationSubscriptionUseCase: UpdateNotificationSubscriptionUseCase,
    private val deleteNotificationSubscriptionUseCase: DeleteNotificationSubscriptionUseCase,
    private val getNotificationPermissionInfoUseCase: GetNotificationPermissionInfoUseCase,
    private val getUserStatusUseCase: GetUserStatusUseCase
) : ViewModel(), ContainerHost<LostAndFoundKeywordState, LostAndFoundKeywordSideEffect> {

    override val container = container<LostAndFoundKeywordState, LostAndFoundKeywordSideEffect>(
        LostAndFoundKeywordState()
    )

    init {
        observeUserStatus()
        observeKeywords()
        fetchSuggestedKeywords()
        loadNotificationState()
    }

    private fun observeUserStatus() = intent {
        repeatOnSubscription {
            getUserStatusUseCase()
                .catch { reduce { state.copy(isLoggedIn = false) } }
                .collect { user ->
                    reduce { state.copy(isLoggedIn = !user.isAnonymous) }
                }
        }
    }

    private fun observeKeywords() = intent {
        repeatOnSubscription {
            fetchLostItemKeywordUseCase()
                .catch { reduce { state.copy(keywords = persistentListOf()) } }
                .collect { keywords ->
                    reduce { state.copy(keywords = keywords.toPersistentList()) }
                }
        }
    }

    private fun fetchSuggestedKeywords() = intent {
        fetchLostItemKeywordSuggestionsUseCase()
            .catch { Timber.e(it, "추천 키워드 로드 실패") }
            .collect { suggestions ->
                reduce { state.copy(suggestedKeywords = suggestions.toPersistentList()) }
            }
    }

    private fun loadNotificationState() = intent {
        val (info, error) = getNotificationPermissionInfoUseCase()
        if (error != null) {
            Timber.e("키워드 알림 설정 로드 실패: %s", error.message)
            return@intent
        }
        if (info != null) {
            val isEnabled = info.subscribes
                .firstOrNull { it.type == SubscribesType.LOST_ITEM_KEYWORD }
                ?.isPermit ?: false
            reduce { state.copy(isNotificationEnabled = isEnabled) }
        }
    }

    fun onKeywordInputChanged(input: String) {
        blockingIntent {
            reduce { state.copy(keywordInput = input) }
        }
    }

    fun addKeyword(keyword: String) {
        intent {
            if (!state.isLoggedIn) {
                reduce { state.copy(showLoginDialog = true) }
                return@intent
            }
            if (state.isLoading) return@intent

            val validationError = validateKeyword(keyword)
            if (validationError != null) {
                postSideEffect(LostAndFoundKeywordSideEffect.ShowSnackbar(validationError))
                return@intent
            }
            val addError = checkAddability(state, keyword)
            if (addError != null) {
                postSideEffect(LostAndFoundKeywordSideEffect.ShowSnackbar(addError))
                return@intent
            }
            reduce { state.copy(isLoading = true) }
            saveLostItemKeywordUseCase(keyword)
                .catch {
                    reduce { state.copy(isLoading = false) }
                    postSideEffect(LostAndFoundKeywordSideEffect.ShowSnackbar(R.string.keyword_add_failed))
                }
                .collect {
                    reduce { state.copy(isLoading = false, keywordInput = "") }
                }
        }
    }

    fun deleteKeyword(keyword: String) {
        intent {
            if (state.isLoading) return@intent

            reduce { state.copy(isLoading = true) }
            deleteLostItemKeywordUseCase(keyword)
                .catch {
                    reduce { state.copy(isLoading = false) }
                    postSideEffect(LostAndFoundKeywordSideEffect.ShowSnackbar(R.string.keyword_delete_failed))
                }
                .collect {
                    reduce { state.copy(isLoading = false) }
                }
        }
    }

    fun addSuggestedKeyword(keyword: String) {
        addKeyword(keyword)
    }

    fun toggleNotification(isEnabled: Boolean) {
        intent {
            if (!state.isLoggedIn) {
                reduce { state.copy(showLoginDialog = true) }
                return@intent
            }
            if (state.isLoading) return@intent

            reduce { state.copy(isLoading = true, isNotificationEnabled = isEnabled) }
            val (_, error) = if (isEnabled) {
                updateNotificationSubscriptionUseCase(SubscribesType.LOST_ITEM_KEYWORD)
            } else {
                deleteNotificationSubscriptionUseCase(SubscribesType.LOST_ITEM_KEYWORD)
            }
            if (error != null) {
                reduce { state.copy(isLoading = false, isNotificationEnabled = !isEnabled) }
                postSideEffect(LostAndFoundKeywordSideEffect.ShowSnackbar(R.string.notification_update_failed))
            } else {
                reduce { state.copy(isLoading = false) }
            }
        }
    }

    fun dismissLoginDialog() = intent {
        reduce { state.copy(showLoginDialog = false) }
    }

    private fun validateKeyword(keyword: String): Int? = when {
        keyword.isEmpty() -> R.string.keyword_add_require_input
        WHITESPACE_REGEX.containsMatchIn(keyword) -> R.string.keyword_add_blank_not_allowed
        keyword.length < MIN_KEYWORD_LENGTH || keyword.length > MAX_KEYWORD_LENGTH ->
            R.string.keyword_add_invalid_length
        else -> null
    }

    private fun checkAddability(currentState: LostAndFoundKeywordState, keyword: String): Int? = when {
        currentState.keywords.contains(keyword) -> R.string.keyword_add_already_exist
        currentState.keywords.size >= MAX_KEYWORD_COUNT -> R.string.keyword_add_limit_exceeded
        else -> null
    }

    companion object {
        private const val MAX_KEYWORD_COUNT = 10
        private const val MAX_KEYWORD_LENGTH = 20
        private const val MIN_KEYWORD_LENGTH = 2
        private val WHITESPACE_REGEX = Regex("""\s""")
    }
}
