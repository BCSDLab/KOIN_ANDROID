package `in`.koreatech.koin.feature.recruitment.model

enum class RecruitmentCategory(val label: String) {
    CONTEST("공모전"),
    EXTERNAL_ACTIVITY("대외활동"),
    STUDY("스터디"),
    PROJECT("프로젝트"),
    ETC("기타")
}

fun String.toRecruitmentCategory(): RecruitmentCategory =
    RecruitmentCategory.entries.firstOrNull { it.name == this } ?: RecruitmentCategory.ETC

fun String.toRecruitmentLocation(): String = when (this) {
    "ONLINE" -> "온라인"
    "OFFLINE" -> "오프라인"
    else -> "온·오프라인"
}

fun String.toDateRange(endDate: String): String =
    "${replace("-", ".")} ~ ${endDate.replace("-", ".")}"
