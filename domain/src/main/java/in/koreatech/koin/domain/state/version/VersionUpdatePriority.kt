package `in`.koreatech.koin.domain.state.version

sealed class VersionUpdatePriority {
    data object Importance : VersionUpdatePriority()
    data object None : VersionUpdatePriority()
}