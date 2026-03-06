package `in`.koreatech.koin.data.request.callvan

import com.google.gson.annotations.SerializedName

data class CallvanUserReportCreateRequest(
    @SerializedName("reported_user_id")
    val reportedUserId: Int,
    @SerializedName("reasons")
    val reasons: List<CallvanUserReportReasonRequest>
) {
    data class CallvanUserReportReasonRequest(
        @SerializedName("reason_code")
        val reasonCode: String,
        @SerializedName("custom_text")
        val customText: String?
    )
}
