package `in`.koreatech.koin.feature.callvan.ui.list.model

import androidx.compose.runtime.Stable

@Stable
data class CallvanListItemCallbacks (
    val onJoin: () -> Unit = {},
    val onCancelJoin: () -> Unit = {},
    val onClose: () -> Unit = {},
    val onReRecruit: () -> Unit = {},
    val onComplete: () -> Unit = {},
    val onCall: () -> Unit = {},
    val onChat: () -> Unit = {}
)
