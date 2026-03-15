package `in`.koreatech.koin.feature.callvan.ui.report.model

class CallvanReportSecondStepUiAction(
    val onDetailChange: (String) -> Unit = {},
    val onAddImageClick: () -> Unit = {},
    val onRemoveImage: (index: Int) -> Unit = {}
)
