package `in`.koreatech.koin.data.response.store

import com.google.gson.annotations.SerializedName

data class StoreCategoriesItemResponse(
    @SerializedName("id") val id: Int,
    @SerializedName("image_url") val image_url: String,
    @SerializedName("name") val name: String
)
