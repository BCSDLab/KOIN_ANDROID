package `in`.koreatech.koin.feature.store.home.model

import androidx.compose.runtime.Immutable

@Immutable
enum class OrderStatus {
    NONE,
    CONFIRMING,
    DELIVERING,
    PACKAGED;

    companion object {
        fun fromString(value: String) = when (value) {
            "CONFIRMING" -> CONFIRMING
            "DELIVERING" -> DELIVERING
            "PACKAGED" -> PACKAGED
            else -> NONE
        }
    }
}
