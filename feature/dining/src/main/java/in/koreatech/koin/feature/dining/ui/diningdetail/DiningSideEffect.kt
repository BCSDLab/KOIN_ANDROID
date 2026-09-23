package `in`.koreatech.koin.feature.dining.ui.diningdetail

sealed interface DiningSideEffect {
    data class FetchDining(val forceRefresh: Boolean) : DiningSideEffect
}
