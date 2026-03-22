package `in`.koreatech.koin.feature.callvan.ui.list

import `in`.koreatech.koin.feature.callvan.ui.list.model.CallvanListErrorType

sealed interface CallvanListSideEffect {
    data class ShowSnackbar(val errorType: CallvanListErrorType) : CallvanListSideEffect
}