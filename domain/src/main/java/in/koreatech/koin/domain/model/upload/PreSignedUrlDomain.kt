package `in`.koreatech.koin.domain.model.upload

enum class PreSignedUrlDomain(val domain: String) {
    OWNERS("owners"),
    MARKET("market"),
    LOST_AND_FOUND("lost_items"),
    CLUB("club"),
    CALLVAN_REPORT("callvan_report"),
    CALLVAN_CHAT("callvan_chat"),
    TEAM_RECRUITMENT("team_recruitment")
}
