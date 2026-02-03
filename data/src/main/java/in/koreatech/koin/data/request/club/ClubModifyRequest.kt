package `in`.koreatech.koin.data.request.club

import com.google.gson.annotations.SerializedName

data class ClubModifyRequest(
    @SerializedName("name") val name: String,
    @SerializedName("image_url") val imageUrl: String,
    @SerializedName("club_category_id") val clubCategoryId: Int,
    @SerializedName("location") val location: String,
    @SerializedName("description") val description: String?,
    @SerializedName("instagram") val instagram: String?,
    @SerializedName("google_form_url") val googleForm: String?,
    @SerializedName("open_chat") val openChat: String?,
    @SerializedName("phone_number") val phoneNumber: String?,
    @SerializedName("is_like_hidden") val isLikeHidden: Boolean
)
