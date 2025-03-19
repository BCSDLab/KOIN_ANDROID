package `in`.koreatech.koin.core.util

import `in`.koreatech.koin.core.appbar.AppBarBase

inline fun AppBarBase.setAppBarButtonClickedListener(
    crossinline leftButtonClicked: () -> Unit,
    crossinline rightButtonClicked: () -> Unit
) {
    setOnClickListener {
        if (it.id == AppBarBase.getLeftButtonId()) {
            leftButtonClicked()
        } else if (it.id == AppBarBase.getRightButtonId()) {
            rightButtonClicked()
        }
    }
}
