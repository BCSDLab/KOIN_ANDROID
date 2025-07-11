package `in`.koreatech.koin.feature.club.ui.clubrecruitmodify

import android.os.Parcelable
import java.time.LocalDate
import kotlinx.parcelize.Parcelize

@Parcelize
data class ClubRecruitModifyState(
    val isLoading: Boolean = false,
    val clubId: Int = -1,
    val recruitImageUrl: String = "",
    val showDatePickerDialog: Boolean = false,
    val recruitStartDate: LocalDate = LocalDate.now(),
    val recruitEndDate: LocalDate = recruitStartDate.plusDays(1),
    val recruitAlways: Boolean = false,
    val content: String = "",
    val showModifyRequestDialog: Boolean = false,
    val showModifyCancelDialog: Boolean = false
) : Parcelable
