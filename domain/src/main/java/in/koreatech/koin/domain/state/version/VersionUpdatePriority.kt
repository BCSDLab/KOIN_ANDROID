package `in`.koreatech.koin.domain.state.version

sealed class VersionUpdatePriority {
    object High : VersionUpdatePriority()
    object Medium : VersionUpdatePriority()
    object Low : VersionUpdatePriority()
    object None : VersionUpdatePriority()
}