package `in`.koreatech.koin.domain.model.recruitment

data class Recruitments(
    val recruitments: List<Recruitment>,
    val totalCount: Long,
    val currentCount: Int,
    val totalPage: Int,
    val currentPage: Int
)
