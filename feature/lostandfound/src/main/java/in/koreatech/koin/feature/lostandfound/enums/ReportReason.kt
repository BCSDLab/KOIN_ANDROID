package `in`.koreatech.koin.feature.lostandfound.enums

enum class ReportReason(val title: String, val description: String) {
    OFF_TOPIC("주제에 맞지 않음", "분실물과 관련 없는 글입니다."),
    SPAM("스팸", "광고가 포함된 글입니다."),
    INAPPROPRIATE_LANGUAGE("욕설", "욕설, 성적인 언어, 비방하는 글이 포함된 글입니다."),
    PRIVACY("개인정보", "개인정보가 포함된 글입니다."),
    OTHER("기타", "")
}
