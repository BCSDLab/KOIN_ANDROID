package `in`.koreatech.koin.feature.recruitment.mapper

import `in`.koreatech.koin.domain.error.recruitment.KoinRecruitmentException

fun Throwable.toRecruitmentErrorMessage(): String =
    (this as? KoinRecruitmentException)?.message.orEmpty()
