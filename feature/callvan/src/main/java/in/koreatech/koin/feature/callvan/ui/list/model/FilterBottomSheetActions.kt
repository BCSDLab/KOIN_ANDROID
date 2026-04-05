package `in`.koreatech.koin.feature.callvan.ui.list.model

data class FilterBottomSheetActions(
    val onItemClicked: (CallvanFilterType) -> Unit,
    val onReset: () -> Unit,
    val onApplyClick: () -> Unit
)