package `in`.koreatech.koin.feature.lostandfound.ui.keyword

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.navigation.toRoute
import dagger.hilt.android.lifecycle.HiltViewModel
import `in`.koreatech.koin.feature.lostandfound.R
import `in`.koreatech.koin.feature.lostandfound.navigation.LostAndFoundNavType
import javax.inject.Inject
import kotlinx.collections.immutable.PersistentList
import kotlinx.collections.immutable.persistentListOf
import kotlinx.collections.immutable.toPersistentList
import org.orbitmvi.orbit.Container
import org.orbitmvi.orbit.ContainerHost
import org.orbitmvi.orbit.syntax.simple.blockingIntent
import org.orbitmvi.orbit.syntax.simple.intent
import org.orbitmvi.orbit.syntax.simple.postSideEffect
import org.orbitmvi.orbit.syntax.simple.reduce
import org.orbitmvi.orbit.viewmodel.container

@HiltViewModel
class LostAndFoundKeywordViewModel @Inject constructor(
    savedStateHandle: SavedStateHandle
) :
    ViewModel(),
    ContainerHost<LostAndFoundKeywordState, LostAndFoundKeywordSideEffect> {
    override val container: Container<LostAndFoundKeywordState, LostAndFoundKeywordSideEffect> = container(
        initialState = LostAndFoundKeywordState(
            keywords = try {
                val route = savedStateHandle.toRoute<LostAndFoundNavType.LostAndFoundKeywordRoute>()
                route.initialKeywordsCsv
                    .split("\u0001")
                    .filter { it.isNotEmpty() }
                    .toPersistentList()
            } catch (@Suppress("SwallowedException") e: IllegalArgumentException) {
                persistentListOf()
            }
        )
    )

    fun onKeywordInputChanged(input: String) {
        blockingIntent {
            reduce { state.copy(keywordInput = input) }
        }
    }

    fun addKeyword(keyword: String) {
        intent {
            val validationError = validateKeyword(keyword)
            if (validationError != null) {
                postSideEffect(LostAndFoundKeywordSideEffect.ShowSnackbar(validationError))
                return@intent
            }

            val addError = checkAddability(state, keyword)
            if (addError != null) {
                postSideEffect(LostAndFoundKeywordSideEffect.ShowSnackbar(addError))
            } else {
                reduce {
                    state.copy(
                        keywords = state.keywords.add(keyword),
                        keywordInput = ""
                    )
                }
            }
        }
    }

    private fun validateKeyword(keyword: String): Int? = when {
        keyword.isEmpty() -> R.string.keyword_add_require_input
        WHITESPACE_REGEX.containsMatchIn(keyword) -> R.string.keyword_add_blank_not_allowed
        keyword.length < MIN_KEYWORD_LENGTH || keyword.length > MAX_KEYWORD_LENGTH -> R.string.keyword_add_invalid_length
        else -> null
    }

    private fun checkAddability(currentState: LostAndFoundKeywordState, keyword: String): Int? = when {
        currentState.keywords.contains(keyword) -> R.string.keyword_add_already_exist
        currentState.keywords.size >= MAX_KEYWORD_COUNT -> R.string.keyword_add_limit_exceeded
        else -> null
    }

    fun deleteKeyword(keyword: String) {
        intent {
            reduce {
                state.copy(
                    keywords = state.keywords.remove(keyword)
                )
            }
        }
    }

    fun addSuggestedKeyword(keyword: String) {
        addKeyword(keyword)
    }

    fun toggleNotification(isEnabled: Boolean) {
        intent {
            reduce { state.copy(isNotificationEnabled = isEnabled) }
        }
    }

    companion object {
        private const val MAX_KEYWORD_COUNT = 10
        private const val MAX_KEYWORD_LENGTH = 20
        private const val MIN_KEYWORD_LENGTH = 2
        private val WHITESPACE_REGEX = Regex("""\s""")
        val SUGGESTED_KEYWORDS: PersistentList<String> = persistentListOf("지갑", "카드", "학생증", "에어팟", "핸드폰")
    }
}
