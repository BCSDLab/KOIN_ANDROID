package `in`.koreatech.koin.feature.store.notice

import android.os.Parcelable
import androidx.compose.runtime.Immutable
import kotlinx.collections.immutable.ImmutableList
import kotlinx.collections.immutable.persistentListOf
import kotlinx.parcelize.Parcelize

@Immutable
@Parcelize
data class StoreNoticeListState(
    val isLoading: Boolean = false,
    val isFirstPageLoading: Boolean = true,
    val notices: ImmutableList<StoreNoticeItemState> = persistentListOf()
) : Parcelable

@Immutable
@Parcelize
data class StoreNoticeItemState(
    val id: Int,
    val title: String,
    val description: String,
    val dateRange: String,
    val imageUris: ImmutableList<String> = persistentListOf(),
    val isExpanded: Boolean = false
) : Parcelable
