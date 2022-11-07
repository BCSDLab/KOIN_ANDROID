package `in`.koreatech.koin.data.repository

import `in`.koreatech.koin.data.constant.BUS_REQUEST_DATE_FORMAT
import `in`.koreatech.koin.data.constant.BUS_REQUEST_TIME_FORMAT
import `in`.koreatech.koin.data.mapper.*
import `in`.koreatech.koin.data.source.local.BusLocalDataSource
import `in`.koreatech.koin.data.source.remote.BusRemoteDataSource
import `in`.koreatech.koin.domain.model.bus.*
import `in`.koreatech.koin.domain.model.bus.course.BusCourse
import `in`.koreatech.koin.domain.model.bus.search.BusSearchResult
import `in`.koreatech.koin.domain.model.bus.timer.BusArrivalInfo
import `in`.koreatech.koin.domain.model.bus.timetable.BusRoute
import `in`.koreatech.koin.domain.repository.BusRepository
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter
import javax.inject.Inject

class BusRepositoryImpl @Inject constructor(
    private val busLocalDataSource: BusLocalDataSource,
    private val busRemoteDataSource: BusRemoteDataSource
) : BusRepository {

    override suspend fun getShuttleBusCourses(): List<Pair<BusCourse, String>> {
        return busRemoteDataSource.getBusCourses()
            .map { it.toBusCourse().let { it to it.toCourseNameString() } }
    }

    override suspend fun getExpressBusCourses(): List<Pair<BusCourse, String>> {
        return busLocalDataSource.getExpressCourses()
            .map { it to it.toCourseNameString() }
    }

    override suspend fun getShuttleBusTimetable(busCourse: BusCourse): List<BusRoute.ShuttleBusRoute> {
        return if (busCourse.busType == BusType.Shuttle) {
            busRemoteDataSource.getShuttleBusTimetable(
                busDirection = busCourse.direction.busDirectionString,
                region = busCourse.region
            )
        } else {
            busRemoteDataSource.getCommutingBusTimetable(
                busDirection = busCourse.direction.busDirectionString,
                region = busCourse.region
            )
        }
            .map { it.toShuttleBusRoute() }
    }

    override suspend fun getExpressBusTimetable(busCourse: BusCourse): BusRoute.ExpressBusRoute {
        return busRemoteDataSource.getExpressBusTimetable(
            busDirection = busCourse.direction.busDirectionString
        ).toExpressBusRoute()
    }

    override suspend fun getCityBusTimetable(): BusRoute.CityBusRoute {
        return busLocalDataSource.getCityBusTimetable().toCityBusRoute()
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