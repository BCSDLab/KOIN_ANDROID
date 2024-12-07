package `in`.koreatech.koin.data.response.bus.v2

import `in`.koreatech.koin.domain.model.bus.v2.ShuttleSemester
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class ShuttleSemesterResponse(
    @SerialName("name") val name: String?,
    @SerialName("term") val term: String?,
) {
    fun toShuttleSemester() = ShuttleSemester(
        name = name.orEmpty(),
        term = term.orEmpty()
    )
}