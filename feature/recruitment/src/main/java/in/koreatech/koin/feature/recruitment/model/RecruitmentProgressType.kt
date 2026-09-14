package `in`.koreatech.koin.feature.recruitment.model

enum class RecruitmentProgressType(val label: String, val apiValue: String) {
    ONLINE("온라인", "ONLINE"),
    OFFLINE("오프라인", "OFFLINE"),
    HYBRID("온·오프라인", "MIXED");

    companion object {
        fun from(value: String): RecruitmentProgressType =
            entries.firstOrNull { it.apiValue == value } ?: ONLINE
    }
}
