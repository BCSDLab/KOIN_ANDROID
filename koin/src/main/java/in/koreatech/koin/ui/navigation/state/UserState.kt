package `in`.koreatech.koin.ui.navigation.state

import `in`.koreatech.koin.domain.model.user.User

data class UserState(
    val user: User?,
    val isAnonymous: Boolean
) {
    companion object {
        val anonymous get() = UserState(null, true)
        fun user(user: User) = UserState(user, false)
    }
}