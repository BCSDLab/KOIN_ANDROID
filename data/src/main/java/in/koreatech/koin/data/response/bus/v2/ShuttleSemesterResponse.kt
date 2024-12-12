package `in`.koreatech.koin.data.response.bus.v2

import com.google.gson.annotations.SerializedName
import `in`.koreatech.koin.domain.model.bus.v2.ShuttleSemester
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import java.time.LocalDate

data class ShuttleSemesterResponse(
    @SerializedName("name") val name: String?,
    @SerializedName("from") val from: String?,
    @SerializedName("to") val to: String?,
) {
    fun toShuttleSemester() = ShuttleSemester(
        name = name.orEmpty(),
        from = LocalDate.parse(from.orEmpty()),
        to = LocalDate.parse(to.orEmpty())
    )
}