package `in`.koreatech.koin.data.response.recruitment

import com.google.gson.annotations.SerializedName

data class RecruitmentRoleResponse(
    @SerializedName("id")
    val id: Int,
    @SerializedName("name")
    val name: String,
    @SerializedName("current_participants")
    val currentParticipants: Int,
    @SerializedName("max_participants")
    val maxParticipants: Int,
    @SerializedName("is_closed")
    val isClosed: Boolean
)
