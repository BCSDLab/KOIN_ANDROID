package `in`.koreatech.koin.domain.repository

import `in`.koreatech.koin.domain.model.dining.Dining

interface DiningRepository {
    suspend fun getDining(date: String): List<Dining>
}