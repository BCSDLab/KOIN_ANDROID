package `in`.koreatech.koin.domain.model.user

data class CodeCount(
    val target: String,
    val totalCount: Int,
    val remainingCount: Int,
    val currentCount: Int
)
