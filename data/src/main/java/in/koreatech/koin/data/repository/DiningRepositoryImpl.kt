package `in`.koreatech.koin.data.repository

import `in`.koreatech.koin.data.mapper.toDining
import `in`.koreatech.koin.data.response.DiningResponse
import `in`.koreatech.koin.data.source.remote.DiningRemoteDataSource
import `in`.koreatech.koin.domain.model.dining.Dining
import `in`.koreatech.koin.domain.repository.DiningRepository
import javax.inject.Inject

class DiningRepositoryImpl @Inject constructor(
    private val diningRemoteDataSource: DiningRemoteDataSource
) : DiningRepository {
    override suspend fun getDining(date: String): List<Dining> {
        return diningRemoteDataSource.getDining(date).map(DiningResponse::toDining)
    }
}