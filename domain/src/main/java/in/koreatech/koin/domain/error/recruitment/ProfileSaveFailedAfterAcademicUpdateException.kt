package `in`.koreatech.koin.domain.error.recruitment

import `in`.koreatech.koin.domain.model.user.StudentAcademicInfo

class ProfileSaveFailedAfterAcademicUpdateException(
    val academicInfo: StudentAcademicInfo,
    override val cause: Throwable
) : Exception(cause)
