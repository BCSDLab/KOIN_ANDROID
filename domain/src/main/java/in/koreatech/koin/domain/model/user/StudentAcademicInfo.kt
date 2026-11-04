package `in`.koreatech.koin.domain.model.user

data class StudentAcademicInfo(
    val studentNumber: String?,
    val department: String?,
    val major: String?
)

data class StudentAcademicInfoInput(
    val department: String,
    val major: String?,
    val studentNumber: String?
)
