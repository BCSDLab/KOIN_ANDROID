package `in`.koreatech.koin.domain.model.article

data class ArticleLostAndFoundPagination(
    val articleLostAndFoundHeader: List<ArticleLostAndFoundHeader>,
    val totalCount: Int,
    val currentCount: Int,
    val totalPage: Int,
    val currentPage: Int
)
