package `in`.koreatech.koin.feature.callvan.ui.report.model

import android.net.Uri
import androidx.compose.runtime.Immutable
import kotlinx.collections.immutable.ImmutableList
import kotlinx.collections.immutable.persistentListOf

@Immutable
data class CallvanReportSecondStepUiState(
    val detail: String = "",
    val images: ImmutableList<Uri> = persistentListOf()
)
