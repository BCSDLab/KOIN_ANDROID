package `in`.koreatech.koin.feature.recruitment.ui.chat.util

import `in`.koreatech.koin.domain.model.user.User

fun User.userId(): Int = when (this) {
    is User.Student -> id
    is User.General -> id
    User.Anonymous -> 0
}
