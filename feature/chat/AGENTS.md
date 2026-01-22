# FEATURE Chat Module - AGENTS.md

This file provides prescriptive coding guidelines for AI coding agents working on the FEATURE CHAT module.

## Module Overview

The `feature:chat` module provides real-time messaging functionality using WebSocket (STOMP protocol) for chat rooms.

## Core Responsibilities

1. **Chat Room List**: Display available chat rooms
2. **Real-time Messaging**: Send/receive messages via WebSocket
3. **Message History**: Load and display chat history
4. **User Blocking**: Block/unblock users
5. **Image Upload**: Upload and send images in chat

## Key Patterns

### ViewModel (Orbit MVI)

```kotlin
@HiltViewModel
class ChatRoomViewModel @Inject constructor(
    savedStateHandle: SavedStateHandle,
    private val chatWSConnectUseCase: ChatWSConnectUseCase,
    private val chatWSDisconnectUseCase: ChatWSDisconnectUseCase,
    private val getChatRoomUseCase: GetChatRoomUseCase,
    private val getUserStatusUseCase: GetUserStatusUseCase,
    private val subscribeChatRoomUseCase: SubscribeChatRoomUseCase,
    private val getChatMessageUseCase: GetChatMessageUseCase,
    private val sendMessageUseCase: SendMessageUseCase,
    private val getLostAndFoundPreSignedUrlUseCase: GetLostAndFoundPreSignedUrlUseCase,
    private val uploadFilesUseCase: UploadFileUseCase,
    private val chatBlockUserUseCase: ChatBlockUserUseCase
) : ViewModel(), ContainerHost<ChatRoomState, ChatRoomSideEffect> {
    
    override val container = container<ChatRoomState, ChatRoomSideEffect>(ChatRoomState(), savedStateHandle) {
        val articleId = savedStateHandle.get<Int>(ARTICLE_ID)
        val chatRoomId = savedStateHandle.get<Int>(CHAT_ROOM_ID)
        checkNotNull(articleId)
        getChatRoom(articleId, chatRoomId)
    }

    private val job = SupervisorJob()
    private val coroutineScope = CoroutineScope(Dispatchers.IO + job)
    private val _connectChannel = Channel<Boolean>()
    val connectChannel = _connectChannel.receiveAsFlow()
    
    fun connectToWS() = intent {
        chatWSConnectUseCase().onSuccess {
            subscribeChatRoom(state.articleId, state.chatRoomId)
        }.onFailure { error ->
            if (error is WebSocketReconnectionException) {
                postSideEffect(ChatRoomSideEffect.FailedToConnectWS)
            }
        }
    }
    
    private fun subscribeChatRoom(articleId: Int, chatRoomId: Int) = intent {
        // NOTE: Takes TWO parameters: articleId and chatRoomId
        subscribeChatRoomUseCase(articleId, chatRoomId).catch {
            if (it is UnknownHostException) {
                _connectChannel.send(true)
            }
            // ... error handling
        }.collect { message ->
            reduce {
                state.copy(
                    chatMessage = state.chatMessage.appendMessage(message, state.userId)
                )
            }
        }
    }
    
    fun sendMessage() = intent {
        if (state.chatInputValue.isBlank()) return@intent
        // NOTE: Takes THREE parameters: articleId, chatRoomId, ChatMessage
        sendMessageUseCase(
            state.articleId,
            state.chatRoomId,
            ChatMessage(
                userId = state.userId,
                userNickname = state.userNickName,
                content = state.chatInputValue,
                timestamp = LocalDateTime.now().toString(),
                isImage = false
            )
        )
        reduce {
            state.copy(chatInputValue = "")
        }
    }
    
    fun disconnectWS() = coroutineScope.launch {
        // Use separate coroutineScope (NOT viewModelScope) for disconnect
        // to ensure cleanup happens even after ViewModel is cleared
        chatWSDisconnectUseCase().onFailure {
            if (it !is LostReceiptException) {
                Timber.e(it)
            }
        }
    }
    
    override fun onCleared() {
        super.onCleared()
        disconnectWS()
    }
    
    companion object {
        const val ARTICLE_ID = "article_id"
        const val CHAT_ROOM_ID = "chat_room_id"
    }
}
```

### UseCase Signatures (IMPORTANT)

**SendMessageUseCase** - Takes 3 parameters:
```kotlin
class SendMessageUseCase @Inject constructor(
    private val chatRepository: ChatRepository
) {
    suspend operator fun invoke(
        articleId: Int,
        chatRoomId: Int,
        message: ChatMessage
    ): Result<Unit> = chatRepository.sendMessage(articleId, chatRoomId, message)
}
```

**SubscribeChatRoomUseCase** - Takes 2 parameters:
```kotlin
class SubscribeChatRoomUseCase @Inject constructor(
    private val chatRepository: ChatRepository
) {
    operator fun invoke(
        articleId: Int,
        chatRoomId: Int
    ) = chatRepository.subscribeChatRoom(articleId, chatRoomId)
    // Returns Flow<ChatMessage>
}
```

### Critical Rules

1. **WebSocket Lifecycle**: 
   - Connect via `chatWSConnectUseCase()` 
   - Disconnect via `chatWSDisconnectUseCase()` in `onCleared()`
   - Use separate `CoroutineScope` (NOT `viewModelScope`) for disconnect to ensure cleanup
2. **Message Subscription**: Use `subscribeChatRoomUseCase(articleId, chatRoomId)` with Flow collection
3. **SavedStateHandle**: Use for article/chat room IDs passed via navigation
4. **Error Recovery**: Handle `WebSocketReconnectionException`, `UnknownHostException` for network issues
5. **STOMP Protocol**: Use Krossbow STOMP client (`org.hildan.krossbow`)
6. **Channel for Reconnection**: Use `Channel<Boolean>` to signal reconnection needs

### Side Effects

```kotlin
sealed interface ChatRoomSideEffect {
    data object FailedToConnectWS : ChatRoomSideEffect
    data object BlockUserSuccess : ChatRoomSideEffect
    data object BlockUserFailed : ChatRoomSideEffect
    data object BlockedByUser : ChatRoomSideEffect
    data object FailedToUploadImage : ChatRoomSideEffect
}
```

## Build Commands

```bash
./gradlew :feature:chat:build
./gradlew :feature:chat:test
```

---

**Last Updated**: 2026-01-06  
**For**: AI Coding Agents working on FEATURE CHAT module
