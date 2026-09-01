package `in`.koreatech.koin.feature.recruitment.ui.recruitmentmodify.model

data class RecruitmentModifyRole(
    val name: String = "",
    val count: Int = 1,
    val id: String = java.util.UUID.randomUUID().toString()
) {
    companion object {
        const val NAME_MAX_LENGTH = 10
        const val MAX_ROLE_COUNT = 5
        const val MIN_MEMBER_COUNT = 1
        const val MAX_MEMBER_COUNT = 99
    }
}
