package `in`.koreatech.koin.data.response.store

import com.google.gson.annotations.SerializedName
import com.google.gson.annotations.Expose
import java.util.ArrayList

class StoreMenuResponse(
    @SerializedName("id") val id: Int,
    @SerializedName("shop_id") val shopId: Int,
    @SerializedName("name") val name: String,
    @SerializedName("price_type") val priceType: ArrayList<StorePriceTypeResponse>,
    @SerializedName("created_at") val createdAt: String,
    @SerializedName("update_at") val updatedAt: String,
    @SerializedName("size") val size: String,
    @SerializedName("price") val price: String
)