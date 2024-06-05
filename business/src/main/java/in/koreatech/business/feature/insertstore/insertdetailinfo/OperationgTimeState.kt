package `in`.koreatech.business.feature.insertstore.insertdetailinfo

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize
data class OperatingTimeState(
    val closeTime: String = "",
    val closed: Boolean = false,
    val dayOfWeek: String = "",
    val openTime: String = ""
): Parcelable