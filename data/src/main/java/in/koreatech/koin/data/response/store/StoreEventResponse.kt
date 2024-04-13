package `in`.koreatech.koin.data.response.store

import com.google.gson.annotations.SerializedName

data class StoreEventResponse(
    @SerializedName("events") val events: List<StoreEventItemReponse>
)