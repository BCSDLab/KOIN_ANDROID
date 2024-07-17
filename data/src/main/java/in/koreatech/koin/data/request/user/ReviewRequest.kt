package `in`.koreatech.koin.data.request.user

import com.google.gson.annotations.SerializedName

class ReviewRequest (
    @SerializedName("rating") val rating: Int,
    @SerializedName("content") val content: String,
    @SerializedName("image_urls") val imageUrls: List<String>?,
    @SerializedName("menu_names") val menuNames: List<String>?,
)