package `in`.koreatech.business.main

import `in`.koreatech.koin.domain.model.version.Version

data class BusinessMainActivityState(
    val destination: String = "",
    val version: Version? = null,
)
