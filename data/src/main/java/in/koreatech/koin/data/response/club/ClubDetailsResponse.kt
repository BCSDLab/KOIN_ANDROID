package `in`.koreatech.koin.data.response.club

import com.google.gson.annotations.SerializedName

data class ClubDetailsResponse(
    @SerializedName("id") val id: Int,
    @SerializedName("name") val name: String,
    @SerializedName("category") val category: String,
    @SerializedName("location") val location: String,
    @SerializedName("image_url") val imageUrl: String,
    @SerializedName("likes") val likes: Int,
    @SerializedName("description") val description: String,
    @SerializedName("introduction") val introduction: String,
    @SerializedName("instagram") val instagram: String?,
    @SerializedName("google_form") val googleForm: String?,
    @SerializedName("open_chat") val openChat: String?,
    @SerializedName("phone_number") val phoneNumber: String?,
    @SerializedName("manager") val manager: Boolean,
    @SerializedName("is_liked") val isLiked: Boolean,
    @SerializedName("is_recruit_subscribed") val isRecruitSubscribed: Boolean,
    @SerializedName("updated_at") val updatedAt: String,
    @SerializedName("is_like_hidden") val isLikeHidden: Boolean,
    @SerializedName("hot_status") val hotStatus: HotStatusResponse?
) {
    data class HotStatusResponse(
        @SerializedName("month") val month: Int,
        @SerializedName("weekOfMonth") val weekOfMonth: Int,
        @SerializedName("streakCount") val streakCount: Int
    )
}
