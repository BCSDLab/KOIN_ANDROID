package `in`.koreatech.koin.domain.model.article

data class LostAndFoundFilterParams(
    val type: String? = null,
    val page: Int = 1,
    val limit: Int = 10,
    val category: String = "ALL",
    val foundStatus: String = "ALL",
    val sort: String = "LATEST",
    val author: String = "ALL",
    val title: String = ""
)
