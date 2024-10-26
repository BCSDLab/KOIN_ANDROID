package `in`.koreatech.koin.ui.userinfo.state

import `in`.koreatech.koin.R
import `in`.koreatech.koin.domain.model.user.Gender
import `in`.koreatech.koin.domain.model.user.User
import android.content.Context

data class UserState(
    val email: String,
    val username: String,
    val userNickname: String,
    val userAnonymousNickname: String,
    val phoneNumber: String,
    val gender: String,
    val studentNumber: String,
    val major: String,
)

fun User.toUserState(context: Context): UserState {
    return when (this) {
        User.Anonymous -> throw IllegalStateException()
        is User.Student -> with(context) {
            UserState(
                email = email ?: "",
                username = name ?: "",
                userNickname = nickname ?: "",
                userAnonymousNickname = anonymousNickname ?: "",
                phoneNumber = phoneNumber ?: "",
                gender = when (gender) {
                    Gender.Woman -> getString(R.string.user_info_gender_female)
                    Gender.Man -> getString(R.string.user_info_gender_male)
                    Gender.Unknown -> ""
                },
                studentNumber = studentNumber ?: "",
                major = major ?: ""
            )
        }
    }
}