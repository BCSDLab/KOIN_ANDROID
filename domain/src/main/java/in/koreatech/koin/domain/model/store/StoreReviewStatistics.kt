package `in`.koreatech.koin.domain.model.store

data class StoreReviewStatistics (
    val averageRating: Double,
    val ratings: Map<String, Int>
)