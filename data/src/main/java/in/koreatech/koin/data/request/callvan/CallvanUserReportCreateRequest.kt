package `in`.koreatech.koin.data.request.callvan

import com.google.gson.annotations.SerializedName

data class CallvanUserReportCreateRequest(
    @SerializedName("reported_user_id")
    val reportedUserId: Int,
    @SerializedName("description")
    val description: String?,
    @SerializedName("reasons")
    val reasons: List<CallvanUserReportReasonRequest>,
    @SerializedName("attachments")
    val attachments: List<CallvanUserReportAttachmentRequest>?
) {
    data class CallvanUserReportReasonRequest(
        @SerializedName("reason_code")
        val reasonCode: String,
        @SerializedName("custom_text")
        val customText: String?
    )

    data class CallvanUserReportAttachmentRequest(
        @SerializedName("attachment_type")
        val attachmentType: String,
        @SerializedName("url")
        val url: String
    )
}
