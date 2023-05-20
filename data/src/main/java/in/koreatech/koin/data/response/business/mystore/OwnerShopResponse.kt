package `in`.koreatech.koin.data.response.business.mystore

import com.google.gson.annotations.SerializedName

data class OwnerShopResponse (
    // 가게 주소
    @SerializedName("address") val address: String,
    // 배달 가능 여부
    @SerializedName("delivery") val delivery: Boolean,
    // 배달 가격
    @SerializedName("delivery_price") val deliveryPrice: Int,
    // 설명
    @SerializedName("description") val desc: String,
    // 고유 번호
    @SerializedName("id") val id: Int,
    // 가게 대표 이미지 (문자열로 여러 개)
    @SerializedName("image_urls") val imageUrls: List<String>?,
    // 메뉴 카테고리 (이벤트, 대표 ...)
    @SerializedName("menu_categories") val menuCategories: List<Map<String, Any>>,
    // 가게 이름
    @SerializedName("name") val name: String,
    // 오픈 정보 (close_time: "02:00", closed: false, day_of_week: "Monday", open_time: "16:00"
    @SerializedName("open") val open: List<Map<String,Any>>,
    // 계좌 이체 가능
    @SerializedName("pay_bank") val payBank: Boolean,
    // 카드 가능
    @SerializedName("pay_card") val payCard: Boolean,
    // 연락처
    @SerializedName("phone") val phone: String,
    // 가게 카테고리 (ex. 중국집)
    @SerializedName("shop_categories") val shopCategories: List<Map<String, Any>>
)