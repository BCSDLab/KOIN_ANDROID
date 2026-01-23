package `in`.koreatech.koin.feature.lostandfound.ui.list

import android.os.Parcelable
import `in`.koreatech.koin.feature.lostandfound.enums.LostAndFoundFilterType
import `in`.koreatech.koin.feature.lostandfound.enums.LostAndFoundFilterType.ALL
import `in`.koreatech.koin.feature.lostandfound.model.LostAndFoundItemState
import kotlinx.collections.immutable.ImmutableList
import kotlinx.collections.immutable.persistentListOf
import kotlinx.parcelize.Parcelize

@Parcelize
data class LostAndFoundListState(
    val isLoading: Boolean = false,
    val isLoggedIn: Boolean = false,
    val showFilterLoginDialog: Boolean = false,
    val showWriteLoginDialog: Boolean = false,
    val isFirstPageLoading: Boolean = false,
    val showFilterBottomSheet: Boolean = false,
    val showWriteBottomSheet: Boolean = false,
    val searchQuery: String = "",
    val categoryFilterType: LostAndFoundFilterType = ALL,
    val lostOrFoundFilterType: LostAndFoundFilterType = ALL,
    val foundFilterType: LostAndFoundFilterType = ALL,
    val authorFilterType: LostAndFoundFilterType = ALL,
    val searchedArticles: ImmutableList<LostAndFoundItemState> = persistentListOf(),
    val searchedArticlesCurrentPage: Int = 1,
    val searchedArticlesTotalPage: Int = 1,
    val isLoadingMoreArticles: Boolean = false,
    val hasMoreArticles: Boolean = true
) : Parcelable
