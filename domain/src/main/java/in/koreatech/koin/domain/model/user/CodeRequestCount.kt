package `in`.koreatech.koin.domain.model.user

data class CodeRequestCount(
    val target: String,
    val totalCount: Int,
    val remainingCount: Int,
    val currentCount: Int
)
