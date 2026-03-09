package `in`.koreatech.koin.feature.callvan.ui.report.model

import android.net.Uri
import kotlinx.collections.immutable.ImmutableList
import kotlinx.collections.immutable.persistentListOf

data class CallvanReportSecondStepUiState(
    val detail: String = "",
    val images: ImmutableList<Uri> = persistentListOf()
)
