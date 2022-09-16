package `in`.koreatech.koin.domain.model.dining

data class Dining(
    val id: Int,
    val date: String,
    val type: String,
    val place: String,
    val priceCard: String,
    val priceCash: String,
    val kcal: String,
    val menu: List<String>,
    val createdAt: String,
    val updatedAt: String,
    val error: String
)
