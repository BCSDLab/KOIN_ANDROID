package `in`.koreatech.koin.core.fragment

import android.content.Context
import android.os.Bundle
import androidx.annotation.StringRes
import androidx.fragment.app.Fragment
import `in`.koreatech.koin.core.progressdialog.CustomProgressDialog
import `in`.koreatech.koin.core.progressdialog.IProgressDialog

open class BaseFragment: Fragment(), IProgressDialog {
    private var customProgressDialog: CustomProgressDialog? = null
    private lateinit var context: Context

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        context = this.requireContext()
    }

    override fun showProgressDialog(message: String?) {
        if (customProgressDialog == null) {
            customProgressDialog = CustomProgressDialog(context, message)
        }
    }

    override fun showProgressDialog(@StringRes resId: Int) {
        if (customProgressDialog == null) {
            customProgressDialog = CustomProgressDialog(context, context.resources.getString(resId))
        }
    }

    override fun hideProgressDialog() {
        if (customProgressDialog != null) {
            customProgressDialog = null
        }
    }
}