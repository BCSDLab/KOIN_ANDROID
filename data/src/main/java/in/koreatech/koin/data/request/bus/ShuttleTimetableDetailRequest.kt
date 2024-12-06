package `in`.koreatech.koin.data.request.bus

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class ShuttleTimetableDetailRequest(
    @SerialName("id") val id: String
)