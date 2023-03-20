package `in`.koreatech.core.ui.ext

import `in`.koreatech.core.ui.view.appbar.AppBarBase

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