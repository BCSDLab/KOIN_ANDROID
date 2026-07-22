package `in`.koreatech.koin.domain.model.dining

data class DiningWithOperationTime(
    val id: Int,
    val date: String,
    val type: String,
    val place: String,
    val priceCard: String,
    val priceCash: String,
    val kcal: String,
    val menu: List<String>,
    val imageUrl: String,
    val soldOutAt: String,
    val startTime: String,
    val endTime: String
)
