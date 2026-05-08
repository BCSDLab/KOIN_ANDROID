package `in`.koreatech.koin.feature.lostandfound.ui.keyword

import android.os.Parcelable
import kotlinx.collections.immutable.ImmutableList
import kotlinx.collections.immutable.persistentListOf
import kotlinx.parcelize.Parcelize

@Parcelize
data class LostAndFoundKeywordState(
    val keywords: ImmutableList<String> = persistentListOf(),
    val keywordInput: String = "",
    val isNotificationEnabled: Boolean = false
) : Parcelable
