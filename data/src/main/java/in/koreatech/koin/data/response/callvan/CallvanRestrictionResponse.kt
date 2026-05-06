package `in`.koreatech.koin.data.response.callvan

import com.google.gson.annotations.SerializedName

data class CallvanRestrictionResponse(
    @SerializedName("is_restricted")
    val isRestricted: Boolean,
    @SerializedName("restriction_type")
    val restrictionType: String?,
    @SerializedName("restricted_until")
    val restrictedUntil: String?
)
