package `in`.koreatech.koin.domain.model.store

data class StoreReview(
    val totalCount: Int,
    val currentCount: Int,
    val totalPage: Int,
    val currentPage: Int,
    val statistics: StoreReviewStatistics,
    val reviews: List<StoreReviewContent>
)