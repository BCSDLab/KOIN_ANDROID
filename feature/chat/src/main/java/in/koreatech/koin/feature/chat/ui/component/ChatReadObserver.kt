package `in`.koreatech.koin.feature.chat.ui.component

import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.rememberUpdatedState
import androidx.compose.runtime.snapshotFlow
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.compose.LocalLifecycleOwner
import androidx.lifecycle.repeatOnLifecycle
import kotlinx.coroutines.flow.filter

@Composable
internal fun ChatReadObserver(
    listState: LazyListState,
    latestMessageId: String?,
    roomId: String?,
    onReadMessage: suspend (String, String) -> Unit
) {
    val currentOnReadMessage by rememberUpdatedState(onReadMessage)
    if (roomId == null || latestMessageId == null) return
    val lifecycleOwner = LocalLifecycleOwner.current
    LaunchedEffect(lifecycleOwner, listState, roomId, latestMessageId) {
        lifecycleOwner.repeatOnLifecycle(Lifecycle.State.RESUMED) {
            snapshotFlow {
                listState.layoutInfo.visibleItemsInfo.any { it.key == latestMessageId }
            }.filter { it }.collect {
                currentOnReadMessage(roomId, latestMessageId)
            }
        }
    }
}
