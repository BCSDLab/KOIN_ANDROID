package `in`.koreatech.business.feature.store

data class WriteEventState(
    val title: String = "",
    val content: String = "",
    val startYear: String = "",
    val startMonth: String = "",
    val startDay: String = "",
    val endYear: String = "",
    val endMonth: String = "",
    val endDay: String = "",
    val images: List<String> = emptyList(),
    val showTitleInputAlert: Boolean = false,
    val showContentInputAlert: Boolean = false,
    val showDateInputAlert: Boolean = false,
)
