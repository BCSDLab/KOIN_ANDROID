package `in`.koreatech.koin.feature.land.navigation

import kotlinx.serialization.Serializable

@Serializable
internal sealed class LandNavType {
    @Serializable
    data object LandMain : LandNavType()

    @Serializable
    data object LandSearch : LandNavType()

    @Serializable
    data class LandDetail(
        val landId: Int,
        val isMonthlyAvailable: Boolean,
        val isCharterAvailable: Boolean
    ) : LandNavType()
}
