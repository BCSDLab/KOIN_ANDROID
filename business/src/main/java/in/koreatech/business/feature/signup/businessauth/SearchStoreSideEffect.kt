package `in`.koreatech.business.feature.signup.businessauth


sealed class SearchStoreSideEffect {
    data class SearchStore(val search: String) : SearchStoreSideEffect()
    data object NavigateToBackScreen : SearchStoreSideEffect()
}