package `in`.koreatech.koin.feature.recruitment.ui.myappliedrecruitment.model

enum class AppliedFilterStatus {
    ALL,
    APPROVED,
    PENDING,
    REJECTED
}

fun AppliedFilterStatus.toApiValue(): List<String> = when (this) {
    AppliedFilterStatus.ALL -> emptyList()
    AppliedFilterStatus.APPROVED -> listOf("ACCEPTED")
    AppliedFilterStatus.PENDING -> listOf("PENDING")
    AppliedFilterStatus.REJECTED -> listOf("REJECTED")
}

enum class AppliedFilterSort {
    LATEST,
    DEADLINE
}

fun AppliedFilterSort.toApiValue(): String = when (this) {
    AppliedFilterSort.LATEST -> "LATEST_DESC"
    AppliedFilterSort.DEADLINE -> "DEADLINE_ASC"
}

data class AppliedFilterState(
    val status: AppliedFilterStatus = AppliedFilterStatus.ALL,
    val sort: AppliedFilterSort = AppliedFilterSort.LATEST
)
