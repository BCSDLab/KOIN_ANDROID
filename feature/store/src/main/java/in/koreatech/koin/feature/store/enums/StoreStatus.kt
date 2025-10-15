package `in`.koreatech.koin.feature.store.enums

enum class StoreStatus(val isButtonEnabled: Boolean) {
    OPEN(true),
    PRE_OPEN(false),
    SOLD_OUT(false)
}
