package `in`.koreatech.koin.core.progressdialog

import android.app.ProgressDialog
import android.content.Context
import android.os.AsyncTask
import `in`.koreatech.koin.core.R

class CustomProgressDialog(context: Context?, private val message: String?) :
    AsyncTask<Void?, Void?, Void?>() {
    private val TAG = "CustomProgressDialog"
    private var progressDialog: ProgressDialog?

    init {
        progressDialog = ProgressDialog(context, R.style.KAPProgress)
        progressDialog!!.setCancelable(false)
        progressDialog!!.setCanceledOnTouchOutside(false)
        progressDialog!!.isIndeterminate = true
    }

    override fun doInBackground(vararg params: Void?): Void? {
        while (true) {
            if (isCancelled) {
                break
            }
        }
        return null
    }

    override fun onPreExecute() {
        progressDialog!!.setMessage(message)
        progressDialog!!.show()
    }

    override fun onPostExecute(aVoid: Void?) {
        if (progressDialog != null) progressDialog!!.dismiss()
    }

    override fun onCancelled(aVoid: Void?) {
        if (progressDialog != null) progressDialog!!.dismiss()
        super.onCancelled(aVoid)
        progressDialog = null
    }
}