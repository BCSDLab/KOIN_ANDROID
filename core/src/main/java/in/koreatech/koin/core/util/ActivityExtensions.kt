package `in`.koreatech.koin.core.util

import androidx.lifecycle.LifecycleOwner
import `in`.koreatech.koin.core.progressdialog.IProgressDialog
import `in`.koreatech.koin.core.viewmodel.BaseViewModel

fun <T : BaseViewModel> IProgressDialog.withLoading(lifecycleOwner: LifecycleOwner, viewModel: T) {
    viewModel.isLoading.observe(lifecycleOwner) {
        if (it) {
            showProgressDialog("로딩 중...")
        } else {
            hideProgressDialog()
        }
    }
}
