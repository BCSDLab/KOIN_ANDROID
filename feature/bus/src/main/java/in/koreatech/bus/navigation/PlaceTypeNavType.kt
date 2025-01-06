package `in`.koreatech.bus.navigation

import android.net.Uri
import android.os.Bundle
import androidx.core.os.BundleCompat
import androidx.navigation.NavType
import `in`.koreatech.bus.type.PlaceType
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json

internal object PlaceTypeNavType : NavType<PlaceType>(
    isNullableAllowed = true
) {
    override fun get(bundle: Bundle, key: String): PlaceType {
        return BundleCompat.getSerializable(bundle, key, PlaceType::class.java)!!
    }

    override fun parseValue(value: String): PlaceType {
        return Json.decodeFromString<PlaceType>(value)
    }

    override fun put(bundle: Bundle, key: String, value: PlaceType) {
        bundle.putSerializable(key, value)
    }

    override fun serializeAsValue(value: PlaceType): String {
        return Uri.encode(Json.encodeToString(value))
    }
}