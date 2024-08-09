package `in`.koreatech.koin.domain.repository

import `in`.koreatech.koin.domain.model.dining.Dining

interface DiningRepository {
    suspend fun getDining(date: String): List<Dining>
    suspend fun getAuthDining(date: String): List<Dining>
    suspend fun likeDining(id: Int)
    suspend fun unlikeDining(id: Int)
}