package `in`.koreatech.koin.domain.model.user

sealed class User {
    data class Student(
        val id: Int,
        val isGraduated: Boolean,
        val enabled: Boolean,
        val anonymousNickname: String?,
        val portalAccount: String,
        val identity: UserIdentity,
        val name: String?,
        val studentNumber: String?,
        val profileImageUrl: String?,
        val gender: Gender,
        val nickname: String?,
        val phoneNumber: String?,
        val accountNonExpired: Boolean,
        val accountNonLocked: Boolean,
        val credentialsNonExpired: Boolean,
        val username: String?,
        val major: String?
    ): User()

    object Anonymous: User()

    val isAnonymous get() = this is Anonymous
    val isStudent get() = this is Student
}