package `in`.koreatech.koin.domain.model.store

data class StoreReview(
    val totalCount: Int,
    val currentCount: Int,
    val totalPage: Int,
    val currentPage: Int,
    val statistics: StoreReviewStatistics,
    val reviews: List<StoreReviewContent>
) {
    companion object {
        fun empty() = StoreReview(
            totalCount = 0,
            currentCount = 0,
            totalPage = 0,
            currentPage = 0,
            statistics = StoreReviewStatistics.empty(),
            reviews = emptyList()
        )
    }
}
