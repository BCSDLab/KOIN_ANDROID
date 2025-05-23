package `in`.koreatech.koin.data.request.club

import com.google.gson.annotations.SerializedName

data class ClubEmpowermentRequest(
    @SerializedName("club_id") val clubId: Int,
    @SerializedName("changed_manager_id") val changedManagerId: String
)
