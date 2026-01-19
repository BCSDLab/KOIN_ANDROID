package `in`.koreatech.koin.feature.lostandfound.ui.list

import android.os.Parcelable
import `in`.koreatech.koin.feature.lostandfound.model.LostAndFoundItemState
import kotlinx.parcelize.Parcelize

@Parcelize
data class LostAndFoundListState(
    val isLoading: Boolean = false,
    val showFilterBottomSheet: Boolean = false,
    val showWriteBottomSheet: Boolean = false,
    val searchedArticles: List<LostAndFoundItemState> = emptyList(),
    val searchedArticlesCurrentPage: Int = 1,
    val searchedArticlesTotalPage: Int = 1,
    val isLoadingMoreArticles: Boolean = false,
    val hasMoreArticles: Boolean = true
) : Parcelable