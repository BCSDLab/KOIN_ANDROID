package `in`.koreatech.koin.feature.recruitment.mapper

import `in`.koreatech.koin.domain.error.recruitment.KoinRecruitmentException

/**
 * 서버가 내려준 에러 메시지를 반환한다.
 * 서버 메시지가 없으면 빈 문자열을 반환하며, 화면에서 비어 있을 때 기본 문구(recruitment_error_default)를 표시한다.
 */
fun Throwable.toRecruitmentErrorMessage(): String =
    (this as? KoinRecruitmentException)?.message.orEmpty()
