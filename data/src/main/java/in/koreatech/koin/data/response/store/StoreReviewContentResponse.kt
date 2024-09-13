package `in`.koreatech.koin.data.response.store

import com.google.gson.annotations.SerializedName

data class StoreReviewContentResponse(
    @SerializedName("review_id") val reviewId: Int,
    @SerializedName("rating") val rating: Int,
    @SerializedName("nick_name") val nickName: String,
    @SerializedName("content") val content: String,
    @SerializedName("image_urls") val imageUrls: List<String>,
    @SerializedName("menu_names") val menuNames: List<String>,
    @SerializedName("is_mine") val isMine: Boolean,
    @SerializedName("is_modified") val isModified: Boolean,
    @SerializedName("is_reported") val isReported: Boolean,
    @SerializedName("created_at") val createdAt: String
)