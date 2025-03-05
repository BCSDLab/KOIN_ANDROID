package `in`.koreatech.koin.feature.lostandfound.enums

import androidx.annotation.StringRes
import `in`.koreatech.koin.feature.lostandfound.R

enum class LostOrFoundType(
    @StringRes val stringRes: Int,
) {
    LOST(R.string.lost_item), // 분실물
    FOUND(R.string.found_item), // 습득물
}
