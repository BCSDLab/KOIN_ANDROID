package `in`.koreatech.koin.ui.userinfo.state

import `in`.koreatech.koin.R
import `in`.koreatech.koin.domain.model.user.Gender
import `in`.koreatech.koin.domain.model.user.User
import android.content.Context

data class UserState(
    val portalAccount: String,
    val username: String,
    val userNickname: String,
    val userAnonymousNickname: String,
    val phoneNumber: String,
    val gender: String,
    val studentNumber: String,
    val major: String
)

fun User.toUserState(context: Context): UserState {
    return when (this) {
        User.Anonymous -> throw IllegalStateException()
        is User.Student -> with(context) {
            UserState(
                portalAccount = getString(R.string.koreatech_email_postfix, portalAccount),
                username = name ?: getString(R.string.user_info_no_name),
                userNickname = nickname ?: getString(R.string.user_info_no_nickname),
                userAnonymousNickname = anonymousNickname
                    ?: getString(R.string.user_info_no_anonymous_nickname),
                phoneNumber = phoneNumber ?: getString(R.string.user_info_no_phone_number),
                gender = when (gender) {
                    Gender.Woman -> getString(R.string.user_info_gender_female)
                    Gender.Man -> getString(R.string.user_info_gender_male)
                    Gender.Unknown -> getString(R.string.user_info_gender_unknown)
                },
                studentNumber = studentNumber ?: getString(R.string.user_info_no_student_number),
                major = major ?: getString(R.string.user_info_no_major)
            )
        }
    }

}