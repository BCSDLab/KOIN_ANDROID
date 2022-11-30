package `in`.koreatech.koin.data.response.user

import com.google.gson.annotations.SerializedName

data class UserResponse(
    @SerializedName("id") val id: Int,
    @SerializedName("portal_account") val portalAccount: String,
    @SerializedName("password") val password: String,
    @SerializedName("nickname") val nickname: String?,
    @SerializedName("anonymous_nickname") val anonymousNickname: String?,
    @SerializedName("name") val name: String,
    @SerializedName("student_number") val studentNumber: String?,
    @SerializedName("major") val major: String?,
    @SerializedName("identity") val identity: Int,
    @SerializedName("is_graduated") val isGraduated: Boolean,
    @SerializedName("phone_number") val phoneNumber: String?,
    @SerializedName("gender") val gender: Int,
    @SerializedName("profile_image_url") val profileImageUrl: String?,
    @SerializedName("authority") val authority: String?,
    @SerializedName("enabled") val enabled: Boolean,
    @SerializedName("authorities") val authorities: String?,
    @SerializedName("accountNonLocked") val accountNonLocked: Boolean,
    @SerializedName("credentialsNonExpired") val credentialsNonExpired: Boolean,
    @SerializedName("accountNonExpired") val accountNonExpired: Boolean,
    @SerializedName("username") val username: String
)