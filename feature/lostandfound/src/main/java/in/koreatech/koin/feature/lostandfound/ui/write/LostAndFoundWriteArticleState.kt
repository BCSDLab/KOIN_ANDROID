package `in`.koreatech.koin.feature.lostandfound.ui.write

import android.os.Parcelable
import `in`.koreatech.koin.feature.lostandfound.enums.LostOrFoundType
import kotlinx.parcelize.Parcelize

@Parcelize
data class LostAndFoundWriteArticleState(
    val lostOrFoundType: LostOrFoundType = LostOrFoundType.FOUND,
    val itemList: List<LostAndFoundWriteArticleItemState> = emptyList(),
    val isAllFieldValid: Boolean = false,
) : Parcelable
