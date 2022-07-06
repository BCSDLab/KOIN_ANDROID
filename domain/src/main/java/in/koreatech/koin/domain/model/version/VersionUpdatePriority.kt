package `in`.koreatech.koin.domain.model.version

sealed class VersionUpdatePriority {
    object High : VersionUpdatePriority()
    object Medium : VersionUpdatePriority()
    object Low : VersionUpdatePriority()
    object None : VersionUpdatePriority()
}