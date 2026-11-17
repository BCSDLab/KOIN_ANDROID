package `in`.koreatech.koin.feature.user

import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.res.stringResource
import kotlinx.collections.immutable.ImmutableList
import kotlinx.collections.immutable.persistentListOf

@Composable
fun rememberGenderList(): ImmutableList<String> {
    val man = stringResource(R.string.user_info_general_user_info_gender_man)
    val woman = stringResource(R.string.user_info_general_user_info_gender_woman)
    return remember(man, woman) { persistentListOf(man, woman) }
}

const val NICKNAME_MAX_LENGTH = 10
const val PHONE_NUMBER_LENGTH = 11
const val VERIFICATION_CODE_LENGTH = 6

const val KOREATECH_EMAIL_DOMAIN = "koreatech.ac.kr"

const val DEEPLINK_MAIN = "koin://main/navigation"

const val OWNER_URL_STAGE = "https://owner.stage.koreatech.in/"
const val OWNER_URL_PRODUCTION = "https://owner.koreatech.in/"
