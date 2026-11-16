package `in`.koreatech.koin.feature.land.model

import androidx.compose.runtime.Immutable
import `in`.koreatech.koin.domain.model.land.LandDetail
import kotlinx.collections.immutable.ImmutableList
import kotlinx.collections.immutable.persistentListOf

@Immutable
internal data class LandDetailUiModel(
    val id: Int,
    val name: String,
    val roomType: String,
    val address: String,
    val latitude: Double,
    val longitude: Double,
    val size: String,
    val phone: String,
    val monthlyFee: String,
    val charterFee: String,
    val deposit: String,
    val managementFee: String,
    val imageUrls: ImmutableList<String>
)

internal fun LandDetail.toUiModel(): LandDetailUiModel {
    val photos = persistentListOf<String>().builder()
    for (url in imageUrls) {
        if (url.isNotBlank()) photos.add(url)
    }
    return LandDetailUiModel(
        id = id,
        name = name,
        roomType = roomType,
        address = address,
        latitude = latitude,
        longitude = longitude,
        size = size,
        phone = phone,
        monthlyFee = monthlyFee,
        charterFee = charterFee,
        deposit = deposit,
        managementFee = managementFee,
        imageUrls = photos.build()
    )
}
