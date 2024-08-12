package `in`.koreatech.koin.domain.model.article

data class ArticlePagination(
    val articles: List<Article>,
    val totalCount: Int,
    val currentCount: Int,
    val totalPage: Int,
    val currentPage: Int
)