package `in`.koreatech.business.util.ext

import com.chargemap.compose.numberpicker.Hours
import `in`.koreatech.business.feature.insertstore.insertdetailinfo.operatingTime.KorDayOfWeek

fun Hours.toTimeString(): String {

    val hoursString: String =
        if (this.hours < 10) "0" + this.hours.toString() else this.hours.toString()

    val minutesString: String =
        if (this.minutes < 10) "0" + this.minutes.toString() else this.minutes.toString()

    return "$hoursString:$minutesString"
}

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
    else infoString + " : ${openTime} ~ ${closeTime}"

    return infoString
}