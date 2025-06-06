package `in`.koreatech.koin.domain.model.user

sealed class User {
    data class Student(
        val id: Int,
        val loginId: String,
        val email: String?,
        val anonymousNickname: String?,
        val gender: Gender,
        val major: String?,
        val name: String?,
        val nickname: String?,
        val phoneNumber: String?,
        val studentNumber: String?,
        val userType: String
    ) : User()

    data class General(
        val id: Int,
        val loginId: String,
        val email: String?,
        val anonymousNickname: String?,
        val gender: Gender,
        val name: String,
        val nickname: String?,
        val phoneNumber: String,
        val userType: String
    ) : User()

    data object Anonymous : User()

    val isAnonymous get() = this is Anonymous
    val isStudent get() = this is Student
    val isGeneral get() = this is General
}
