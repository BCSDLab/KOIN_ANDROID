package `in`.koreatech.koin.domain.model.version

data class Version(
    val currentVersion: String,
    val latestVersion: String,
    val versionUpdatePriority: VersionUpdatePriority
)
