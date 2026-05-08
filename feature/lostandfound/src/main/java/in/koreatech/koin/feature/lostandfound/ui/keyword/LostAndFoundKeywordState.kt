package `in`.koreatech.koin.feature.lostandfound.ui.keyword

import kotlinx.collections.immutable.PersistentList
import kotlinx.collections.immutable.persistentListOf

data class LostAndFoundKeywordState(
    val keywords: PersistentList<String> = persistentListOf(),
    val keywordInput: String = "",
    val isNotificationEnabled: Boolean = false
)
