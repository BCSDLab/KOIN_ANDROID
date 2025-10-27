package `in`.koreatech.koin.feature.store.model

import android.os.Bundle
import androidx.navigation.NavType
import kotlinx.serialization.Serializable
import kotlinx.serialization.json.Json

@Serializable
data class StoreReviewNavigationData(
    val shopId: Int,
    val orderableShopId: Int,
    val isOrderableShop: Boolean
)

val StoreReviewNavigationDataType = object : NavType<StoreReviewNavigationData>(isNullableAllowed = false) {
    override fun get(bundle: Bundle, key: String): StoreReviewNavigationData? {
        return bundle.getString(key)?.let { Json.decodeFromString(it) }
    }

    override fun parseValue(value: String): StoreReviewNavigationData {
        return Json.decodeFromString(value)
    }

    override fun put(bundle: Bundle, key: String, value: StoreReviewNavigationData) {
        bundle.putString(key, Json.encodeToString(StoreReviewNavigationData.serializer(), value))
    }

    override fun serializeAsValue(value: StoreReviewNavigationData): String {
        return Json.encodeToString(StoreReviewNavigationData.serializer(), value)
    }
}
