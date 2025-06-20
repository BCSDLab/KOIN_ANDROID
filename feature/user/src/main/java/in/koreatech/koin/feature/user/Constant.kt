package `in`.koreatech.koin.feature.user

import kotlinx.collections.immutable.persistentListOf

val genderList = persistentListOf("남성", "여성")
val majorStringList = persistentListOf(
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

const val SIGN_UP_PHONE_NUMBER_MAX_LENGTH = 11
const val SIGN_UP_VERIFICATION_CODE_MAX_LENGTH = 6
const val SIGN_UP_NICKNAME_MAX_LENGTH = 10

const val PHONE_NUMBER_LENGTH = 11
const val VERIFICATION_CODE_LENGTH = 6

const val KOREATECH_EMAIL_DOMAIN = "koreatech.ac.kr"

const val DEEPLINK_MAIN = "koin://main/activity"
const val DEEPLINK_TIMETABLE = "koin://timetable/activity"
const val DEEPLINK_ARTICLE = "koin://article/activity"
const val DEEPLINK_FIND_ID = "koin://findid/activity"
const val DEEPLINK_FIND_PASSWORD = "koin://findpassword/activity"
const val DEEPLINK_SIGN_UP = "koin://signup/activity"

const val OWNER_URL_STAGE = "https://owner.stage.koreatech.in/"
const val OWNER_URL_PRODUCTION = "https://owner.koreatech.in/"
