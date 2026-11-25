package `in`.koreatech.koin.feature.chat.ui.model

import android.net.Uri
import androidx.compose.runtime.Immutable
import dagger.hilt.android.scopes.ViewModelScoped
import `in`.koreatech.koin.domain.model.chat.ChatDomainType
import `in`.koreatech.koin.domain.model.chat.ChatRoomInfo
import `in`.koreatech.koin.domain.model.chat.ChatRoomMessage
import `in`.koreatech.koin.domain.model.upload.PreSignedUrlDomain
import `in`.koreatech.koin.domain.usecase.chat.GetChatRoomMessagesUseCase
import `in`.koreatech.koin.domain.usecase.chat.SendChatMessageUseCase
import `in`.koreatech.koin.domain.usecase.presignedurl.UploadImageUseCase
import `in`.koreatech.koin.domain.usecase.user.GetUserStatusUseCase
import `in`.koreatech.koin.domain.util.suspendRunCatching
import `in`.koreatech.koin.feature.chat.ui.mapper.toChatMessageGroups
import `in`.koreatech.koin.feature.chat.util.userId
import java.time.LocalTime
import java.time.format.DateTimeFormatter
import java.util.UUID
import javax.inject.Inject
import kotlinx.collections.immutable.ImmutableList
import kotlinx.collections.immutable.PersistentList
import kotlinx.collections.immutable.persistentListOf
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.sync.Mutex
import kotlinx.coroutines.sync.withLock
import timber.log.Timber

@Immutable
internal data class ChatMessageState(
    val messages: ImmutableList<ChatMessageGroup> = persistentListOf(),
    val uploadingImages: PersistentList<ChatMessage> = persistentListOf()
)

internal class ChatImageUploadException(cause: Throwable) : RuntimeException(cause)

@ViewModelScoped
internal class ChatMessageSession @Inject constructor(
    private val getMessages: GetChatRoomMessagesUseCase,
    private val sendMessage: SendChatMessageUseCase,
    private val uploadImage: UploadImageUseCase,
    private val getUserStatus: GetUserStatusUseCase
) {
    private val store = ChatMessageStore()
    private val mutex = Mutex()
    private val mutableState = MutableStateFlow(ChatMessageState())
    private var room: ChatRoomInfo? = null
    private var currentUserId: Int = 0
    private var mappedSnapshot: List<ChatRoomMessage> = emptyList()
    val state: StateFlow<ChatMessageState> = mutableState.asStateFlow()
    val hasHistoryCursor: Boolean get() = store.afterMessageId != null

    suspend fun observeCurrentUser() {
        getUserStatus().catch { Timber.e(it) }.collect { user ->
            val userId = user.userId()
            mutex.withLock {
                if (currentUserId == userId) return@withLock
                currentUserId = userId
                val groups = mappedSnapshot.toChatMessageGroups(currentUserId)
                mutableState.update { it.copy(messages = groups) }
            }
        }
    }

    fun bind(room: ChatRoomInfo) {
        this.room = room
    }

    suspend fun loadHistory(): Result<Unit> = mutex.withLock {
        val room = checkNotNull(room)
        suspendRunCatching {
            val cursor = store.afterMessageId
            val history = if (cursor == null) loadInitialHistory(room.roomId) else loadNewerMessages(room.roomId, cursor)
            if (history.isNotEmpty()) publish(store.mergeHistory(history))
        }
    }

    private suspend fun loadInitialHistory(roomId: String): List<ChatRoomMessage> {
        var page = getMessages(roomId, limit = HISTORY_PAGE_SIZE).getOrThrow()
        val pages = mutableListOf(page)
        while (page.size == HISTORY_PAGE_SIZE) {
            page = getMessages(roomId, beforeMessageId = page.first().messageId, limit = HISTORY_PAGE_SIZE).getOrThrow()
            pages.add(page)
        }
        val messages = ArrayList<ChatRoomMessage>(pages.sumOf { it.size })
        for (index in pages.lastIndex downTo 0) {
            messages.addAll(pages[index])
        }
        return messages
    }

    private suspend fun loadNewerMessages(roomId: String, afterMessageId: String): List<ChatRoomMessage> {
        val messages = mutableListOf<ChatRoomMessage>()
        var cursor = afterMessageId
        do {
            val page = getMessages(roomId, afterMessageId = cursor, limit = HISTORY_PAGE_SIZE).getOrThrow()
            messages.addAll(page)
            if (page.size < HISTORY_PAGE_SIZE) break
            cursor = page.last().messageId
        } while (true)
        return messages
    }

    suspend fun accept(message: ChatRoomMessage) {
        mutex.withLock { publish(store.mergeMessage(message)) }
    }

    suspend fun send(content: String): Result<Unit> {
        val room = checkNotNull(room)
        return sendMessage(room.roomId, content, false).map { accept(it) }
    }

    suspend fun upload(fileSize: Long, fileType: String, fileName: String, imageUri: Uri): Result<Unit> {
        val room = checkNotNull(room)
        val pending = ChatMessage(
            id = "upload-${UUID.randomUUID()}",
            content = imageUri.toString(),
            timestamp = LocalTime.now().format(CHAT_TIME_FORMATTER),
            isSentByMe = true,
            isImage = true,
            userId = currentUserId
        )
        mutableState.update { it.copy(uploadingImages = it.uploadingImages.add(pending)) }
        try {
            val domain = when (room.domainType) {
                ChatDomainType.LOST_ITEM -> PreSignedUrlDomain.LOST_AND_FOUND
                ChatDomainType.CALLVAN -> PreSignedUrlDomain.CALLVAN_CHAT
                ChatDomainType.TEAM_RECRUITMENT -> PreSignedUrlDomain.TEAM_RECRUITMENT
            }
            val imageUrl = uploadImage(domain, fileSize, fileType, fileName, pending.content).getOrElse {
                return Result.failure(ChatImageUploadException(it))
            }
            return sendMessage(room.roomId, imageUrl, true).map { accept(it) }
        } finally {
            mutableState.update { it.copy(uploadingImages = it.uploadingImages.remove(pending)) }
        }
    }

    private fun publish(messages: List<ChatRoomMessage>) {
        if (messages === mappedSnapshot) return
        mappedSnapshot = messages
        val groups = messages.toChatMessageGroups(currentUserId)
        mutableState.update { it.copy(messages = groups) }
    }

    private companion object {
        const val HISTORY_PAGE_SIZE = 200
        val CHAT_TIME_FORMATTER: DateTimeFormatter = DateTimeFormatter.ofPattern("HH:mm")
    }
}
