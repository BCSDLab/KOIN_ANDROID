package `in`.koreatech.koin.core.util

import android.content.Context
import android.content.Intent
import androidx.core.net.toUri

fun Context.goToContactUrl() = startActivity(Intent(Intent.ACTION_VIEW, KOIN_ASK_FORM.toUri()))

const val KOIN_ASK_FORM = "https://forms.gle/Yo1WNR5mLQdi1pMh6"
