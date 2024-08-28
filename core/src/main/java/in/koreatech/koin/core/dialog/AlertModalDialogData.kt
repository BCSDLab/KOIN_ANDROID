package `in`.koreatech.koin.core.dialog

import androidx.annotation.StringRes
import `in`.koreatech.koin.core.R

data class AlertModalDialogData(
    @StringRes val title: Int,
    @StringRes val message: Int,
    @StringRes val positiveButtonText: Int,
    @StringRes val negativeButtonText: Int = R.string.close,
)