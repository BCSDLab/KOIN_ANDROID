package `in`.koreatech.koin.feature.recruitment.model

import androidx.compose.runtime.Immutable

@Immutable
data class RecruitmentRoleModel(
    val id: Int,
    val name: String,
    val currentParticipants: Int = 0,
    val maxParticipants: Int = 0,
    val isClosed: Boolean = false
)
