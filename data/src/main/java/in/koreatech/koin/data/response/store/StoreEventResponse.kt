package `in`.koreatech.koin.data.response.store

import com.google.gson.annotations.SerializedName

data class StoreEventResponse(
    @SerializedName("shops") val shops: List<StoreEventItemReponse>
)