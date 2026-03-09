package `in`.koreatech.koin.feature.callvan.ui.list

sealed class CallvanListSideEffect {
    data object FetchData : CallvanListSideEffect()
}
