package `in`.koreatech.koin.ui.bus.state

import `in`.koreatech.koin.R
import `in`.koreatech.koin.data.util.busNumberFormatted
import `in`.koreatech.koin.data.util.localized
import `in`.koreatech.koin.data.util.toBusArrivalTimeFormatted
import `in`.koreatech.koin.data.util.toBusRemainTimeFormatted
import `in`.koreatech.koin.domain.model.bus.timer.BusArrivalInfo
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
        departure = departure.localized(context),
        arrival = arrival.localized(context),
        nowBusRemainTime = nowBusRemainTime.toBusRemainTimeFormatted(context),
        nowBusDepartureTime = nowBusArrivalTime?.toBusArrivalTimeFormatted(context),
        nextBusRemainTime = nextBusRemainTime.toBusRemainTimeFormatted(context),
        nextBusDepartureTime = nextBusArrivalTime?.toBusArrivalTimeFormatted(context),
    )

fun BusArrivalInfo.ExpressBusArrivalInfo.toExpressBusRemainTimeUiState(
    context: Context,
    departure: BusNode,
    arrival: BusNode
) =
    BusRemainTimeUiState(
        type = BusType.Express,
        departure = departure.localized(context),
        arrival = arrival.localized(context),
        nowBusRemainTime = nowBusRemainTime.toBusRemainTimeFormatted(context),
        nowBusDepartureTime = nowBusArrivalTime?.toBusArrivalTimeFormatted(context),
        nextBusRemainTime = nextBusRemainTime.toBusRemainTimeFormatted(context),
        nextBusDepartureTime = nextBusArrivalTime?.toBusArrivalTimeFormatted(context)
    )

fun BusArrivalInfo.CityBusArrivalInfo.toCityBusRemainTimeUiState(
    context: Context,
    departure: BusNode,
    arrival: BusNode
) =
    BusRemainTimeUiState(
        type = BusType.City,
        departure = departure.localized(context),
        arrival = arrival.localized(context),
        nowBusRemainTime = nowBusRemainTime.toBusRemainTimeFormatted(context),
        nowBusDepartureTime = nowBusArrivalTime?.toBusArrivalTimeFormatted(context),
        nextBusRemainTime = nextBusRemainTime.toBusRemainTimeFormatted(context),
        nextBusDepartureTime = nextBusArrivalTime?.toBusArrivalTimeFormatted(context),
        busNumber = busNumber?.busNumberFormatted(context)
    )