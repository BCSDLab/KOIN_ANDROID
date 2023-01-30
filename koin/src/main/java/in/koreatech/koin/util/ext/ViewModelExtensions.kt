package `in`.koreatech.koin.util.ext

import android.view.View
import androidx.lifecycle.LifecycleOwner
import `in`.koreatech.koin.core.viewmodel.BaseViewModel
import `in`.koreatech.koin.domain.model.Result
import `in`.koreatech.koin.domain.model.error.ErrorHandler
import `in`.koreatech.koin.util.SnackbarUtil

fun <T> Result<T>.onFailureToast(viewModel: BaseViewModel): Result<T> =
    this.onFailure {
        viewModel.updateErrorMessage(it)
    }

fun BaseViewModel.withToastError(lifecycleOwner: LifecycleOwner, view: View) {
    errorToast.observe(lifecycleOwner) {
        SnackbarUtil.makeShortSnackbar(view, it)
    }
}