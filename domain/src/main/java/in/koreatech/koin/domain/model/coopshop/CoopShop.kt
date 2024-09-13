package `in`.koreatech.koin.domain.model.coopshop

data class CoopShop(
    val id: Int,
    val name: String,
    val semester: String,
    val opens: List<OpenCloseInfo>,
    val phone: String,
    val location: String,
    val remarks: String,
    val updatedAt: String
)