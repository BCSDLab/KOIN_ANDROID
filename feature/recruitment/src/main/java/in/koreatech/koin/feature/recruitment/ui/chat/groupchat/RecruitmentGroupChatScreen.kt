package `in`.koreatech.koin.feature.recruitment.ui.chat.groupchat

import androidx.activity.compose.LocalOnBackPressedDispatcherOwner
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.exclude
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.ime
import androidx.compose.foundation.layout.imePadding
import androidx.compose.foundation.layout.navigationBars
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material3.Icon
import androidx.compose.material3.Scaffold
import androidx.compose.material3.ScaffoldDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import `in`.koreatech.koin.core.designsystem.theme.RebrandKoinTheme
import `in`.koreatech.koin.feature.recruitment.R
import `in`.koreatech.koin.feature.recruitment.ui.chat.components.RecruitmentChatDateChip
import `in`.koreatech.koin.feature.recruitment.ui.chat.components.RecruitmentChatInput
import `in`.koreatech.koin.feature.recruitment.ui.chat.components.RecruitmentChatMessageBubble
import `in`.koreatech.koin.feature.recruitment.ui.chat.components.RecruitmentChatTopBar
import `in`.koreatech.koin.feature.recruitment.ui.chat.model.RecruitmentChatMessageGroup
import kotlinx.collections.immutable.ImmutableList
import kotlinx.collections.immutable.persistentListOf
import org.orbitmvi.orbit.compose.collectAsState

@Composable
fun RecruitmentGroupChatScreen(
    viewModel: RecruitmentGroupChatViewModel = hiltViewModel()
) {
    val uiState by viewModel.collectAsState()
    val onBackPressedDispatcher = LocalOnBackPressedDispatcherOwner.current?.onBackPressedDispatcher

    RecruitmentGroupChatScreenImpl(
        title = uiState.title,
        currentMemberCount = uiState.currentMemberCount,
        maxMemberCount = uiState.maxMemberCount,
        date = uiState.date,
        messages = uiState.messages,
        chatInputValue = uiState.chatInputValue,
        onNavigationIconClick = { onBackPressedDispatcher?.onBackPressed() },
        onChatInputValueChange = viewModel::onChatInputValueChange,
        onImageButtonClick = {},
        onSendClick = viewModel::sendMessage
    )
}

@Composable
private fun RecruitmentGroupChatScreenImpl(
    title: String,
    currentMemberCount: Int,
    maxMemberCount: Int,
    date: String,
    messages: ImmutableList<RecruitmentChatMessageGroup>,
    chatInputValue: String,
    modifier: Modifier = Modifier,
    onNavigationIconClick: () -> Unit = {},
    onChatInputValueChange: (String) -> Unit = {},
    onImageButtonClick: () -> Unit = {},
    onSendClick: () -> Unit = {}
) {
    val scrollState = rememberLazyListState()
    val latestMessageId = messages.lastOrNull()?.messages?.lastOrNull()?.id

    Scaffold(
        modifier = modifier.imePadding(),
        topBar = {
            RecruitmentChatTopBar(
                title = title,
                onNavigationIconClick = onNavigationIconClick,
                actions = {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Icon(
                            imageVector = ImageVector.vectorResource(id = R.drawable.ic_recruitment_chat_person_count),
                            contentDescription = stringResource(id = R.string.recruitment_chat_member_count_icon),
                            modifier = Modifier.size(16.dp),
                            tint = RebrandKoinTheme.colors.neutral600
                        )
                        Spacer(modifier = Modifier.width(4.dp))
                        Text(
                            text = stringResource(
                                R.string.recruitment_chat_toolbar_participants,
                                currentMemberCount,
                                maxMemberCount
                            ),
                            style = RebrandKoinTheme.typography.regular12,
                            color = RebrandKoinTheme.colors.neutral600
                        )
                        Spacer(modifier = Modifier.width(20.dp))
                    }
                }
            )
        },
        contentWindowInsets = ScaffoldDefaults.contentWindowInsets
            .exclude(WindowInsets.navigationBars)
            .exclude(WindowInsets.ime),
        containerColor = RebrandKoinTheme.colors.neutral0
    ) { contentPadding ->
        Column(
            modifier = Modifier
                .padding(contentPadding)
                .fillMaxSize()
        ) {
            RecruitmentChatDateChip(date = date)

            LaunchedEffect(latestMessageId) {
                if (latestMessageId != null && scrollState.firstVisibleItemIndex < 3) {
                    scrollState.animateScrollToItem(0)
                }
            }

            LazyColumn(
                modifier = Modifier
                    .fillMaxWidth()
                    .weight(1f)
                    .background(RebrandKoinTheme.colors.neutral0),
                state = scrollState,
                reverseLayout = true,
                verticalArrangement = Arrangement.Top
            ) {
                messages.asReversed().forEach { group ->
                    items(
                        items = group.messages.asReversed(),
                        key = { message -> message.id }
                    ) { message ->
                        RecruitmentChatMessageBubble(
                            content = message.content,
                            timestamp = message.timestamp,
                            isSentByMe = message.isSentByMe,
                            authorNickname = if (message.isFirstInGroup) message.authorNickname else null
                        )
                    }
                }
            }

            RecruitmentChatInput(
                value = chatInputValue,
                onValueChange = onChatInputValueChange,
                onImageButtonClick = onImageButtonClick,
                onSendClick = onSendClick
            )
        }
    }
}

@Preview(showBackground = true)
@Composable
private fun RecruitmentGroupChatScreenPreview() {
    RebrandKoinTheme {
        RecruitmentGroupChatScreenImpl(
            title = RecruitmentGroupChatPreviewData.TITLE,
            currentMemberCount = RecruitmentGroupChatPreviewData.CURRENT_MEMBER_COUNT,
            maxMemberCount = RecruitmentGroupChatPreviewData.MAX_MEMBER_COUNT,
            date = RecruitmentGroupChatPreviewData.DATE,
            messages = RecruitmentGroupChatPreviewData.messages(),
            chatInputValue = ""
        )
    }
}

@Preview(showBackground = true)
@Composable
private fun RecruitmentGroupChatScreenEmptyPreview() {
    RebrandKoinTheme {
        RecruitmentGroupChatScreenImpl(
            title = RecruitmentGroupChatPreviewData.TITLE,
            currentMemberCount = RecruitmentGroupChatPreviewData.CURRENT_MEMBER_COUNT,
            maxMemberCount = RecruitmentGroupChatPreviewData.MAX_MEMBER_COUNT,
            date = RecruitmentGroupChatPreviewData.DATE,
            messages = persistentListOf(),
            chatInputValue = ""
        )
    }
}
