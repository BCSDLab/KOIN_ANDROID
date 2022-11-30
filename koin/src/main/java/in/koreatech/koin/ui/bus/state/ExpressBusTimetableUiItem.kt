package `in`.koreatech.koin.ui.bus.state

import `in`.koreatech.koin.R
import `in`.koreatech.koin.domain.model.bus.timetable.BusNodeInfo
import android.content.Context

data class ExpressBusTimetableUiItem(
    val departureTime: String,
    val arrivalTime: String,
    val charge: String
)

fun BusNodeInfo.ExpressNodeInfo.toExpressBusTimetableUiItem(context: Context) = ExpressBusTimetableUiItem(
    departureTime = departureTime,
    arrivalTime = arrivalTime,
    charge = "$charge${context.getString(R.string.dining_money_unit)}"
)