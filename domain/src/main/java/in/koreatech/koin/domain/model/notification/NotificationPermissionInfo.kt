package `in`.koreatech.koin.domain.model.notification

data class NotificationPermissionInfo(
    val isPermit: Boolean,
    val subscribes: List<Subscribes>
)

data class Subscribes(
    val type: SubscribesType,
    val isPermit: Boolean,
    val detailSubscribes: List<SubscribesDetail>
) {
    companion object {
        const val SHOP_EVENT = "SHOP_EVENT"
        const val DINING_SOLD_OUT = "DINING_SOLD_OUT"
        const val DINING_IMAGE_UPLOAD = "DINING_IMAGE_UPLOAD"
        const val ARTICLE_KEYWORD = "ARTICLE_KEYWORD"
        const val REVIEW_PROMPT = "REVIEW_PROMPT"
        const val LOST_ITEM_CHAT = "LOST_ITEM_CHAT"
    }
}

data class SubscribesDetail(
    val type: SubscribesDetailType,
    val isPermit: Boolean
) {
    companion object {
        const val BREAKFAST = "BREAKFAST"
        const val LUNCH = "LUNCH"
        const val DINNER = "DINNER"
    }
}

enum class SubscribesType {
    SHOP_EVENT,
    DINING_SOLD_OUT,
    DINING_IMAGE_UPLOAD,
    NOTHING,
    ARTICLE_KEYWORD,
    REVIEW_PROMPT,
    LOST_ITEM_CHAT
}

enum class SubscribesDetailType {
    BREAKFAST,
    LUNCH,
    DINNER,
    NOTHING
}
