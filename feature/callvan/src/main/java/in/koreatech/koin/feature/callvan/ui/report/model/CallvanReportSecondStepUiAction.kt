package `in`.koreatech.koin.feature.callvan.ui.report.model

import android.net.Uri

class CallvanReportSecondStepUiAction(
    val onDetailChange: (String) -> Unit = {},
    val onAddImageClick: () -> Unit = {},
    val onRemoveImage: (Uri) -> Unit = {}
)
