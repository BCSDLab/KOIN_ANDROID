package `in`.koreatech.koin.data.repository

import `in`.koreatech.koin.data.constant.BUS_REQUEST_DATE_FORMAT
import `in`.koreatech.koin.data.constant.BUS_REQUEST_TIME_FORMAT
import `in`.koreatech.koin.data.mapper.*
import `in`.koreatech.koin.data.source.remote.BusRemoteDataSource
import `in`.koreatech.koin.domain.model.bus.*
import `in`.koreatech.koin.domain.repository.BusRepository
import java.time.LocalDateTime
import java.time.LocalTime
import java.time.format.DateTimeFormatter
import javax.inject.Inject

class BusRepositoryImpl @Inject constructor(
    private val busRemoteDataSource: BusRemoteDataSource
) : BusRepository {

    override suspend fun getBusCourses(): List<BusCourse> {
        return busRemoteDataSource.getBusCourses().map { it.toBusCourse() }
    }

    override suspend fun getShuttleBusTimetable(
        busDirection: BusDirection,
        region: String
    ): List<BusRoute.ShuttleBusRoute> {
        return busRemoteDataSource.getCommutingBusTimetable(
            busDirection = busDirection.busDirectionString,
            region = region
        )
            .map { response ->
                response.toShuttleBusRoute()
            }
    }

    override suspend fun getExpressBusTimetable(
        busDirection: BusDirection,
        region: String
    ): BusRoute.ExpressBusRoute {
        return busRemoteDataSource.getExpressBusTimetable(
            busDirection = busDirection.busDirectionString,
            region = region
        )
            .toExpressBusRoute()
    }

    override suspend fun getCommutingBusTimetable(
        busDirection: BusDirection,
        region: String
    ): List<BusRoute.CommutingBusRoute> {
        return busRemoteDataSource.getCommutingBusTimetable(
            busDirection = busDirection.busDirectionString,
            region = region
        )
            .map { response ->
                response.toCommutingBusRoute()
            }
    }

    override suspend fun getShuttleBusRemainTime(
        departure: BusNode,
        arrival: BusNode
    ): BusArrivalInfo.ShuttleBusArrivalInfo {
        return busRemoteDataSource.getBuses(
                busType = BusType.Shuttle.busTypeString,
                departure = departure.busNodeString,
                arrival = arrival.busNodeString
            ).toShuttleBusArrivalInfo()
    }

    override suspend fun getExpressBusRemainTime(
        departure: BusNode,
        arrival: BusNode
    ): BusArrivalInfo.ExpressBusArrivalInfo {
        return busRemoteDataSource.getBuses(
                busType = BusType.Express.busTypeString,
                departure = departure.busNodeString,
                arrival = arrival.busNodeString
            ).toExpressBusRemainTimePair()
    }

    override suspend fun getCommutingBusRemainTime(
        departure: BusNode,
        arrival: BusNode
    ): BusArrivalInfo.CommutingBusArrivalInfo {
        return busRemoteDataSource.getBuses(
                busType = BusType.Commuting.busTypeString,
                departure = departure.busNodeString,
                arrival = arrival.busNodeString
            ).toCommutingBusRemainTimePair()
    }

    override suspend fun getCityBusRemainTime(
        departure: BusNode,
        arrival: BusNode
    ): BusArrivalInfo.CityBusArrivalInfo {
        return busRemoteDataSource.getBuses(
                busType = BusType.City.busTypeString,
                departure = departure.busNodeString,
                arrival = arrival.busNodeString
            ).toCityBusRemainTimePair()
    }

    override suspend fun searchBus(
        time: LocalDateTime,
        departure: BusNode,
        arrival: BusNode
    ): List<BusSearchResult> {
        return busRemoteDataSource.searchBus(
            date = time.format(DateTimeFormatter.ofPattern(BUS_REQUEST_DATE_FORMAT)),
            time = time.format(DateTimeFormatter.ofPattern(BUS_REQUEST_TIME_FORMAT)),
            departure = departure.busNodeString,
            arrival = arrival.busNodeString
        ).map {
            it.toBusSearchResult(time)
        }
    }
}