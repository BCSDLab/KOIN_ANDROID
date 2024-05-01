package `in`.koreatech.koin.data.response.store

import com.google.gson.annotations.SerializedName

data class StoreItemResponse(
    @SerializedName("id") val uid: Int,
    @SerializedName("name") val name: String,
    @SerializedName("phone") val phone: String?,
    @SerializedName("delivery") val isDeliveryOk: Boolean?,
    @SerializedName("pay_card") val isCardOk: Boolean?,
    @SerializedName("pay_bank") val isBankOk: Boolean?,
    @SerializedName("open") val open: List<OpenResponseDTO>,
    @SerializedName("category_ids") val categoryIds: List<Int>,
    @SerializedName("is_event") val  isEvent: Boolean?,
    @SerializedName("is_open") val  isOpen: Boolean?,
) {
    data class OpenResponseDTO(
        @SerializedName("day_of_week")
        val dayOfWeek: String,
        @SerializedName("closed")
        val closed: Boolean,
        @SerializedName("open_time")
        val openTime: String?,
        @SerializedName("close_time")
        val closeTime: String?,
    )
}
