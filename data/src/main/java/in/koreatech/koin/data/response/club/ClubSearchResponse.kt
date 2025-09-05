package `in`.koreatech.koin.data.response.club

import com.google.gson.annotations.SerializedName

data class ClubSearchResponse(
    @SerializedName("keywords") val keywords: List<ClubSearchItemResponse>
) {
    data class ClubSearchItemResponse(
        @SerializedName("club_id") val clubId: Int,
        @SerializedName("club_name") val clubName: String
    )
}
