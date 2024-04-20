package `in`.koreatech.koin.domain.model.user

sealed class User {
    data class Student(
        val anonymousNickname: String?,
        val email: String?,
        val name: String?,
        val studentNumber: String?,
        val gender: Gender,
        val nickname: String?,
        val phoneNumber: String?,
        val major: String?
    ): User()

    object Anonymous: User()

    val isAnonymous get() = this is Anonymous
    val isStudent get() = this is Student
}