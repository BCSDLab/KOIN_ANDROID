package `in`.koreatech.koin.feature.lostandfound.ui.list

import android.os.Parcelable
import `in`.koreatech.koin.feature.lostandfound.enums.LostAndFoundFilterType.AuthorFilterType
import `in`.koreatech.koin.feature.lostandfound.enums.LostAndFoundFilterType.CategoryFilterType
import `in`.koreatech.koin.feature.lostandfound.enums.LostAndFoundFilterType.FoundFilterType
import `in`.koreatech.koin.feature.lostandfound.enums.LostAndFoundFilterType.LostOrFoundFilterType
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
    val isFirstPageLoading: Boolean = true,
    val showFilterBottomSheet: Boolean = false,
    val showWriteBottomSheet: Boolean = false,
    val searchQuery: String = "",
    val categoryFilterType: ImmutableList<CategoryFilterType> = persistentListOf(CategoryFilterType.ALL),
    val lostOrFoundFilterType: LostOrFoundFilterType = LostOrFoundFilterType.ALL,
    val foundFilterType: FoundFilterType = FoundFilterType.ALL,
    val authorFilterType: AuthorFilterType = AuthorFilterType.ALL,
    val searchedArticles: ImmutableList<LostAndFoundItemState> = persistentListOf(),
    val searchedArticlesCurrentPage: Int = 1,
    val searchedArticlesTotalPage: Int = 1,
    val isLoadingMoreArticles: Boolean = false,
    val hasMoreArticles: Boolean = true,
    val keywords: ImmutableList<String> = persistentListOf(),
    val selectedKeywordIndex: Int = 0
) : Parcelable
