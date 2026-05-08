package `in`.koreatech.koin.feature.lostandfound.ui.keyword

import androidx.lifecycle.ViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import `in`.koreatech.koin.feature.lostandfound.R
import javax.inject.Inject
import kotlinx.collections.immutable.ImmutableList
import kotlinx.collections.immutable.persistentListOf
import kotlinx.collections.immutable.toPersistentList
import org.orbitmvi.orbit.ContainerHost
import org.orbitmvi.orbit.syntax.simple.blockingIntent
import org.orbitmvi.orbit.syntax.simple.intent
import org.orbitmvi.orbit.syntax.simple.postSideEffect
import org.orbitmvi.orbit.syntax.simple.reduce
import org.orbitmvi.orbit.viewmodel.container

@HiltViewModel
class LostAndFoundKeywordViewModel @Inject constructor() :
    ViewModel(),
    ContainerHost<LostAndFoundKeywordState, LostAndFoundKeywordSideEffect> {
    override val container = container<LostAndFoundKeywordState, LostAndFoundKeywordSideEffect>(
        initialState = LostAndFoundKeywordState()
    )

    fun onKeywordInputChanged(input: String) {
        blockingIntent {
            reduce { state.copy(keywordInput = input) }
        }
    }

    fun addKeyword(keyword: String) {
        intent {
            val trimmed = keyword.trim()
            when {
                trimmed.isEmpty() -> {
                    postSideEffect(LostAndFoundKeywordSideEffect.ShowSnackbar(R.string.keyword_add_require_input))
                    return@intent
                }
                trimmedKeywordRegex.containsMatchIn(trimmed) -> {
                    postSideEffect(LostAndFoundKeywordSideEffect.ShowSnackbar(R.string.keyword_add_blank_not_allowed))
                    return@intent
                }
                trimmed.length < MIN_KEYWORD_LENGTH || trimmed.length > MAX_KEYWORD_LENGTH -> {
                    postSideEffect(LostAndFoundKeywordSideEffect.ShowSnackbar(R.string.keyword_add_invalid_length))
                    return@intent
                }
            }

            when {
                state.keywords.size >= MAX_KEYWORD_COUNT -> {
                    postSideEffect(LostAndFoundKeywordSideEffect.ShowSnackbar(R.string.keyword_add_limit_exceeded))
                }
                state.keywords.contains(trimmed) -> {
                    postSideEffect(LostAndFoundKeywordSideEffect.ShowSnackbar(R.string.keyword_add_already_exist))
                }
                else -> {
                    reduce {
                        state.copy(
                            keywords = (state.keywords + trimmed).toPersistentList(),
                            keywordInput = ""
                        )
                    }
                }
            }
        }
    }

    fun deleteKeyword(keyword: String) {
        intent {
            reduce {
                state.copy(
                    keywords = state.keywords.minus(keyword).toPersistentList()
                )
            }
        }
    }

    fun addSuggestedKeyword(keyword: String) {
        addKeyword(keyword)
    }

    fun toggleNotification() {
        intent {
            reduce { state.copy(isNotificationEnabled = !state.isNotificationEnabled) }
        }
    }

    companion object {
        private const val MAX_KEYWORD_COUNT = 10
        private const val MAX_KEYWORD_LENGTH = 20
        private const val MIN_KEYWORD_LENGTH = 2
        private val trimmedKeywordRegex = Regex("""\s+""")
        val SUGGESTED_KEYWORDS: ImmutableList<String> = persistentListOf("지갑", "카드", "학생증", "에어팟", "핸드폰")
    }
}
