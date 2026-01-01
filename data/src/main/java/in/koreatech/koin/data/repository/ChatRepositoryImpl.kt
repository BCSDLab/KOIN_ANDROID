package `in`.koreatech.koin.data.repository

import `in`.koreatech.koin.data.mapper.toChatMessageRequest
import `in`.koreatech.koin.data.response.chat.toChatListItem
import `in`.koreatech.koin.data.source.remote.ChatRemoteDataSource
import `in`.koreatech.koin.domain.error.chat.KoinChatException
import `in`.koreatech.koin.domain.model.chat.ChatListItem
import `in`.koreatech.koin.domain.model.chat.ChatMessage
import `in`.koreatech.koin.domain.model.chat.ChatRoom
import `in`.koreatech.koin.domain.repository.ChatRepository
import javax.inject.Inject
import kotlinx.coroutines.CancellationException
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.map
import retrofit2.HttpException

class ChatRepositoryImpl @Inject constructor(
    private val chatRemoteDataSource: ChatRemoteDataSource
) : ChatRepository {
    override suspend fun connectWS() {
        chatRemoteDataSource.connectWS()
    }

    override suspend fun disconnectWS() {
        chatRemoteDataSource.disconnectWS()
    }

    override suspend fun getChatRoomList(): Flow<List<ChatListItem>> {
        return flow {
            emit(chatRemoteDataSource.getChatRoomList().map { it.toChatListItem() })
        }
    }

    override suspend fun getChatRoomFromArticleId(articleId: Int): Result<ChatRoom> {
        return runCatching {
            chatRemoteDataSource.getChatRoomFromArticleId(articleId).toChatRoom()
        }.onFailure {
            return Result.failure(
                when (it) {
                    is HttpException -> {
                        if (it.code() == 403) {
                            KoinChatException.BlockedException()
                        } else {
                            it
                        }
                    }

                    is CancellationException -> throw it
                    else -> it
                }
            )
        }
    }

    override suspend fun getChatRoom(
        articleId: Int,
        chatRoomId: Int
    ): Result<ChatRoom> {
        return runCatching {
            chatRemoteDataSource.getChatRoom(articleId, chatRoomId).toChatRoom()
        }
    }

    override suspend fun getChatMessages(
        articleId: Int,
        chatRoomId: Int
    ): Result<List<ChatMessage>> {
        return runCatching {
            chatRemoteDataSource.getChatMessages(articleId, chatRoomId).map { it.toChatMessage() }
        }.onFailure {
            if (it is CancellationException) {
                throw it
            } else {
                return Result.failure(it)
            }
        }
    }

    override fun subscribeChatRoom(
        articleId: Int,
        chatRoomId: Int
    ): Flow<ChatMessage> {
        return chatRemoteDataSource.subscribeChatRoom(articleId, chatRoomId).map { it.toChatMessage() }
    }

    override fun subscribeChatList(userId: Int): Flow<List<ChatListItem>> {
        return chatRemoteDataSource.subscribeChatList(userId).map { it.map { it.toChatListItem() } }
    }

    override suspend fun sendMessage(
        articleId: Int,
        chatRoomId: Int,
        message: ChatMessage
    ): Result<Unit> {
        return chatRemoteDataSource.sendMessage(articleId, chatRoomId, message.toChatMessageRequest())
    }

    override suspend fun blockUser(
        articleId: Int,
        chatRoomId: Int
    ): Result<Unit> {
        return chatRemoteDataSource.blockUser(articleId, chatRoomId)
    }
}
