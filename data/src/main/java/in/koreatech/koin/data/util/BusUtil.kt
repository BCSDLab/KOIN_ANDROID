package `in`.koreatech.koin.data.util

import `in`.koreatech.koin.data.R
import `in`.koreatech.koin.domain.model.bus.BusNode
import `in`.koreatech.koin.domain.model.bus.timer.BusArrivalInfo
import android.content.Context
import java.time.LocalTime
import java.time.format.DateTimeFormatter

fun BusNode.localized(context: Context) = when (this) {
    BusNode.Koreatech -> context.getString(R.string.bus_node_koreatech)
    BusNode.Station -> context.getString(R.string.bus_node_station)
    BusNode.Terminal -> context.getString(R.string.bus_node_terminal)
}

fun Long?.toBusRemainTimeFormatted(context: Context) = this?.let {
    if(it >= 0) {
        LocalTime.ofSecondOfDay(it)
            .format(DateTimeFormatter.ofPattern(context.getString(R.string.bus_remain_time_format)))
    } else {
        null
    }
} ?: context.getString(R.string.bus_no_remain_time)

fun LocalTime.toBusArrivalTimeFormatted(context: Context) = format(
    DateTimeFormatter.ofPattern(context.getString(R.string.bus_arrival_time_format))
)

fun BusArrivalInfo.localized(context: Context) = when(this) {
    is BusArrivalInfo.CityBusArrivalInfo -> context.getString(R.string.bus_name_city)
    is BusArrivalInfo.CommutingBusArrivalInfo -> context.getString(R.string.bus_name_commuting)
    is BusArrivalInfo.ExpressBusArrivalInfo -> context.getString(R.string.bus_name_express)
    is BusArrivalInfo.ShuttleBusArrivalInfo -> context.getString(R.string.bus_name_shuttle)
}

fun Int.busNumberFormatted(context: Context) = context.getString(R.string.city_bus_number_format, this)