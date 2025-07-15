package `in`.koreatech.koin.domain.model.club

data class ClubEvent(
    val id: Int,
    val name: String,
    val imageUrls: List<String>,
    val startDate: String,
    val endDate: String,
    val introduce: String,
    val content: String?,
    val status: String,
    val isSubscribed: Boolean?
)
