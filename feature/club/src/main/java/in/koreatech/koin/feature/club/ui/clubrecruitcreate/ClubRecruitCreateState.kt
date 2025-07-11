package `in`.koreatech.koin.feature.club.ui.clubrecruitcreate

import android.os.Parcelable
import java.time.LocalDate
import kotlinx.parcelize.Parcelize

@Parcelize
data class ClubRecruitCreateState(
    val isLoading: Boolean = false,
    val clubId: Int = -1,
    val recruitImageUrl: String = "",
    val showDatePickerDialog: Boolean = false,
    val recruitStartDate: LocalDate = LocalDate.now(),
    val recruitEndDate: LocalDate = recruitStartDate.plusDays(1),
    val recruitAlways: Boolean = false,
    val showCreateRequestDialog: Boolean = false,
    val showCreateCancelDialog: Boolean = false
) : Parcelable
