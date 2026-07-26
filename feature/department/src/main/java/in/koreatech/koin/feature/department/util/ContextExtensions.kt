package `in`.koreatech.koin.feature.department.util

import android.content.ActivityNotFoundException
import android.content.ClipData
import android.content.ClipboardManager
import android.content.Context
import android.content.ContextWrapper
import android.content.Intent
import androidx.activity.ComponentActivity
import androidx.core.content.getSystemService
import androidx.core.net.toUri
import `in`.koreatech.koin.core.toast.ToastUtil
import `in`.koreatech.koin.feature.department.R
import timber.log.Timber

internal fun Context.findActivity(): ComponentActivity? =
    when (this) {
        is ComponentActivity -> this
        is ContextWrapper -> baseContext.findActivity()
        else -> null
    }

internal fun Context.copyPhoneNumberToClipboard(phoneNumber: String) {
    val clipboardManager = getSystemService<ClipboardManager>()
    if (clipboardManager == null) {
        ToastUtil.getInstance().makeShort(getString(R.string.department_cannot_copy))
        return
    }
    clipboardManager.setPrimaryClip(
        ClipData.newPlainText(CLIP_LABEL_PHONE_NUMBER, phoneNumber)
    )
    ToastUtil.getInstance().makeShort(getString(R.string.department_phone_number_copied))
}

internal fun Context.openUrl(url: String) {
    try {
        startActivity(Intent(Intent.ACTION_VIEW, url.toUri()))
    } catch (e: ActivityNotFoundException) {
        Timber.e(e)
        ToastUtil.getInstance().makeShort(getString(R.string.department_cannot_open_link))
    }
}

private const val CLIP_LABEL_PHONE_NUMBER = "department_phone_number"
