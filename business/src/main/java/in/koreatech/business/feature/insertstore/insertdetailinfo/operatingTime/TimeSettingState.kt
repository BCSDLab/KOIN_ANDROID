package `in`.koreatech.business.feature.insertstore.insertdetailinfo.operatingTime

import android.os.Parcelable
import com.chargemap.compose.numberpicker.Hours
import kotlinx.parcelize.Parcelize

@Parcelize
data class TimeSettingState(
    val timeInfoString: String,
    val dayOfWeekList: List<String>,
    val openTime: String,
    val closeTime: String,
    val isClosed: Boolean,
    val is24Hours: Boolean
): Parcelable