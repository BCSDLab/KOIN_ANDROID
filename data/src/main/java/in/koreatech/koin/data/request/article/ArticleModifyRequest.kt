package `in`.koreatech.koin.data.request.article

import com.google.gson.annotations.SerializedName

data class ArticleModifyRequest(
    @SerializedName("category") val category: String,
    @SerializedName("found_place") val foundPlace: String,
    @SerializedName("found_date") val foundDate: String,
    @SerializedName("content") val content: String?,
    @SerializedName("new_image") val newImage: List<String>?,
    @SerializedName("delete_image_ids") val deleteImageIds: List<String>?
)