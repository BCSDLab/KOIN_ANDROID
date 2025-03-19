package `in`.koreatech.business.feature.event.writeevent.writeevent

import `in`.koreatech.koin.domain.model.store.StoreUrl
import java.time.YearMonth

data class WriteEventState(
    val storeId: Int = -1,
    val title: String = "",
    val content: String = "",
    val startDate: String = "2025-01-01",
    val endDate: String = "2025-12-31",
    val startYear: String = "2025",
    val startMonth: String = "12",
    val startDay: String = "",
    val endYear: String = "",
    val endMonth: String = "",
    val endDay: String = "",
    val images: List<String> = emptyList(),
    val fileInfo: MutableList<StoreUrl> = mutableListOf(), // 서버에 보낼 파일 정보
    val showCalendarAlert: Boolean = false,
    val showTitleInputAlert: Boolean = false,
    val showContentInputAlert: Boolean = false,
    val showDateInputAlert: Boolean = false,
    val showDatePickerAlert: Boolean = false,
    val selectedYearMonth: YearMonth = YearMonth.now(),
    val error: Throwable? = null
)
