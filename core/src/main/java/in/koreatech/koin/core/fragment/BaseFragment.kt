package `in`.koreatech.koin.core.fragment

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.StringRes
import androidx.core.app.ActivityCompat
import androidx.databinding.ViewDataBinding
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentTransaction
import androidx.fragment.app.commit
import `in`.koreatech.koin.core.activity.ActivityBase
import `in`.koreatech.koin.core.progressdialog.CustomProgressDialog
import `in`.koreatech.koin.core.progressdialog.IProgressDialog

open class BaseFragment: Fragment(), IProgressDialog {
    private var customProgressDialog: CustomProgressDialog? = null
    private var context: Context? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        context = this.getContext()
    }

    override fun showProgressDialog(message: String?) {
        if (customProgressDialog == null) {
            customProgressDialog = CustomProgressDialog(context, message)
        }
    }

    override fun showProgressDialog(@StringRes resId: Int) {
        if (customProgressDialog == null) {
            customProgressDialog = CustomProgressDialog(context, context!!.resources.getString(resId))
        }
    }

    override fun hideProgressDialog() {
        if (customProgressDialog != null) {
            customProgressDialog = null
        }
    }
}