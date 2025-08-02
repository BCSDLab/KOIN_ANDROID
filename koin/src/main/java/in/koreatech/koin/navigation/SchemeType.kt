package `in`.koreatech.koin.navigation

import `in`.koreatech.koin.feature.chat.ui.room.ChatRoomActivity
import `in`.koreatech.koin.feature.club.ui.ClubActivity
import `in`.koreatech.koin.ui.article.ArticleActivity
import `in`.koreatech.koin.ui.dining.DiningActivity
import `in`.koreatech.koin.ui.store.activity.StoreActivity

enum class SchemeType(
    val type: String,
    val className: Class<*>
) {
    SHOP("shop", StoreActivity::class.java),
    DINING("dining", DiningActivity::class.java),
    ARTICLE("keyword", ArticleActivity::class.java),
    CHAT("chat", ChatRoomActivity::class.java),
    CLUB_RECRUIT("club-recruitment", ClubActivity::class.java),
    CLUB("club", ClubActivity::class.java);

    companion object {
        fun fromType(type: String?): SchemeType? {
            return entries.find { it.type == type }
        }
    }
}
