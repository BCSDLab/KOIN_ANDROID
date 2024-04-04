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
    val imageUrl: String,
    val createdAt: String,
    val updatedAt: String,
    val isSoldOut: Boolean,
    // TODO: sold_out 품절 시각으로 변경시 대응
//    val soldOutAt: String,
    val isChanged: Boolean,
    val error: String
)
