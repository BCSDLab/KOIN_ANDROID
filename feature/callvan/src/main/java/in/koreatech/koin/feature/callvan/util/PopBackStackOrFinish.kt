package `in`.koreatech.koin.feature.callvan.util

import android.content.Context
import androidx.activity.ComponentActivity
import androidx.navigation.NavController

fun NavController.popBackStackOrFinish(context: Context) {
    if (!popBackStack()) (context as? ComponentActivity)?.finish()
}
