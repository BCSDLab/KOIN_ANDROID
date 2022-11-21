package `in`.koreatech.koin.data.mapper

import `in`.koreatech.koin.data.R
import `in`.koreatech.koin.domain.model.bus.BusDirection
import `in`.koreatech.koin.domain.model.bus.BusType
import `in`.koreatech.koin.domain.model.bus.course.BusCourse
import android.content.Context

fun BusCourse.toCourseNameString(context: Context): String {
    return when (busType) {
        BusType.Shuttle -> {
            "$region ${
                if (direction == BusDirection.ToKoreatech) context.getString(
                    R.string.bus_course_shuttle_to_koreatech
                ) else context.getString(R.string.bus_course_shuttle_from_koreatech)
            }"
        }
        BusType.Commuting -> {
            "$region ${
                if (direction == BusDirection.ToKoreatech) context.getString(R.string.bus_course_commuting_to_koreatech) else context.getString(
                    R.string.bus_course_commuting_from_koreatech
                )
            }"
        }
        BusType.Express -> {
            if (direction == BusDirection.ToKoreatech) context.getString(R.string.bus_course_express_to_koreatech) else context.getString(
                R.string.bus_course_express_from_koreatech
            )
        }
        else -> ""
    }
}