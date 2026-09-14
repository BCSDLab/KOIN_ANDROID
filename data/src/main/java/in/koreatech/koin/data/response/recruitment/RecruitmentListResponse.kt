package `in`.koreatech.koin.data.response.recruitment

import com.google.gson.annotations.SerializedName

data class RecruitmentListResponse(
    @SerializedName("recruitments")
    val recruitments: List<RecruitmentResponse>,
    @SerializedName("total_count")
    val totalCount: Long,
    @SerializedName("current_count")
    val currentCount: Int,
    @SerializedName("total_page")
    val totalPage: Int,
    @SerializedName("current_page")
    val currentPage: Int
)
