package `in`.koreatech.koin.domain.model.store

data class StoreReviewStatistics(
    val averageRating: Double,
    val ratings: Map<String, Int>
) {
    companion object {
        fun empty() = StoreReviewStatistics(
            averageRating = 0.0,
            ratings = emptyMap()
        )
    }
}
