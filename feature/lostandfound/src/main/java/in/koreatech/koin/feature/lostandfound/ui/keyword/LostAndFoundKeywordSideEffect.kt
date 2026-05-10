package `in`.koreatech.koin.feature.lostandfound.ui.keyword

import androidx.annotation.StringRes

sealed class LostAndFoundKeywordSideEffect {
    data class ShowSnackbar(@StringRes val messageResId: Int) : LostAndFoundKeywordSideEffect()
}
