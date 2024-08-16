package `in`.koreatech.koin.domain.model.article

data class ArticlePagination(
    val articleHeaders: List<ArticleHeader>,
    val totalCount: Int,
    val currentCount: Int,
    val totalPage: Int,
    val currentPage: Int
)