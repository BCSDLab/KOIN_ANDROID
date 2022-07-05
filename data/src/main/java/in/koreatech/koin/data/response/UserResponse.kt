package `in`.koreatech.koin.data.response

import com.google.gson.annotations.SerializedName

data class UserResponse(
    @SerializedName("credentialsNonExpired") val credentialsNonExpired: Boolean,
    @SerializedName("is_graduated") val isGraduated: Boolean,
    @SerializedName("enabled") val enabled: Boolean,
    @SerializedName("anonymous_nickname") val anonymousNickname: String,
    @SerializedName("portal_account") val portalAccount: String,
    @SerializedName("identity") val identity: Int,
    @SerializedName("name") val name: String,
    @SerializedName("nickname") val nickname: String,
    @SerializedName("accountNonExpired") val accountNonExpired: String,
    @SerializedName("id") val id: String,
    @SerializedName("accountNonLocked") val accountNonLocked: String,
    @SerializedName("username") val username: String
)