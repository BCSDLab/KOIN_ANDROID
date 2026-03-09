package `in`.koreatech.koin.feature.callvan.ui.detail

import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import `in`.koreatech.koin.core.designsystem.component.button.FilledButton
import `in`.koreatech.koin.core.designsystem.component.topbar.KoinTopAppBar
import `in`.koreatech.koin.core.designsystem.theme.KoinTheme
import `in`.koreatech.koin.core.designsystem.theme.RebrandKoinTheme
import `in`.koreatech.koin.feature.callvan.R
import `in`.koreatech.koin.feature.callvan.ui.component.CallvanDropdownMenuItem
import `in`.koreatech.koin.feature.callvan.ui.component.CallvanNotificationIcon
import `in`.koreatech.koin.feature.callvan.ui.component.CallvanParticipantCount
import `in`.koreatech.koin.feature.callvan.ui.component.callvanPersonIconColor
import `in`.koreatech.koin.feature.callvan.ui.detail.component.CallvanDetailParticipantItem
import `in`.koreatech.koin.feature.callvan.ui.detail.component.CallvanDetailRouteCard
import `in`.koreatech.koin.feature.callvan.ui.detail.model.CallvanDetailParticipantUiItem
import kotlinx.collections.immutable.ImmutableList
import kotlinx.collections.immutable.persistentListOf
import org.orbitmvi.orbit.compose.collectAsState

@Composable
fun CallvanDetailScreen(
    viewModel: CallvanDetailViewModel = hiltViewModel(),
    onTopbarBackClick: () -> Unit = {},
    onNotificationClick: () -> Unit = {},
    onEnterChatClick: () -> Unit = {},
    onReportClick: (userId: Int) -> Unit = {}
) {
    val state by viewModel.collectAsState()

    CallvanDetailScreenImpl(
        departure = state.departure,
        destination = state.destination,
        dateTime = state.dateTime,
        currentParticipants = state.currentParticipants,
        maxParticipants = state.maxParticipants,
        participants = state.participants,
        onTopbarBackClick = onTopbarBackClick,
        onNotificationClick = onNotificationClick,
        onEnterChatClick = onEnterChatClick,
        onReportClick = onReportClick
    )
}

@Suppress("LongParameterList")
@Composable
fun CallvanDetailScreenImpl(
    departure: String,
    destination: String,
    dateTime: String,
    currentParticipants: Int,
    maxParticipants: Int,
    participants: ImmutableList<CallvanDetailParticipantUiItem>,
    hasNewNotification: Boolean = false,
    onTopbarBackClick: () -> Unit = {},
    onNotificationClick: () -> Unit = {},
    onEnterChatClick: () -> Unit = {},
    onReportClick: (userId: Int) -> Unit = {}
) {
    Scaffold(
        topBar = {
            KoinTopAppBar(
                title = stringResource(R.string.callvan_detail_top_bar),
                onNavigationIconClick = onTopbarBackClick,
                actions = {
                    IconButton(onClick = onNotificationClick) {
                        CallvanNotificationIcon(hasNewNotification = hasNewNotification)
                    }
                }
            )
        },
        bottomBar = {
            FilledButton(
                text = stringResource(R.string.callvan_detail_enter_chat),
                onClick = onEnterChatClick,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 24.dp, vertical = 16.dp),
                colors = ButtonDefaults.buttonColors(containerColor = RebrandKoinTheme.colors.primary500),
                shape = KoinTheme.shapes.small
            )
        },
        containerColor = KoinTheme.colors.neutral0
    ) { contentPadding ->
        val participantColorIndices = remember(participants) {
            var count = 0
            participants.map { item ->
                if (item.isMe) {
                    5
                } else {
                    count++
                }
            }
        }
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(contentPadding)
                .padding(horizontal = 24.dp)
        ) {
            item {
                Spacer(modifier = Modifier.height(16.dp))
                ParticipantsHeader(
                    currentParticipants = currentParticipants,
                    maxParticipants = maxParticipants
                )
                Spacer(modifier = Modifier.height(12.dp))
                CallvanDetailRouteCard(
                    departure = departure,
                    destination = destination,
                    dateTime = dateTime
                )
                Spacer(modifier = Modifier.height(16.dp))
            }

            itemsIndexed(participants, key = { _, participant -> participant.id }) { index, participant ->
                if (index > 0) {
                    HorizontalDivider(color = KoinTheme.colors.neutral200)
                }
                CallvanDetailParticipantItem(
                    participant = participant,
                    tint = callvanPersonIconColor(participantColorIndices[index]),
                    menuItems = persistentListOf(
                        CallvanDropdownMenuItem(
                            text = { stringResource(R.string.callvan_detail_participant_report) },
                            icon = {
                                Icon(
                                    imageVector = ImageVector.vectorResource(R.drawable.ic_siren),
                                    contentDescription = null
                                )
                            },
                            onClick = {
                                onReportClick(participant.id)
                            }
                        )
                    )
                )
            }
        }
    }
}

@Composable
private fun ParticipantsHeader(
    currentParticipants: Int,
    maxParticipants: Int
) {
    Row(verticalAlignment = Alignment.CenterVertically) {
        Text(
            text = stringResource(R.string.callvan_detail_participants_header),
            style = KoinTheme.typography.medium18,
            color = KoinTheme.colors.neutral800
        )
        Spacer(modifier = Modifier.width(8.dp))
        CallvanParticipantCount(
            currentCount = currentParticipants,
            maxCount = maxParticipants,
            textStyle = KoinTheme.typography.regular14,
            iconSize = 20.dp
        )
    }
}

@Preview(showBackground = true)
@Composable
private fun CallvanDetailScreenPreview() {
    CallvanDetailScreenImpl(
        departure = "테니스장",
        destination = "천안 시외터미널",
        dateTime = "02.05 (월) 14:00",
        currentParticipants = 6,
        maxParticipants = 8,
        participants = persistentListOf(
            CallvanDetailParticipantUiItem(id = 1, name = "홍길동", isMe = true),
            CallvanDetailParticipantUiItem(id = 2, name = "신짱구", isMe = false),
            CallvanDetailParticipantUiItem(id = 3, name = "김철수", isMe = false),
            CallvanDetailParticipantUiItem(id = 4, name = "한유리", isMe = false),
            CallvanDetailParticipantUiItem(id = 5, name = "이훈이", isMe = false),
            CallvanDetailParticipantUiItem(id = 6, name = "맹구", isMe = false)
        )
    )
}

@Preview(showBackground = true)
@Composable
private fun CallvanDetailScreenNotificationPreview() {
    CallvanDetailScreenImpl(
        departure = "테니스장",
        destination = "천안 시외터미널",
        dateTime = "02.05 (월) 14:00",
        currentParticipants = 6,
        maxParticipants = 8,
        hasNewNotification = true,
        participants = persistentListOf(
            CallvanDetailParticipantUiItem(id = 1, name = "홍길동", isMe = true),
            CallvanDetailParticipantUiItem(id = 2, name = "신짱구", isMe = false),
            CallvanDetailParticipantUiItem(id = 3, name = "김철수", isMe = false),
            CallvanDetailParticipantUiItem(id = 4, name = "한유리", isMe = false),
            CallvanDetailParticipantUiItem(id = 5, name = "이훈이", isMe = false),
            CallvanDetailParticipantUiItem(id = 6, name = "맹구", isMe = false)
        )
    )
}
