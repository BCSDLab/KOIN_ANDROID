package `in`.koreatech.koin.data.response.business

import com.google.gson.annotations.SerializedName

data class MyStoreRegisterResponse(
    @SerializedName("address") val address: String,
    @SerializedName("category_ids") val categoryIds: List<Int>, // 현재는 String으로 되어있음
    @SerializedName("delivery") val delivery: Boolean, // 배달가능
    @SerializedName("delivery_price") val delivery_price: Int,
    @SerializedName("image_urls") val imageUrls: List<String>, // 이미지
    @SerializedName("name") val name: String, // 가게이름

    @SerializedName("open") val open: List<MyStoreDayOffReponse>?, // 가게 휴점 시간

    @SerializedName("pay_bank") val payBank: Boolean, // 계좌이체 가능
    @SerializedName("pay_card") val payCard: Boolean, // 카드결제 가능
    @SerializedName("phone") val phone: String // 전화번호
)
