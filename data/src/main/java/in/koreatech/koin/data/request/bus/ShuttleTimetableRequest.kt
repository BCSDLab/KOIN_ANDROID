package `in`.koreatech.koin.data.request.bus

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class ShuttleTimetableRequest(
    @SerialName("id") val id: String
)