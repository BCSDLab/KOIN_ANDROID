package `in`.koreatech.koin.data.response.store

import com.google.gson.annotations.SerializedName

data class StoreRegisterResponse (
    @SerializedName("address") val address: String,
    @SerializedName("category_ids") val categoryIds: List<Int>,
    @SerializedName("delivery") val delivery: Boolean, // 배달가능
    @SerializedName("delivery_price") val delivery_price: Int,
    @SerializedName("description") val description: String,
    @SerializedName("image_urls") val imageUrls: List<String>, // 이미지
    @SerializedName("name") val name: String, // 가게이름
    @SerializedName("open") val open: List<StoreDayOffResponse>?, // 가게 휴점 시간
    @SerializedName("pay_bank") val payBank: Boolean, // 계좌이체 가능
    @SerializedName("pay_card") val payCard: Boolean, // 카드결제 가능
    @SerializedName("phone") val phone: String // 전화번호
)
