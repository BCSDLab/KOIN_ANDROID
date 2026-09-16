package `in`.koreatech.koin.domain.repository

import `in`.koreatech.koin.domain.model.dining.Dining
import kotlinx.coroutines.flow.Flow

interface DiningRepository {
    fun getDining(date: String, forceRefresh: Boolean = false): Flow<List<Dining>>

    suspend fun fetchDining(date: String): Result<Unit>
}
