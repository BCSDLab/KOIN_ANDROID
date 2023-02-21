package `in`.koreatech.koin.core.activity

import android.content.Context
import android.content.pm.ActivityInfo
import android.os.Bundle
import androidx.annotation.LayoutRes
import androidx.annotation.StringRes
import androidx.appcompat.app.AppCompatActivity
import `in`.koreatech.koin.core.progressdialog.CustomProgressDialog
import `in`.koreatech.koin.core.progressdialog.IProgressDialog

open class ActivityBase : AppCompatActivity, IProgressDialog {
    private var customProgressDialog: CustomProgressDialog? = null

    constructor() : super()
    constructor(@LayoutRes contentLayoutId: Int) : super(contentLayoutId)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        try {
            requestedOrientation = ActivityInfo.SCREEN_ORIENTATION_PORTRAIT
        } catch (ignore: IllegalStateException) {
        }
    }

    override fun showProgressDialog(message: String?) {
        if (customProgressDialog == null) {
            customProgressDialog = CustomProgressDialog(this, message)
            customProgressDialog!!.execute()
        }
    }

    override fun showProgressDialog(@StringRes resId: Int) {
        if (customProgressDialog == null) {
            customProgressDialog =
                CustomProgressDialog(this, getString(resId))
            customProgressDialog!!.execute()
        }
    }

    override fun hideProgressDialog() {
        if (customProgressDialog != null) {
            customProgressDialog!!.cancel(false)
            customProgressDialog = null
        }
    }
}