package `in`.koreatech.koin.feature.chat.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.toRoute
import `in`.koreatech.koin.core.navigation.ChatDomain
import `in`.koreatech.koin.feature.chat.ui.chat.ChatScreen
import `in`.koreatech.koin.feature.chat.ui.groupchat.GroupChatScreen
import `in`.koreatech.koin.feature.chat.ui.list.ChatList

@Composable
internal fun ChatNavHost(startDestination: ChatNavType) {
    val navController = rememberNavController()
    NavHost(navController = navController, startDestination = startDestination) {
        composable<ChatNavType.ChatList> { entry ->
            ChatList(
                navigateToChatRoom = { articleId, chatRoomId ->
                    navController.navigate(ChatNavType.Chat(ChatDomain.LOST_ITEM.name, articleId, chatRoomId))
                },
                showBlockedMessage = entry.toRoute<ChatNavType.ChatList>().showBlockedMessage
            )
        }
        composable<ChatNavType.Chat> {
            ChatScreen(
                navigateToChatList = { isBlocked ->
                    navController.navigate(ChatNavType.ChatList(showBlockedMessage = isBlocked)) {
                        popUpTo(navController.graph.id)
                    }
                }
            )
        }
        composable<ChatNavType.GroupChat> {
            GroupChatScreen()
        }
    }
}
