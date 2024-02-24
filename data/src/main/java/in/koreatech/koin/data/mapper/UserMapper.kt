package `in`.koreatech.koin.data.mapper

import `in`.koreatech.koin.data.request.user.UserRequest
import `in`.koreatech.koin.data.response.user.AuthResponse
import `in`.koreatech.koin.data.response.user.UserResponse
import `in`.koreatech.koin.domain.model.user.AuthToken
import `in`.koreatech.koin.domain.model.user.Gender
import `in`.koreatech.koin.domain.model.user.User
import `in`.koreatech.koin.domain.model.user.UserIdentity

fun UserResponse.toUser() = User.Student(
    anonymousNickname = anonymousNickname,
    email = email,
    name = name,
    studentNumber = studentNumber,
    gender = when (gender) {
        0 -> Gender.Man
        1 -> Gender.Woman
        else -> Gender.Unknown
    },
    nickname = nickname,
    phoneNumber = phoneNumber,
    major = major
)

fun User.Student.toUserRequest() = UserRequest(
    nickname = nickname,
    name = name,
    studentNumber = studentNumber,
    major = major,
    phoneNumber = phoneNumber,
    gender = when (gender) {
        Gender.Man -> 0
        Gender.Woman -> 1
        else -> null
    },
    identity = 0,
    isGraduated = isStudent
)

fun Int.toGraduate(): Boolean{
    return this == 0
}

fun String.toPhoneNumber() : String{
    val digitsOnly = this.filter { it.isDigit() }

    return when (digitsOnly.length) {
        11 -> "${digitsOnly.substring(0, 3)}-${digitsOnly.substring(3, 7)}-${digitsOnly.substring(7)}"
        10 -> "${digitsOnly.substring(0, 3)}-${digitsOnly.substring(3, 6)}-${digitsOnly.substring(6)}"
        else -> digitsOnly
    }
}

fun String.toShcoolEamil() = "$this@koreatech.ac.kr"
