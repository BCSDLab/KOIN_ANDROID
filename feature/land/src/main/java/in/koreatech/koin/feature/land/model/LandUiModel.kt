package `in`.koreatech.koin.feature.land.model

import androidx.compose.runtime.Immutable
import `in`.koreatech.koin.domain.model.land.Land
import kotlinx.collections.immutable.ImmutableList
import kotlinx.collections.immutable.persistentListOf

@Immutable
internal data class LandUiModel(
    val id: Int,
    val name: String,
    val roomType: String,
    val address: String,
    val latitude: Double,
    val longitude: Double,
    val monthlyFee: String?,
    val charterFee: String?,
    val isMonthlyAvailable: Boolean,
    val isCharterAvailable: Boolean
)

internal fun List<Land>.toUiModels(): ImmutableList<LandUiModel> =
    mapTo(persistentListOf<LandUiModel>().builder()) { land ->
        LandUiModel(
            id = land.id,
            name = land.name,
            roomType = land.roomType,
            address = land.address,
            latitude = land.latitude,
            longitude = land.longitude,
            monthlyFee = land.monthlyFee,
            charterFee = land.charterFee,
            isMonthlyAvailable = land.isMonthlyAvailable,
            isCharterAvailable = land.isCharterAvailable
        )
    }.build()
