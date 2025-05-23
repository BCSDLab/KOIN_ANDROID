package `in`.koreatech.koin.data.request.club

import com.google.gson.annotations.SerializedName

data class ClubQnaRequest(
    @SerializedName("parent_id") val parentId: Int?,
    @SerializedName("content") val content: String
)
