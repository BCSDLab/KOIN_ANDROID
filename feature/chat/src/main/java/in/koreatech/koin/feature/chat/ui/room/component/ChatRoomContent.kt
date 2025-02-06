package `in`.koreatech.koin.feature.chat.ui.room.component

import android.net.Uri
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import `in`.koreatech.koin.core.designsystem.theme.KoinTheme
import `in`.koreatech.koin.feature.chat.ui.model.ConvertedChatMessage
import java.time.LocalDate

@Composable
fun ChatRoomContent(
    messages: List<Pair<LocalDate, List<ConvertedChatMessage>>>,
    chatPartnerProfileImage: Uri?,
    chatInputValue: String,
    showBlockDialog: Boolean,
    onBlockUser: () -> Unit,
    onBlockCancel: () -> Unit,
    onChatInputValueChange: (String) -> Unit,
    onImageButtonClick: () -> Unit,
    onSendClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    val scrollState = rememberLazyListState()

    Column(
        modifier = modifier.fillMaxSize()
    ) {
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .background(KoinTheme.colors.neutral0)
                .weight(1f),
            state = scrollState,
            reverseLayout = true
        ) {
            messages.asReversed().forEach { (date, messages) ->
                // Render the message before date because we are using reverse layout
                items(messages.asReversed()) { message ->
                    ChatBubble(
                        message = message,
                        chatPartnerProfileImage = chatPartnerProfileImage
                    )
                }
                item {
                    ChatDateTitle(date)
                }
            }
        }
        ChatInput(
            value = chatInputValue,
            onValueChange = onChatInputValueChange,
            onImageButtonClick = onImageButtonClick,
            onSendClick = onSendClick
        )
        if (showBlockDialog) {
            ChatBlockDialog(
                onBlockUser = onBlockUser,
                onBlockCancel = onBlockCancel
            )
        }
    }
}
