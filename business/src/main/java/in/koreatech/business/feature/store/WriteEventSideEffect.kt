package `in`.koreatech.business.feature.store

sealed class WriteEventSideEffect {
    data object FocusStartYear : WriteEventSideEffect()
    data object FocusStartMonth : WriteEventSideEffect()
    data object FocusStartDay : WriteEventSideEffect()
    data object FocusEndYear : WriteEventSideEffect()
    data object FocusEndMonth : WriteEventSideEffect()
    data object FocusEndDay : WriteEventSideEffect()
}
