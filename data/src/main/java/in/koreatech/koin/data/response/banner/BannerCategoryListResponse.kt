package `in`.koreatech.koin.data.response.banner

import com.google.gson.annotations.SerializedName

data class BannerCategoryListResponse (
    @SerializedName("banner_categories") val categories: List<BannerCategory>
) {
    data class BannerCategory (
        @SerializedName("id") val id: Int?,
        @SerializedName("name") val name: String?
    )
}