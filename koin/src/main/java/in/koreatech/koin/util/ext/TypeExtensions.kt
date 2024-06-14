package `in`.koreatech.koin.util.ext

import android.content.res.Resources
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp

val Float.pxToDp: Dp
    get() = (this / Resources.getSystem().displayMetrics.density).dp
