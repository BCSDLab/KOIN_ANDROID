package `in`.koreatech.koin.feature.lostandfound.ui.list

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class LostAndFoundListState(
    val showFilterBottomSheet: Boolean = false,
    val showWriteBottomSheet: Boolean = false
) : Parcelable