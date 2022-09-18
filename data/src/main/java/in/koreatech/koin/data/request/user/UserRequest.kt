package `in`.koreatech.koin.data.request.user

import com.google.gson.annotations.SerializedName

data class UserRequest(
    @SerializedName("id") val id: Int,
    @SerializedName("portal_account") val portalAccount: String,
    @SerializedName("nickname") val nickname: String?,
    @SerializedName("anonymous_nickname") val anonymousNickname: String?,
    @SerializedName("name") val name: String?,
    @SerializedName("student_number") val studentNumber: String?,
    @SerializedName("major") val major: String?,
    @SerializedName("identity") val identity: Int,
    @SerializedName("is_graduated") val isGraduated: Boolean,
    @SerializedName("phone_number") val phoneNumber: String?,
    @SerializedName("gender") val gender: Int?,
    @SerializedName("profile_image_url") val profileImageUrl: String?
)