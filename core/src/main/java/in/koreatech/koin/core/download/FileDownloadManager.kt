package `in`.koreatech.koin.core.download

import android.app.DownloadManager
import android.app.DownloadManager.Request
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Context.DOWNLOAD_SERVICE
import android.content.Context.RECEIVER_EXPORTED
import android.content.Intent
import android.content.IntentFilter
import android.database.Cursor
import android.os.Build
import androidx.lifecycle.DefaultLifecycleObserver
import androidx.lifecycle.LifecycleOwner

class FileDownloadManager(
    private val lifecycleOwner: LifecycleOwner,
    private val context: Context,
) {

    private val downloadManager: DownloadManager = context.getSystemService(DOWNLOAD_SERVICE) as DownloadManager

    private var id: Long = -1

    private var onDownloadSuccessListener: OnDownloadSuccessListener? = null
    private var onDownloadFailureListener: OnDownloadFailureListener? = null

    private var broadcastReceiver: BroadcastReceiver = object : BroadcastReceiver() {
        override fun onReceive(context: Context?, intent: Intent?) {
            if (intent?.action == DownloadManager.ACTION_DOWNLOAD_COMPLETE) {
                val query = DownloadManager.Query()
                query.setFilterById(id)
                val cursor: Cursor = downloadManager.query(query)
                if (!cursor.moveToFirst()) {
                    return
                }

                val columnIndex = cursor.getColumnIndex(DownloadManager.COLUMN_STATUS)
                val status = cursor.getInt(columnIndex)

                if (status == DownloadManager.STATUS_SUCCESSFUL) {
                    onDownloadSuccessListener?.onDownloadSuccess()
                } else if (status == DownloadManager.STATUS_FAILED) {
                    onDownloadFailureListener?.onDownloadFailure()
                }
            }
        }
    }

    init {
        lifecycleOwner.lifecycle.addObserver(object : DefaultLifecycleObserver {
            override fun onResume(owner: LifecycleOwner) {
                val intentFilter = IntentFilter()
                intentFilter.addAction(DownloadManager.ACTION_DOWNLOAD_COMPLETE);

                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU)
                    context.registerReceiver(broadcastReceiver, intentFilter, RECEIVER_EXPORTED)
                else
                    context.registerReceiver(broadcastReceiver, intentFilter)

            }
            override fun onPause(owner: LifecycleOwner) {
                context.unregisterReceiver(broadcastReceiver)
            }
        })
    }

    fun download(request: Request) {
        id = downloadManager.enqueue(request)
    }

    fun setOnDownloadSuccessListener(onDownloadSuccessListener: OnDownloadSuccessListener) {
        this.onDownloadSuccessListener = onDownloadSuccessListener
    }

    fun setOnDownloadFailureListener(onDownloadFailureListener: OnDownloadFailureListener) {
        this.onDownloadFailureListener = onDownloadFailureListener
    }

    fun interface OnDownloadSuccessListener {
        fun onDownloadSuccess()
    }

    fun interface OnDownloadFailureListener {
        fun onDownloadFailure()
    }
}