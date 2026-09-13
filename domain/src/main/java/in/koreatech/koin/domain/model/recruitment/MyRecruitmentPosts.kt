package `in`.koreatech.koin.domain.model.recruitment

data class MyRecruitmentPosts(
    val posts: List<MyRecruitmentPost>,
    val totalCount: Long,
    val currentCount: Int,
    val totalPage: Int,
    val currentPage: Int
)
