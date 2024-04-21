package `in`.koreatech.koin.domain.model.notification

data class NotificationPermissionInfo(
    val isPermit: Boolean,
    val subscribes: List<Subscribes>,
)

data class Subscribes(
    val type: SubscribesType,
    val isPermit: Boolean,
) {
    companion object {
        const val SHOP_EVENT = "SHOP_EVENT"
        const val DINING_SOLD_OUT = "DINING_SOLD_OUT"
    }
}

enum class SubscribesType{
    SHOP_EVENT, DINING_SOLD_OUT, NOTHING
}