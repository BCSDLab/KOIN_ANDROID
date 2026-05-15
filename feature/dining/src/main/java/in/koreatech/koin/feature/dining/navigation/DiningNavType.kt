package `in`.koreatech.koin.feature.dining.navigation

import kotlinx.serialization.Serializable

@Serializable
sealed class DiningNavType {
    @Serializable
    data class DiningDetail(
        val initDate: String = "",
        val initTabType: Int = -1
    ) : DiningNavType()

    @Serializable
    data object DiningNotice : DiningNavType()
}
