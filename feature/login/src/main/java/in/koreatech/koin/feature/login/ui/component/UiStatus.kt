package `in`.koreatech.koin.feature.login.ui.component

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
sealed class UiStatus : Parcelable {
    object Init : UiStatus()

    object Loading : UiStatus()

    object Success : UiStatus()

    data class Failed(
        val message: String = ""
    ) : UiStatus()
}
