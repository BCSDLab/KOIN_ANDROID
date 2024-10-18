package `in`.koreatech.koin.data.response.store

import com.google.gson.annotations.SerializedName

data class BenefitCategoryResponse (
    @SerializedName("id") val id: Int,
    @SerializedName("title")  val title: String?,
    @SerializedName("detail") val detail: String?,
    @SerializedName("on_image_url") val onImageUrl: String?,
    @SerializedName("off_image_url")  val offImageUrl: String?,
)

data class BenefitCategoryListResponse(
    @SerializedName("benefits") val benefitCategories: List<BenefitCategoryResponse>
)