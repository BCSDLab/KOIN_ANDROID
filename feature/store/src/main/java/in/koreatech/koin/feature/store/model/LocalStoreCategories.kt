package `in`.koreatech.koin.feature.store.model

import android.os.Parcelable
import `in`.koreatech.koin.domain.model.store.StoreCategories
import kotlinx.parcelize.Parcelize

@Parcelize
data class LocalStoreCategories(
    val id: Int,
    val imageUrl: String,
    val name: String
) : Parcelable

internal fun StoreCategories.toLocalStoreCategories() = LocalStoreCategories(
    id = id,
    imageUrl = imageUrl.toString(),
    name = name
)
