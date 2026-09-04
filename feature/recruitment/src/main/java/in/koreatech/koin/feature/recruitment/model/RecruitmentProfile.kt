package `in`.koreatech.koin.feature.recruitment.model

import kotlinx.collections.immutable.ImmutableList
import kotlinx.collections.immutable.persistentListOf

data class RecruitmentProfile(
    val nickname: String = "",
    val department: String = "",
    val studentId: String = "",
    val preferredRole: String = "",
    val skills: ImmutableList<String> = persistentListOf(),
    val activities: ImmutableList<RecruitmentActivityEntry> = persistentListOf(),
    val region: String = "",
    val availableTime: String = "",
    val selfIntroduction: String = ""
)
