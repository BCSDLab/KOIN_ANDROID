package `in`.koreatech.koin.data.response.notification

import com.google.gson.annotations.SerializedName

data class NotificationPermissionInfoResponse(
    @SerializedName("is_permit")
    val isPermit: Boolean,
    @SerializedName("subscribes")
    val subscribes: List<SubscribesResponse>,
)

data class SubscribesResponse(
    @SerializedName("type")
    val type: String,
    @SerializedName("is_permit")
    val isPermit: Boolean,
    @SerializedName("detail_subscribes")
    val detailSubscribes: List<SubscribeDetailResponse>,
)

data class SubscribeDetailResponse(
    @SerializedName("detail_type")
    val detailType: String,
    @SerializedName("is_permit")
    val isPermit: String,
)