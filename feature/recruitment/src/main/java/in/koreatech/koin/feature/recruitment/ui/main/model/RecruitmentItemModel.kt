package `in`.koreatech.koin.feature.recruitment.ui.main.model

import androidx.compose.runtime.Immutable
import `in`.koreatech.koin.feature.recruitment.model.RecruitmentCategory
import `in`.koreatech.koin.feature.recruitment.model.RecruitmentLocation
import `in`.koreatech.koin.feature.recruitment.model.RecruitmentStatus
import kotlinx.collections.immutable.ImmutableList
import kotlinx.collections.immutable.persistentListOf

@Immutable
data class RecruitmentItemModel(
    val id: Int,
    val category: RecruitmentCategory,
    val status: RecruitmentStatus,
    val dDay: Int,
    val title: String,
    val roles: ImmutableList<String> = persistentListOf(),
    val location: RecruitmentLocation,
    val period: String,
    val currentCount: Int,
    val maxCount: Int
) {
    val isFull: Boolean get() = currentCount >= maxCount
}
