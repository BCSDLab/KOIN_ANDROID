package `in`.koreatech.koin.feature.article.ui.article.keyword

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import `in`.koreatech.koin.core.viewmodel.BaseViewModel
import `in`.koreatech.koin.domain.model.notification.NotificationPermissionInfo
import `in`.koreatech.koin.domain.model.notification.SubscribesType
import `in`.koreatech.koin.domain.model.user.User
import `in`.koreatech.koin.domain.repository.ArticleRepository
import `in`.koreatech.koin.domain.usecase.notification.DeleteNotificationSubscriptionUseCase
import `in`.koreatech.koin.domain.usecase.notification.GetNotificationPermissionInfoUseCase
import `in`.koreatech.koin.domain.usecase.notification.UpdateNotificationSubscriptionUseCase
import `in`.koreatech.koin.domain.usecase.user.GetUserStatusUseCase
import `in`.koreatech.koin.domain.util.onFailure
import `in`.koreatech.koin.domain.util.onSuccess
import javax.inject.Inject
import kotlinx.coroutines.channels.BufferOverflow
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.onStart
import kotlinx.coroutines.flow.shareIn
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update

@HiltViewModel
class ArticleKeywordViewModel @Inject constructor(
    private val savedStateHandle: SavedStateHandle,
    private val articleRepository: ArticleRepository,
    private val getNotificationPermissionInfoUseCase: GetNotificationPermissionInfoUseCase,
    private val updateNotificationSubscriptionUseCase: UpdateNotificationSubscriptionUseCase,
    private val deleteNotificationSubscriptionUseCase: DeleteNotificationSubscriptionUseCase,
    getUserStatusUseCase: GetUserStatusUseCase
) : BaseViewModel() {
    val user: StateFlow<User> =
        getUserStatusUseCase()
            .stateIn(viewModelScope, SharingStarted.Eagerly, User.Anonymous)

    private val _notificationUiState =
        MutableStateFlow<NotificationUiState>(NotificationUiState.Nothing)
    val notificationUiState = _notificationUiState.asStateFlow()

    val keywordInputUiState: StateFlow<KeywordInputUiState> =
        savedStateHandle.getStateFlow(KEYWORD_INPUT, "").map {
            if (it.isEmpty()) KeywordInputUiState.Empty else KeywordInputUiState.Valid(it)
        }.stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5_000),
            initialValue = KeywordInputUiState.Empty
        )

    val myKeywords: StateFlow<List<String>> =
        articleRepository.fetchMyKeyword()
            .stateIn(
                scope = viewModelScope,
                started = SharingStarted.WhileSubscribed(5_000),
                initialValue = listOf()
            )

    private val _keywordAddUiState =
        MutableSharedFlow<KeywordAddUiState>(
            extraBufferCapacity = 1,
            onBufferOverflow = BufferOverflow.DROP_OLDEST
        )
    val keywordAddUiState: SharedFlow<KeywordAddUiState> =
        _keywordAddUiState
            .shareIn(
                scope = viewModelScope,
                started = SharingStarted.WhileSubscribed(5_000)
            )

    val suggestedKeywords: StateFlow<List<String>> =
        articleRepository.fetchKeywordSuggestions()
            .stateIn(
                scope = viewModelScope,
                started = SharingStarted.WhileSubscribed(5_000),
                initialValue = listOf()
            )

    fun addKeyword(keyword: String) {
        val trimmedKeyword =
            keyword.trim().ifEmpty {
                _keywordAddUiState.tryEmit(KeywordAddUiState.RequireInput)
                return
            }
        if (myKeywords.value.size >= MAX_KEYWORD_COUNT) {
            _keywordAddUiState.tryEmit(KeywordAddUiState.LimitExceeded)
            return
        }

        if (trimmedKeyword.length > MAX_KEYWORD_LENGTH || trimmedKeyword.length < MIN_KEYWORD_LENGTH) {
            _keywordAddUiState.tryEmit(KeywordAddUiState.InvalidLength)
            return
        }

        if (myKeywords.value.contains(trimmedKeyword)) {
            _keywordAddUiState.tryEmit(KeywordAddUiState.AlreadyExist)
            return
        }

        if (trimmedKeywordRegex.containsMatchIn(trimmedKeyword)) { // jusang-regex-opt
            _keywordAddUiState.tryEmit(KeywordAddUiState.BlankNotAllowed)
            return
        }

        articleRepository.saveKeyword(trimmedKeyword).onStart {
            _keywordAddUiState.emit(KeywordAddUiState.Loading)
        }.onEach {
            _keywordAddUiState.emit(KeywordAddUiState.Success(trimmedKeyword))
        }.catch {
            _keywordAddUiState.emit(KeywordAddUiState.Error)
        }.launchIn(viewModelScope)
    }

    fun getPermissionInfo() {
        viewModelScope.launchWithLoading {
            getNotificationPermissionInfoUseCase().onSuccess { info ->
                _notificationUiState.update {
                    NotificationUiState.Success(info)
                }
            }.onFailure {
                _notificationUiState.update { NotificationUiState.Failed }
            }
        }
    }

    fun updateSubscription(type: SubscribesType) {
        viewModelScope.launchWithLoading {
            updateNotificationSubscriptionUseCase(type)
        }
    }

    fun deleteSubscription(type: SubscribesType) {
        viewModelScope.launchWithLoading {
            deleteNotificationSubscriptionUseCase(type)
        }
    }

    fun deleteKeyword(keyword: String) {
        articleRepository.deleteKeyword(keyword).onEach {
            _keywordAddUiState.emit(KeywordAddUiState.Success(keyword))
        }.catch {
            _keywordAddUiState.emit(KeywordAddUiState.Error)
        }.launchIn(viewModelScope)
    }

    fun onKeywordInputChanged(keyword: String) {
        savedStateHandle[KEYWORD_INPUT] = keyword
    }

    companion object {
        const val MAX_KEYWORD_COUNT = 10
        const val MAX_SUGGEST_KEYWORD_COUNT = 5
        const val MAX_KEYWORD_LENGTH = 20
        const val MIN_KEYWORD_LENGTH = 2
        const val KEYWORD_INPUT = "keyword_input"
        val trimmedKeywordRegex = Regex("""\s+""")
    }
}

sealed interface KeywordAddUiState {
    data object Nothing : KeywordAddUiState

    data object Loading : KeywordAddUiState

    data class Success(val keyword: String) : KeywordAddUiState

    data object AlreadyExist : KeywordAddUiState

    data object LimitExceeded : KeywordAddUiState

    data object BlankNotAllowed : KeywordAddUiState

    data object InvalidLength : KeywordAddUiState

    data object RequireInput : KeywordAddUiState

    data object Error : KeywordAddUiState
}

sealed interface KeywordInputUiState {
    data object Empty : KeywordInputUiState

    data class Valid(val keyword: String) : KeywordInputUiState
}

sealed class NotificationUiState {
    data class Success(
        val notificationPermissionInfo: NotificationPermissionInfo
    ) : NotificationUiState()

    data object Failed : NotificationUiState()

    data object Nothing : NotificationUiState()
}
