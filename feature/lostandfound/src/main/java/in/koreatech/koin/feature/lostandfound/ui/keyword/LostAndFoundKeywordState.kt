package `in`.koreatech.koin.feature.lostandfound.ui.keyword

import androidx.compose.runtime.Immutable
import kotlinx.collections.immutable.ImmutableList
import kotlinx.collections.immutable.persistentListOf

@Immutable
data class LostAndFoundKeywordState(
    val keywords: ImmutableList<String> = persistentListOf(),
    val keywordInput: String = "",
    val isNotificationEnabled: Boolean = false
)
