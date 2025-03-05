package `in`.koreatech.koin.data.repository

import `in`.koreatech.koin.data.mapper.toChatMessageRequest
import `in`.koreatech.koin.data.response.chat.toChatListItem
import `in`.koreatech.koin.data.source.remote.ChatRemoteDataSource
import `in`.koreatech.koin.domain.model.chat.ChatListItem
import `in`.koreatech.koin.domain.model.chat.ChatMessage
import `in`.koreatech.koin.domain.model.chat.ChatRoom
import `in`.koreatech.koin.domain.repository.ChatRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject

class ChatRepositoryImpl
    @Inject
    constructor(
        private val chatRemoteDataSource: ChatRemoteDataSource,
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

        override suspend fun getChatRoomFromArticleId(articleId: Int): Flow<ChatRoom> {
            return flow {
                emit(chatRemoteDataSource.getChatRoomFromArticleId(articleId).toChatRoom())
            }
        }

        override suspend fun getChatRoom(
            articleId: Int,
            chatRoomId: Int,
        ): Flow<ChatRoom> {
            return flow {
                emit(chatRemoteDataSource.getChatRoom(articleId, chatRoomId).toChatRoom())
            }
        }

        override suspend fun getChatMessages(
            articleId: Int,
            chatRoomId: Int,
        ): Flow<List<ChatMessage>> {
            return flow {
                emit(
                    chatRemoteDataSource.getChatMessages(articleId, chatRoomId)
                        .map { it.toChatMessage() },
                )
            }
        }

        override fun subscribeChatRoom(
            articleId: Int,
            chatRoomId: Int,
        ): Flow<ChatMessage> {
            return chatRemoteDataSource.subscribeChatRoom(articleId, chatRoomId).map { it.toChatMessage() }
        }

        override suspend fun sendMessage(
            articleId: Int,
            chatRoomId: Int,
            message: ChatMessage,
        ) {
            chatRemoteDataSource.sendMessage(articleId, chatRoomId, message.toChatMessageRequest())
        }

        override suspend fun blockUser(
            articleId: Int,
            chatRoomId: Int,
        ): Result<Unit> {
            return chatRemoteDataSource.blockUser(articleId, chatRoomId)
        }
    }
