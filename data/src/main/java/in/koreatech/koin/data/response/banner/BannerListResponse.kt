package `in`.koreatech.koin.data.response.banner

import com.google.gson.annotations.SerializedName

data class BannerListResponse(
    @SerializedName("count") val count: Int,
    @SerializedName("banners") val banners: List<BannerResponse>
) {
    data class BannerResponse(
        @SerializedName("id") val id: Int,
        @SerializedName("image_url") val imageUrl: String,
        @SerializedName("redirect_link") val redirectLink: String?,
        @SerializedName("version") val version: String?
    )
}
