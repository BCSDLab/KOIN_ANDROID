package `in`.koreatech.koin.data.request.club

import com.google.gson.annotations.SerializedName

data class ClubCreateRequest(
    @SerializedName("name") val name: String,
    @SerializedName("image_url") val imageUrl: String,
    @SerializedName("club_managers") val clubManagers: List<ClubManagersRequest>,
    @SerializedName("club_category_id") val clubCategoryId: Int,
    @SerializedName("location") val location: String,
    @SerializedName("description") val description: String,
    @SerializedName("instagram") val instagram: String,
    @SerializedName("google_form_url") val googleForm: String,
    @SerializedName("open_chat") val openChat: String,
    @SerializedName("phone_number") val phoneNumber: String,
    @SerializedName("role") val role: String
) {
    data class ClubManagersRequest(
        @SerializedName("user_id") val userId: String
    )
}
