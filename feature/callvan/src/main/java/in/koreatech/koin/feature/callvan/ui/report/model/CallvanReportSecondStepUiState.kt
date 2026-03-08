package `in`.koreatech.koin.feature.callvan.ui.report.model

import android.net.Uri
import androidx.compose.runtime.Stable
import kotlinx.collections.immutable.ImmutableList
import kotlinx.collections.immutable.persistentListOf

@Stable
class CallvanReportSecondStepUiState(
    val detail: String = "",
    val onDetailChange: (String) -> Unit = {},
    val images: ImmutableList<Uri> = persistentListOf(),
    val onAddImageClick: () -> Unit = {},
    val onRemoveImage: (Uri) -> Unit = {}
)