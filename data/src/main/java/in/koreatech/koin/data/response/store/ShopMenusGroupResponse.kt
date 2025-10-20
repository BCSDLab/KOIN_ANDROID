package `in`.koreatech.koin.data.response.store

import com.google.gson.annotations.SerializedName

data class ShopMenusGroupResponse(
    @SerializedName("count") val count: Int,
    @SerializedName("menuGroups") val menuGroups: List<ShopMenuGroupResponse>
) {
    data class ShopMenuGroupResponse(
        @SerializedName("id") val id: Int,
        @SerializedName("name") val name: String
    )
}
