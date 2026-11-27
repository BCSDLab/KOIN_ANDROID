package `in`.koreatech.koin.feature.chat.ui.component

import androidx.lifecycle.ViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import `in`.koreatech.koin.domain.usecase.chat.MarkChatReadUseCase
import javax.inject.Inject
import kotlinx.coroutines.sync.Mutex
import kotlinx.coroutines.sync.withLock
import timber.log.Timber

@HiltViewModel
internal class ChatReadViewModel @Inject constructor(
    private val markChatReadUseCase: MarkChatReadUseCase
) : ViewModel() {
    private val mutex = Mutex()
    private var lastReadRoomId: String? = null
    private var lastReadMessageId: String? = null

    suspend fun markRead(roomId: String, messageId: String) {
        mutex.withLock {
            if (lastReadRoomId == roomId && lastReadMessageId == messageId) return@withLock
            markChatReadUseCase(roomId, messageId).onSuccess {
                lastReadRoomId = roomId
                lastReadMessageId = messageId
            }.onFailure {
                Timber.e(it)
            }
        }
    }
}
