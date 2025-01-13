package `in`.koreatech.koin.domain.model.article

data class ArticleLostAndFoundUpload(
    val category: String,
    val location: String,
    val foundDate: String,
    val content: String?,
    val images: List<String>?
)