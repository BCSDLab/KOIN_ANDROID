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
import android.content.Context
import dagger.hilt.android.qualifiers.ApplicationContext
import `in`.koreatech.koin.domain.model.bus.city.CityBusInfo
import `in`.koreatech.koin.domain.model.bus.timetable.BusTimetable
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter
import javax.inject.Inject

class BusRepositoryImpl @Inject constructor(
    @ApplicationContext private val context: Context,
    private val busLocalDataSource: BusLocalDataSource,
    private val busRemoteDataSource: BusRemoteDataSource
) : BusRepository {

    override suspend fun getShuttleBusCourses(): List<Pair<BusCourse, String>> {
        return busRemoteDataSource.getBusCourses()
            .map { it.toBusCourse().let { it to it.toCourseNameString(context) } }
    }

    override suspend fun getExpressBusCourses(): List<Pair<BusCourse, String>> {
        return busLocalDataSource.getExpressCourses()
            .map { it to it.toCourseNameString(context) }
    }

    override suspend fun getShuttleBusTimetable(busCourse: BusCourse): BusTimetable.ShuttleBusTimetable {
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
        }.toShuttleBusTimetable()
    }

    override suspend fun getExpressBusTimetable(busCourse: BusCourse): BusTimetable.ExpressBusTimetable {
        return busRemoteDataSource.getExpressBusTimetable(
            busDirection = busCourse.direction.busDirectionString
        ).toExpressBusTimetable()
    }

    override suspend fun getCityBusTimetable(cityBusInfo: CityBusInfo): BusTimetable.CityBusTimetable {
        return busRemoteDataSource.getCityBusTimetable(
            number = cityBusInfo.busNumber,
            direction = cityBusInfo.direction
        ).toCityBusTimetable()
    }

    override suspend fun getShuttleBusRemainTime(
        departure: BusNode,
        arrival: BusNode
    ): BusArrivalInfo.ShuttleBusArrivalInfo {
        return busRemoteDataSource.getBuses(
            busType = BusType.Shuttle.busTypeString,
            departure = departure.busNodeString,
            arrival = arrival.busNodeString
        ).toShuttleBusArrivalInfo(departure, arrival)
    }

    override suspend fun getExpressBusRemainTime(
        departure: BusNode,
        arrival: BusNode
    ): BusArrivalInfo.ExpressBusArrivalInfo {
        return busRemoteDataSource.getBuses(
            busType = BusType.Express.busTypeString,
            departure = departure.busNodeString,
            arrival = arrival.busNodeString
        ).toExpressBusRemainTimePair(departure, arrival)
    }

    override suspend fun getCommutingBusRemainTime(
        departure: BusNode,
        arrival: BusNode
    ): BusArrivalInfo.CommutingBusArrivalInfo {
        return busRemoteDataSource.getBuses(
            busType = BusType.Commuting.busTypeString,
            departure = departure.busNodeString,
            arrival = arrival.busNodeString
        ).toCommutingBusRemainTimePair(departure, arrival)
    }

    override suspend fun getCityBusRemainTime(
        departure: BusNode,
        arrival: BusNode
    ): BusArrivalInfo.CityBusArrivalInfo {
        return busRemoteDataSource.getBuses(
            busType = BusType.City.busTypeString,
            departure = departure.busNodeString,
            arrival = arrival.busNodeString
        ).toCityBusRemainTimePair(departure, arrival)
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
            it.toBusSearchResult(context)
        }
    }
}