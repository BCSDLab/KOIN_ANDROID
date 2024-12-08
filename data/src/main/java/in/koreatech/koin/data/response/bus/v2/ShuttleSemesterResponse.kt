package `in`.koreatech.koin.data.response.bus.v2

import com.google.gson.annotations.SerializedName
import `in`.koreatech.koin.domain.model.bus.v2.ShuttleSemester
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

data class ShuttleSemesterResponse(
    @SerializedName("name") val name: String?,
    @SerializedName("term") val term: String?,
) {
    fun toShuttleSemester() = ShuttleSemester(
        name = name.orEmpty(),
        term = term.orEmpty()
    )
}