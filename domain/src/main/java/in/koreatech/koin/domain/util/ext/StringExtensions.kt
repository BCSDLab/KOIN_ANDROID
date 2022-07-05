package `in`.koreatech.koin.domain.util.ext

import `in`.koreatech.koin.domain.util.HashUtil

fun String.toSHA256() = HashUtil.generateSHA256(this)

