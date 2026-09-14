package `in`.koreatech.koin.feature.recruitment.model

import java.time.LocalDate

data class RecruitmentActivityEntry(
    val id: Long,
    val name: String,
    val startDate: LocalDate,
    val endDate: LocalDate?,
    val isOngoing: Boolean = false,
    val content: String
) {
    companion object {
        const val CONTENT_MAX_LENGTH = 1000
    }
}
