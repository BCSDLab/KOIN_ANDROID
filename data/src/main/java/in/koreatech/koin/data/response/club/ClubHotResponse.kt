package `in`.koreatech.koin.data.response.club

import com.google.gson.annotations.SerializedName

data class ClubHotResponse(
    @SerializedName("club_id")
    val clubId: Int,
    @SerializedName("name")
    val name: String,
    @SerializedName("image_url")
    val imageUrl: String
)
