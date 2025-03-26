package `in`.koreatech.bus.type

import android.os.Parcelable
import androidx.annotation.StringRes
import `in`.koreatech.koin.feature.bus.R
import kotlinx.parcelize.Parcelize

@Parcelize
enum class CommonDirectionType(
    @StringRes val titleRes: Int
) : Parcelable {
    TO_BYEONGCHEON(R.string.to_byeongcheon),
    TO_CHEONAN(R.string.to_cheonan)
}
