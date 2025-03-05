package `in`.koreatech.koin.feature.lostandfound.enums

import androidx.annotation.StringRes
import `in`.koreatech.koin.feature.lostandfound.R

enum class LostItemCategory(
    val id: Int,
    @StringRes val stringRes: Int,
) {
    CARD(0, R.string.item_type_card),
    ID(1, R.string.item_type_id),
    WALLET(2, R.string.item_type_wallet),
    ELECTRONIC_DEVICE(3, R.string.item_type_electronic_device),
    OTHER(4, R.string.item_type_other),
    NONE(-1, R.string.item_type_none),
    ;

    companion object {
        fun safeValueOf(value: String): LostItemCategory {
            return when (value.trim()) {
                "카드" -> CARD
                "신분증" -> ID
                "지갑" -> WALLET
                "전자제품" -> ELECTRONIC_DEVICE
                "기타" -> OTHER
                else -> NONE
            }
        }

        fun LostItemCategory.getCategoryKoreanWord(): String {
            return when (this) {
                CARD -> "카드"
                ID -> "신분증"
                WALLET -> "지갑"
                ELECTRONIC_DEVICE -> "전자제품"
                OTHER -> "기타"
                NONE -> ""
            }
        }
    }
}
