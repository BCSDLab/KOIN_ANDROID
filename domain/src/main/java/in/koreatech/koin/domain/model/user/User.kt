package `in`.koreatech.koin.domain.model.user

sealed class User {
    data class Student(
        val id: Int,
        val anonymousNickname: String?,
        val email: String?,
        val name: String?,
        val studentNumber: String?,
        val gender: Gender,
        val nickname: String?,
        val phoneNumber: String?,
        val major: String?,
        val userType: String
    ): User()

    data object Anonymous: User()

    val isAnonymous get() = this is Anonymous
    val isStudent get() = this is Student
}