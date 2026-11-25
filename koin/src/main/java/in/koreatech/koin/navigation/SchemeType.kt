package `in`.koreatech.koin.navigation

import `in`.koreatech.koin.core.navigation.ChatDomain
import `in`.koreatech.koin.feature.callvan.CallvanActivity
import `in`.koreatech.koin.feature.chat.ui.ChatActivity
import `in`.koreatech.koin.feature.club.ui.ClubActivity
import `in`.koreatech.koin.feature.dining.ui.DiningActivity
import `in`.koreatech.koin.feature.lostandfound.ui.LostAndFoundActivity
import `in`.koreatech.koin.feature.store.StoreActivity
import `in`.koreatech.koin.ui.main.activity.MainActivity

enum class SchemeType(
    val type: String,
    val className: Class<*>,
    val chatDomainType: ChatDomain? = null
) {
    SHOP("shop", StoreActivity::class.java),
    DINING("dining", DiningActivity::class.java),
    ARTICLE("keyword", MainActivity::class.java),
    CHAT("chat", ChatActivity::class.java, ChatDomain.LOST_ITEM),
    CLUB_RECRUIT("club-recruitment", ClubActivity::class.java),
    CLUB("club", ClubActivity::class.java),
    LOST_AND_FOUND("articles/lost-item", LostAndFoundActivity::class.java),
    CALLVAN("callvan", CallvanActivity::class.java),
    CALLVAN_CHAT("callvan-chat", ChatActivity::class.java, ChatDomain.CALLVAN)
    ;

    companion object {
        fun fromType(type: String?): SchemeType? {
            return entries.find { it.type == type }
        }
    }
}
