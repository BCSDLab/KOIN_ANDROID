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
    val phoneNumberDigitsOnly = this.filter { it.isDigit() }

    return when (phoneNumberDigitsOnly.length) {
        11 -> "${phoneNumberDigitsOnly.substring(0, 3)}-${phoneNumberDigitsOnly.substring(3, 7)}-${phoneNumberDigitsOnly.substring(7)}"
        10 -> "${phoneNumberDigitsOnly.substring(0, 3)}-${phoneNumberDigitsOnly.substring(3, 6)}-${phoneNumberDigitsOnly.substring(6)}"
        else -> phoneNumberDigitsOnly
    }
}

fun String.toSchoolEamil() = "$this@koreatech.ac.kr"
