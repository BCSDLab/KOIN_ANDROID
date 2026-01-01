package `in`.koreatech.koin.feature.store.model

import android.os.Parcelable
import `in`.koreatech.koin.domain.model.store.AddCartItemOption
import kotlinx.parcelize.Parcelize
import kotlinx.serialization.Serializable

@Serializable
data class LocalCartAdd(
    val orderableShopId: Int,
    val orderableShopMenuId: Int,
    val orderableShopMenuPriceId: Int,
    val orderableShopMenuOptionIds: List<LocalAddCartItemOption>,
    val quantity: Int
)

@Parcelize
@Serializable
data class LocalAddCartItemOption(
    val optionGroupId: Int,
    val optionId: Int
) : Parcelable

fun List<LocalAddCartItemOption>.toAddCartItemOption() = map {
    AddCartItemOption(
        optionGroupId = it.optionGroupId,
        optionId = it.optionId
    )
}
