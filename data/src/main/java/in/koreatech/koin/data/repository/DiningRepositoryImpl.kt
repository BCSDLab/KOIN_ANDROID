package `in`.koreatech.koin.data.repository

import `in`.koreatech.koin.data.mapper.toDining
import `in`.koreatech.koin.data.mapper.toDiningEntity
import `in`.koreatech.koin.data.response.DiningResponse
import `in`.koreatech.koin.data.source.local.DiningLocalDataSource
import `in`.koreatech.koin.data.source.remote.DiningRemoteDataSource
import `in`.koreatech.koin.data.util.mapHttpFailure
import `in`.koreatech.koin.domain.error.dining.KoinDiningException
import `in`.koreatech.koin.domain.model.dining.Dining
import `in`.koreatech.koin.domain.repository.DiningRepository
import `in`.koreatech.koin.domain.util.suspendRunCatching
import javax.inject.Inject
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.onStart

class DiningRepositoryImpl @Inject constructor(
    private val diningRemoteDataSource: DiningRemoteDataSource,
    private val diningLocalDataSource: DiningLocalDataSource
) : DiningRepository {
    override fun getDining(date: String, forceRefresh: Boolean): Flow<List<Dining>> {
        return diningLocalDataSource.observeDining(date)
            .map { dining -> dining.map { it.toDining() } }
            .onStart {
                if (forceRefresh || diningLocalDataSource.observeDining(date).first().isEmpty()) {
                    fetchDining(date)
                }
            }
    }

    override suspend fun fetchDining(date: String): Result<Unit> = suspendRunCatching {
        val dining = diningRemoteDataSource.getDining(date).map(DiningResponse::toDining)
        diningLocalDataSource.saveDining(date, dining.map { it.toDiningEntity(date) })
    }

    override suspend fun postDiningSoldOutReport(diningId: Int, imageUrl: String): Result<Unit> = suspendRunCatching {
        diningRemoteDataSource.postDiningSoldOutReport(diningId, imageUrl)
    }.mapHttpFailure {
        on(400, "DINING_REPORT_DATE_NOT_ALLOWED") throws KoinDiningException.DiningReportDateNotAllowedException()
        on(400, "INVALID_REPORT_IMAGE") throws KoinDiningException.InvalidReportImageException()
        on(404, "NOT_FOUND_DINING") throws KoinDiningException.NotFoundDiningException()
        on(409, "DINING_ALREADY_SOLD_OUT") throws KoinDiningException.DiningAlreadySoldOutException()
        on(409, "DINING_REPORT_ALREADY_SUBMITTED") throws KoinDiningException.DiningReportAlreadySubmittedException()
    }
}
