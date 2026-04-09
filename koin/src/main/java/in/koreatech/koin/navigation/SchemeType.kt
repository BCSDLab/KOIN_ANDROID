package `in`.koreatech.koin.navigation

import `in`.koreatech.koin.feature.article.ArticleActivity
import `in`.koreatech.koin.feature.callvan.CallvanActivity
import `in`.koreatech.koin.feature.chat.ui.groupchat.GroupChatActivity
import `in`.koreatech.koin.feature.chat.ui.room.ChatRoomActivity
import `in`.koreatech.koin.feature.club.ui.ClubActivity
import `in`.koreatech.koin.feature.dining.ui.DiningActivity
import `in`.koreatech.koin.feature.lostandfound.ui.LostAndFoundActivity
import `in`.koreatech.koin.feature.store.StoreActivity

enum class SchemeType(
    val type: String,
    val className: Class<*>
) {
    SHOP("shop", StoreActivity::class.java),
    DINING("dining", DiningActivity::class.java),
    ARTICLE("keyword", ArticleActivity::class.java),
    CHAT("chat", ChatRoomActivity::class.java),
    CLUB_RECRUIT("club-recruitment", ClubActivity::class.java),
    CLUB("club", ClubActivity::class.java),
    LOST_AND_FOUND("articles/lost-item", LostAndFoundActivity::class.java),
    CALLVAN("callvan", CallvanActivity::class.java),
    CALLVAN_CHAT("callvan-chat", GroupChatActivity::class.java),
    ;

    companion object {
        fun fromType(type: String?): SchemeType? {
            return entries.find { it.type == type }
        }
    }
}
