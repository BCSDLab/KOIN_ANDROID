package `in`.koreatech.koin.feature.dining.ui.diningdetail

import androidx.compose.runtime.Immutable
import `in`.koreatech.koin.domain.model.dining.DiningPlace
import `in`.koreatech.koin.domain.model.dining.DiningWithOperationTime
import kotlinx.collections.immutable.ImmutableList
import kotlinx.collections.immutable.persistentListOf

data class DiningState(
    val selectedDate: String,
    val dining: ImmutableList<DiningWithOperationTime> = persistentListOf(),
    val showBottomSheet: Boolean = false,
    val isSoldOutSubscribed: Boolean = false,
    val isDiningImageSubscribed: Boolean = false,
    val isDiningRefreshing: Boolean = false,
    val isLoading: Boolean = false,
    val diningReportState: DiningReportState = DiningReportState()
)

@Immutable
data class DiningReportState(
    val isDialogVisible: Boolean = false,
    val diningPlace: DiningPlace? = null,
    val imageUrl: String? = null,
    val reportedPlace: ImmutableList<DiningPlace> = persistentListOf(),
    val isImageUploading: Boolean = false
) {
    val isReportable: Boolean
        get() = imageUrl != null && diningPlace != null
}
