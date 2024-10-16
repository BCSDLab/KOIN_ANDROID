package `in`.koreatech.koin.domain.model.version

import `in`.koreatech.koin.domain.state.version.VersionUpdatePriority

data class Version(
    val currentVersion: String,
    val latestVersion: String,
    val title: String,
    val content: String,
    val versionUpdatePriority: VersionUpdatePriority
)
