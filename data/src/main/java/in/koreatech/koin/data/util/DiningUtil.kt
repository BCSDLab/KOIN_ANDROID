package `in`.koreatech.koin.data.util

import `in`.koreatech.koin.data.R
import `in`.koreatech.koin.domain.model.dining.DiningType
import android.content.Context

fun DiningType.localized(context: Context) = when(this) {
    DiningType.Breakfast -> context.getString(R.string.dining_breakfast)
    DiningType.Lunch -> context.getString(R.string.dining_lunch)
    DiningType.Dinner -> context.getString(R.string.dining_dinner)
    DiningType.NextBreakfast -> context.getString(R.string.dining_next_breakfast)
}