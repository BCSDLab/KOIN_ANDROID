package `in`.koreatech.koin.domain.model.banner

data class Banner(
    val id: Int,
    val title: String,
    val imageUrl: String,
    val redirectLink: String?,
    val version: String?
)
