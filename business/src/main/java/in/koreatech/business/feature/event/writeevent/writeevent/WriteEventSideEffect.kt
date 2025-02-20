package `in`.koreatech.business.feature.event.writeevent.writeevent

sealed class WriteEventSideEffect {
    data object ToastImageLimit : WriteEventSideEffect()
    data object RegisterEventSuccess : WriteEventSideEffect()
}