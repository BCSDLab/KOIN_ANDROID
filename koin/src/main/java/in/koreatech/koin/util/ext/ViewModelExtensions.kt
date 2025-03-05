package `in`.koreatech.koin.util.ext

import android.view.View
import androidx.lifecycle.LifecycleOwner
import `in`.koreatech.koin.core.viewmodel.BaseViewModel
import `in`.koreatech.koin.domain.model.error.ErrorHandler
import `in`.koreatech.koin.domain.util.onFailure
import `in`.koreatech.koin.util.SnackbarUtil

fun <T> Pair<T?, ErrorHandler?>.onFailureToast(viewModel: BaseViewModel): Pair<T?, ErrorHandler?> =
    this.onFailure { e ->
        viewModel.updateErrorMessage(e.message)
    }

fun BaseViewModel.withToastError(
    lifecycleOwner: LifecycleOwner,
    view: View,
) {
    errorToast.observe(lifecycleOwner) {
        SnackbarUtil.makeShortSnackbar(view, it)
    }
}
