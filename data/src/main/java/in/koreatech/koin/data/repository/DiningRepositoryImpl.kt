package `in`.koreatech.koin.data.repository

import `in`.koreatech.koin.data.mapper.toDining
import `in`.koreatech.koin.data.mapper.toDiningEntity
import `in`.koreatech.koin.data.response.DiningResponse
import `in`.koreatech.koin.data.source.local.DiningLocalDataSource
import `in`.koreatech.koin.data.source.remote.DiningRemoteDataSource
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
                    fetchDining(date).getOrThrow()
                }
            }
    }

    override suspend fun fetchDining(date: String): Result<Unit> = suspendRunCatching {
        val dining = diningRemoteDataSource.getDining(date).map(DiningResponse::toDining)
        diningLocalDataSource.saveDining(date, dining.map { it.toDiningEntity(date) })
    }
}
