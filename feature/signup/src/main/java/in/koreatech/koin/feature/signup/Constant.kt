package `in`.koreatech.koin.feature.signup

import kotlinx.collections.immutable.persistentListOf

const val SIGN_UP_PHONE_NUMBER_MAX_LENGTH = 11
const val SIGN_UP_VERIFICATION_CODE_MAX_LENGTH = 6

val departmentStringList = persistentListOf(
    "건축공학부",
    "고용서비스정책학과",
    "기계공학부",
    "디자인공학부",
    "메카트로닉스공학부",
    "산업경영학부",
    "전기전자통신공학부",
    "컴퓨터공학부",
    "화학생명공학부",
    "에너지신소재공학부"
)
