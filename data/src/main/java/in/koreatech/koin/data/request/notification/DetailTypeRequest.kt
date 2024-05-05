package `in`.koreatech.koin.data.request.notification

import com.google.gson.annotations.SerializedName

data class DetailTypeRequest(
    @SerializedName("detail_type")
    val detailType: String,
)
