package `in`.koreatech.koin.feature.recruitment.mapper

import `in`.koreatech.koin.domain.error.recruitment.KoinRecruitmentException

private const val DEFAULT_RECRUITMENT_ERROR_MESSAGE = "요청을 처리하지 못했어요. 잠시 후 다시 시도해주세요."

fun Throwable.toRecruitmentErrorMessage(default: String = DEFAULT_RECRUITMENT_ERROR_MESSAGE): String =
    (this as? KoinRecruitmentException)?.message ?: default
