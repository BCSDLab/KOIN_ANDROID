package `in`.koreatech.koin.domain.util.ext

import `in`.koreatech.koin.domain.util.PasswordUtil

fun String.toSHA256() = PasswordUtil.generateSHA256(this)

