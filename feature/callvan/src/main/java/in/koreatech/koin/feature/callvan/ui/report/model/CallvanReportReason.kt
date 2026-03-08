package `in`.koreatech.koin.feature.callvan.ui.report.model

enum class CallvanReportReason(val title: String, val description: String) {
    NO_SHOW("노쇼", "참여 신청 후 약속 장소에 나타나지 않았습니다."),
    NON_PAYMENT("비용 미납", "콜밴 비용을 납부하지 않았습니다."),
    PROFANITY("욕설", "욕설, 성적인 언어, 비방하는 언어를 사용했습니다."),
    OTHER("기타", "")
}
