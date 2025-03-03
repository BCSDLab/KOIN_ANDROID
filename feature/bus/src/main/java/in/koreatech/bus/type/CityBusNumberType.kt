package `in`.koreatech.bus.type

import android.os.Parcelable
import androidx.annotation.StringRes
import `in`.koreatech.koin.feature.bus.R
import kotlinx.parcelize.Parcelize

@Parcelize
enum class CityBusNumberType(
    @StringRes val titleRes: Int,
    val numberQuery: Int,
    val toByeongcheonQuery: String,
    val toCheonanQuery: String,
) : Parcelable {
    N400(R.string.n400, 400, "병천3리", "종합터미널"),
    N402(R.string.n402, 402, "황사동", "종합터미널"),
    N405(R.string.n405, 405, "유관순열사사적지", "종합터미널"),
}
