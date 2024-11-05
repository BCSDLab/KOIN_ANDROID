package `in`.koreatech.bus.screen.timetable.type

import androidx.annotation.StringRes
import `in`.koreatech.koin.feature.bus.R

enum class ExpressDirectionType(
    @StringRes val titleRes: Int
) {
    TO_BYEONGCHEON(R.string.to_byeongcheon),
    TO_CHEONAN(R.string.to_cheonan),
}