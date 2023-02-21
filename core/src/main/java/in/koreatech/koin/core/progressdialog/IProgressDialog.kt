package `in`.koreatech.koin.core.progressdialog

import androidx.annotation.StringRes

interface IProgressDialog {
    fun showProgressDialog(message: String?)
    fun showProgressDialog(@StringRes resId: Int)
    fun hideProgressDialog()
}