package `in`.koreatech.koin.ui.bus.state

import `in`.koreatech.koin.R
import `in`.koreatech.koin.domain.model.bus.BusArrivalInfo
import `in`.koreatech.koin.domain.model.bus.BusNode
import `in`.koreatech.koin.domain.model.bus.BusType
import android.content.Context
import java.time.LocalTime
import java.time.format.DateTimeFormatter

data class BusRemainTimeUiState(
    val type: BusType,
    val departure: String,
    val arrival: String,
    val nowBusRemainTime: String,
    val nowBusDepartureTime: String?,
    val nextBusRemainTime: String,
    val nextBusDepartureTime: String?,
    val busNumber: String? = null
)

fun BusArrivalInfo.ShuttleBusArrivalInfo.toShuttleBusRemainTimeUiState(
    context: Context,
    departure: BusNode,
    arrival: BusNode
) =
    BusRemainTimeUiState(
        type = BusType.Shuttle,
        departure = when (departure) {
            BusNode.Koreatech -> context.getString(R.string.bus_node_koreatech)
            BusNode.Station -> context.getString(R.string.bus_node_station)
            BusNode.Terminal -> context.getString(R.string.bus_node_terminal)
        },
        arrival = when (arrival) {
            BusNode.Koreatech -> context.getString(R.string.bus_node_koreatech)
            BusNode.Station -> context.getString(R.string.bus_node_station)
            BusNode.Terminal -> context.getString(R.string.bus_node_terminal)
        },
        nowBusRemainTime = this.nowBusRemainTime?.let {
            if(it >= 0) {
                LocalTime.ofSecondOfDay(it)
                    .format(DateTimeFormatter.ofPattern(context.getString(R.string.bus_remain_time_format)))
            } else {
                null
            }
        } ?: context.getString(R.string.bus_no_remain_time),
        nowBusDepartureTime = this.nowBusArrivalTime?.format(
            DateTimeFormatter.ofPattern(context.getString(R.string.bus_arrival_time_format))
        ),
        nextBusRemainTime = this.nextBusRemainTime?.let {
            if(it >= 0) {
                LocalTime.ofSecondOfDay(it)
                    .format(DateTimeFormatter.ofPattern(context.getString(R.string.bus_remain_time_format)))
            } else {
                null
            }
        } ?: context.getString(R.string.bus_no_remain_time),
        nextBusDepartureTime = this.nextBusArrivalTime?.format(
            DateTimeFormatter.ofPattern(context.getString(R.string.bus_arrival_time_format))
        )
    )

fun BusArrivalInfo.ExpressBusArrivalInfo.toExpressBusRemainTimeUiState(
    context: Context,
    departure: BusNode,
    arrival: BusNode
) =
    BusRemainTimeUiState(
        type = BusType.Express,
        departure = when (departure) {
            BusNode.Koreatech -> context.getString(R.string.bus_node_koreatech)
            BusNode.Station -> context.getString(R.string.bus_node_station)
            BusNode.Terminal -> context.getString(R.string.bus_node_terminal)
        },
        arrival = when (arrival) {
            BusNode.Koreatech -> context.getString(R.string.bus_node_koreatech)
            BusNode.Station -> context.getString(R.string.bus_node_station)
            BusNode.Terminal -> context.getString(R.string.bus_node_terminal)
        },
        nowBusRemainTime = this.nowBusRemainTime?.let {
            if(it >= 0) {
                LocalTime.ofSecondOfDay(it)
                    .format(DateTimeFormatter.ofPattern(context.getString(R.string.bus_remain_time_format)))
            } else {
                null
            }
        } ?: context.getString(R.string.bus_no_remain_time),
        nowBusDepartureTime = this.nowBusArrivalTime?.format(
            DateTimeFormatter.ofPattern(context.getString(R.string.bus_arrival_time_format))
        ),
        nextBusRemainTime = this.nextBusRemainTime?.let {
            if(it >= 0) {
                LocalTime.ofSecondOfDay(it)
                    .format(DateTimeFormatter.ofPattern(context.getString(R.string.bus_remain_time_format)))
            } else {
                null
            }
        } ?: context.getString(R.string.bus_no_remain_time),
        nextBusDepartureTime = this.nextBusArrivalTime?.format(
            DateTimeFormatter.ofPattern(context.getString(R.string.bus_arrival_time_format))
        )
    )

fun BusArrivalInfo.CityBusArrivalInfo.toCityBusRemainTimeUiState(
    context: Context,
    departure: BusNode,
    arrival: BusNode
) =
    BusRemainTimeUiState(
        type = BusType.City,
        departure = when (departure) {
            BusNode.Koreatech -> context.getString(R.string.bus_node_koreatech)
            BusNode.Station -> context.getString(R.string.bus_node_station)
            BusNode.Terminal -> context.getString(R.string.bus_node_terminal)
        },
        arrival = when (arrival) {
            BusNode.Koreatech -> context.getString(R.string.bus_node_koreatech)
            BusNode.Station -> context.getString(R.string.bus_node_station)
            BusNode.Terminal -> context.getString(R.string.bus_node_terminal)
        },
        nowBusRemainTime = this.nowBusRemainTime?.let {
            if(it >= 0) {
                LocalTime.ofSecondOfDay(it)
                    .format(DateTimeFormatter.ofPattern(context.getString(R.string.bus_remain_time_format)))
            } else {
                null
            }
        } ?: context.getString(R.string.bus_no_remain_time),
        nowBusDepartureTime = this.nowBusArrivalTime?.format(
            DateTimeFormatter.ofPattern(context.getString(R.string.bus_arrival_time_format))
        ),
        nextBusRemainTime = this.nextBusRemainTime?.let {
            if(it >= 0) {
            LocalTime.ofSecondOfDay(it)
                .format(DateTimeFormatter.ofPattern(context.getString(R.string.bus_remain_time_format)))
            } else {
                null
            }
        } ?: context.getString(R.string.bus_no_remain_time),
        nextBusDepartureTime = this.nextBusArrivalTime?.format(
            DateTimeFormatter.ofPattern(context.getString(R.string.bus_arrival_time_format))
        ),
        busNumber = busNumber?.let {
            context.getString(R.string.city_bus_number_format, it)
        }
    )