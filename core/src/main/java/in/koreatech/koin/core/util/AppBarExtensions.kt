package `in`.koreatech.koin.core.util

import `in`.koreatech.koin.core.appbar.AppBarBase
import android.content.Intent

inline fun AppBarBase.setAppBarButtonClickedListener(
    crossinline leftButtonClicked: () -> Unit,
    crossinline rightButtonClicked: () -> Unit
) {
    setOnClickListener {
        if (it.id == AppBarBase.getLeftButtonId()) {
            leftButtonClicked()
        } else if (id == AppBarBase.getRightButtonId()) {
            rightButtonClicked()
        }
    }
}