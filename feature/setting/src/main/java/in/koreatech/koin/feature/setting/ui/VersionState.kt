package `in`.koreatech.koin.feature.setting.ui

sealed class VersionState {
    data class Outdated(val currentVersion: String, val latestVersion: String) : VersionState()

    data class Latest(val currentVersion: String) : VersionState()

    data object Init : VersionState()

    data object Failure : VersionState()
}
