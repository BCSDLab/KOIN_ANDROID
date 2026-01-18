package `in`.koreatech.koin.feature.lostandfound.navigation
import kotlinx.serialization.Serializable

@Serializable
sealed class LostAndFoundNavType {
    @Serializable
    data object LostAndFoundListRoute : LostAndFoundNavType()

    @Serializable
    data class LostAndFoundDetailRoute(val articleId: Int) : LostAndFoundNavType()

    @Serializable
    data class LostAndFoundReportRoute(val articleId: Int) : LostAndFoundNavType()

    @Serializable
    data object LostAndFoundWriteRoute : LostAndFoundNavType()
}
