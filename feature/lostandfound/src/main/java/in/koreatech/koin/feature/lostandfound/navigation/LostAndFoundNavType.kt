package `in`.koreatech.koin.feature.lostandfound.navigation
import kotlinx.serialization.Serializable

@Serializable
sealed class LostAndFoundNavType {
    @Serializable
    data object LostAndFoundListRoute : LostAndFoundNavType()

    @Serializable
    data class LostAndFoundKeywordRoute(
        val initialKeywordsCsv: String = ""
    ) : LostAndFoundNavType()

    @Serializable
    data class LostAndFoundDetailRoute(val articleId: Int) : LostAndFoundNavType()

    @Serializable
    data class LostAndFoundReportRoute(val articleId: Int) : LostAndFoundNavType()

    @Serializable
    data class LostAndFoundWriteRoute(val lostOrFoundType: String) : LostAndFoundNavType()

    @Serializable
    data class LostAndFoundModifyRoute(val articleId: Int) : LostAndFoundNavType()
}

const val ARTICLE_ID = "articleId"
const val CHAT_ARTICLE_ID = "article_id"
const val LOST_OR_FOUND_TYPE = "lostOrFoundType"
const val CANCEL_REFRESH_LIST = "cancel_refresh_list"
const val KEY_KEYWORD_LIST = "keyword_list"
