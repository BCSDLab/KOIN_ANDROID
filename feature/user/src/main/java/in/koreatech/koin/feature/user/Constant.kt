package `in`.koreatech.koin.feature.user

import kotlinx.collections.immutable.persistentListOf

val genderList = persistentListOf("남성", "여성")

const val NICKNAME_MAX_LENGTH = 10
const val PHONE_NUMBER_LENGTH = 11
const val VERIFICATION_CODE_LENGTH = 6

const val KOREATECH_EMAIL_DOMAIN = "koreatech.ac.kr"

const val DEEPLINK_MAIN = "koin://main/activity"
const val DEEPLINK_ARTICLE = "koin://article/activity"

const val OWNER_URL_STAGE = "https://owner.stage.koreatech.in/"
const val OWNER_URL_PRODUCTION = "https://owner.koreatech.in/"
