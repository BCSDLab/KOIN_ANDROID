package `in`.koreatech.business.feature.insertstore.insertdetailinfo.operatingTime

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class TimeSettingState(
    val timeInfoString: String,
    val dayOfWeekList: List<KorDayOfWeek>,
    val openTime: String,
    val closeTime: String,
    val isClosed: Boolean,
    val is24Hours: Boolean,
) : Parcelable

@Parcelize
data class KorDayOfWeek(
    val kor: String,
    val eng: String,
    val priority: Int,
) : Parcelable
