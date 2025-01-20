package `in`.koreatech.koin.domain.model.article

data class ArticleLostAndFoundUpload(
    val category: String,
    val foundPlace: String,
    val foundDate: String,
    val content: String?,
    val images: List<String>?
)