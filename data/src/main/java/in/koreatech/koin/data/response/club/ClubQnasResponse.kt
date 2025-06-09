package `in`.koreatech.koin.data.response.club

import com.google.gson.annotations.SerializedName

data class ClubQnasResponse(
    @SerializedName("root_count") val rootCount: Int,
    @SerializedName("total_count") val totalCount: Int,
    @SerializedName("qnas") val qnas: List<QnaResponse>
) {
    data class QnaResponse(
        @SerializedName("id") val id: Int,
        @SerializedName("author_id") val authorId: Int,
        @SerializedName("nickname") val nickname: String,
        @SerializedName("content") val content: String,
        @SerializedName("created_at") val createdAt: String,
        @SerializedName("children") val children: List<QnaResponse>
    )
}
