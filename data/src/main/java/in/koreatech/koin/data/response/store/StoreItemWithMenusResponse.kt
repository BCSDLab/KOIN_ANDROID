package `in`.koreatech.koin.data.response.store

import com.google.gson.annotations.SerializedName

data class StoreItemWithMenusResponse (
    //Unique ID
    @SerializedName("id") val uid: Int,
    //가게 이름
    @SerializedName("name") val name: String,
    //chosung, 가게 이름 앞자리 1글자의 초성
    @SerializedName("chosung") val chosung: String,
    //종류
    //기타(S000), 콜벤(S001), 정식(S002), 족발(S003), 중국집(S004), 치킨(S005), 피자(S006), 탕수육(S007), 일반(S008), 미용실(S009), 카페(S010)
    @SerializedName("category") val category: String,
    //전화번호 010-0000-0000
    @SerializedName("phone") val phone: String?,
    //오픈 시간 ex) 11:00
    @SerializedName("open_time") val openTime: String?,
    //마감 시간 ex) 11:00
    @SerializedName("close_time") val closeTime: String?,
    //주소
    @SerializedName("address") val address: String?,
    //세부사항
    @SerializedName("description") val description: String?,
    //배달 여부
    @SerializedName("delivery") val isDeliveryOk: Boolean?,
    //배달 금액
    @SerializedName("delivery_price") val deliveryPrice: Int?,
    //카드 여부
    @SerializedName("pay_card") val isCardOk: Boolean?,
    //계좌이체 여부
    @SerializedName("pay_bank") val isBankOk: Boolean?,
    //이미지 링크  '[ A : B ]'
    @SerializedName("image_urls") val imageUrls: ArrayList<String>?,
    //생성 날짜 example: 2018-03-21 16:40:57
    @SerializedName("created_at") val createdAt: String,
    //업데이트 날짜
    @SerializedName("updated_at") val updatedAt: String,
    @SerializedName("error") val error: String,
    @SerializedName("shopId") val shopId: Int,
    @SerializedName("size") val size: String,
    @SerializedName("price") val price: String,
    @SerializedName("detail") val detail: String,
    @SerializedName("hit") val hit: String,
    @SerializedName("menus") val menus: List<StoreMenuResponse>,
)