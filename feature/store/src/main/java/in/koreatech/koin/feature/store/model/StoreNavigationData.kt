package `in`.koreatech.koin.feature.store.model

import android.os.Bundle
import androidx.navigation.NavType
import kotlinx.serialization.Serializable
import kotlinx.serialization.json.Json

@Serializable
data class StoreNavigationData(
    val shopId: Int,
    val orderableShopId: Int,
    val isOrderableShop: Boolean
)

val StoreNavigationDataType = object : NavType<StoreNavigationData>(isNullableAllowed = false) {
    override fun get(bundle: Bundle, key: String): StoreNavigationData? {
        return bundle.getString(key)?.let { Json.decodeFromString(it) }
    }

    override fun parseValue(value: String): StoreNavigationData {
        return Json.decodeFromString(value)
    }

    override fun put(bundle: Bundle, key: String, value: StoreNavigationData) {
        bundle.putString(key, Json.encodeToString(StoreNavigationData.serializer(), value))
    }

    override fun serializeAsValue(value: StoreNavigationData): String {
        return Json.encodeToString(StoreNavigationData.serializer(), value)
    }
}
