package `in`.koreatech.business.util.ext

import android.annotation.SuppressLint
import com.chargemap.compose.numberpicker.Hours
import `in`.koreatech.business.feature.insertstore.insertdetailinfo.operatingTime.KorDayOfWeek

@SuppressLint("DefaultLocale")
fun Hours.toTimeString(): String = String.format("%02d:%02d", this.hours, this.minutes)

fun makeTimeInfo(
    dayOfWeekList: List<KorDayOfWeek>,
    openTime: String,
    closeTime: String,
    isClosed: Boolean,
    is24Hours: Boolean
): String {
    var infoString = dayOfWeekList.joinToString(", "){it.kor}

    infoString = if(isClosed) "$infoString : 휴무"
    else if (is24Hours) "$infoString : 24시간 운영"
    else "$infoString : $openTime ~ $closeTime"

    return infoString
}