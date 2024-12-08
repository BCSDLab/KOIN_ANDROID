package `in`.koreatech.bus.type

import android.os.Parcelable
import androidx.annotation.StringRes
import `in`.koreatech.koin.feature.bus.R
import kotlinx.parcelize.Parcelize

@Parcelize
enum class CityBusNumberType(
    @StringRes val titleRes: Int,
    val numberQuery: Int
) : Parcelable {
    N400(R.string.n400, 400),
    N405(R.string.n405, 405),
    N495(R.string.n495, 495),
}