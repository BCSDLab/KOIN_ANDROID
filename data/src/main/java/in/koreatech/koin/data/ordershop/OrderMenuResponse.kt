package `in`.koreatech.koin.data.ordershop

import com.google.gson.annotations.SerializedName

data class OrderMenuResponse(
    @SerializedName("id") val menuId: Int,
    @SerializedName("name") val menuName: String,
    @SerializedName("description") val description: String?,
    @SerializedName("thumbnail_image") val thumbnailImage: String?,
    @SerializedName("is_sold_out") val isSoldOut: Boolean,
    @SerializedName("prices") val prices: List<Price>,
){
    data class Price(
        @SerializedName("id") val priceId: Int,
        @SerializedName("name") val priceName: String?, //null인 경우 단일메뉴
        @SerializedName("price") val price: Int,
    )
}
