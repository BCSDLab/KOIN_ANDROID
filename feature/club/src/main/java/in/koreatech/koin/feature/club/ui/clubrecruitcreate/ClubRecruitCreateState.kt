package `in`.koreatech.koin.feature.club.ui.clubrecruitcreate

import android.os.Parcelable
import kotlinx.parcelize.Parcelize
import java.time.LocalDate

@Parcelize
data class ClubRecruitCreateState(
    val isLoading: Boolean = false,
    val clubId: Int = -1,
    val recruitImageUrl: String = "",
    val showDatePickerDialog: Boolean = false,
    val recruitStartDate: LocalDate = LocalDate.now(),
    val recruitEndDate: LocalDate = LocalDate.now(),
    val recruitAlways: Boolean = false,
    val showCreateRequestDialog: Boolean = false,
    val showCreateCancelDialog: Boolean = false
) : Parcelable
