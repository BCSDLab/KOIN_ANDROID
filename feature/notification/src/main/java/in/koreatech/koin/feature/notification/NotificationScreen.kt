package `in`.koreatech.koin.feature.notification

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.navigationBars
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.windowInsetsPadding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.Icon
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import `in`.koreatech.koin.core.designsystem.component.snackbar.CustomSnackBarHost
import `in`.koreatech.koin.core.designsystem.component.topbar.KoinTopAppBar
import `in`.koreatech.koin.core.designsystem.noRippleClickable
import `in`.koreatech.koin.core.designsystem.theme.RebrandKoinTheme
import `in`.koreatech.koin.core.navigation.utils.rememberNavigator
import `in`.koreatech.koin.feature.notification.component.NotificationItem
import `in`.koreatech.koin.feature.notification.model.LocalNotification
import kotlinx.collections.immutable.ImmutableList
import kotlinx.coroutines.launch
import org.orbitmvi.orbit.compose.collectAsState
import org.orbitmvi.orbit.compose.collectSideEffect

@Composable
fun NotificationScreen(
    onBack: () -> Unit = {},
    viewModel: NotificationViewModel = hiltViewModel()
) {
    val uiState by viewModel.collectAsState()
    val snackbarHostState = remember { SnackbarHostState() }
    val listState = rememberLazyListState()

    val coroutineScope = rememberCoroutineScope()
    val context = LocalContext.current
    val navigator = rememberNavigator()

    viewModel.collectSideEffect { sideEffect ->
        when (sideEffect) {
            is NotificationSideEffect.Error -> {
                snackbarHostState.showSnackbar(context.getString(R.string.notification_error))
            }

            is NotificationSideEffect.NavigateTo -> {
                navigator.navigateToScheme(context, sideEffect.url).let(context::startActivity)
            }

            NotificationSideEffect.Deleted -> {
                snackbarHostState.showSnackbar(context.getString(R.string.notification_deleted))
            }

            NotificationSideEffect.NewNotificationReceived -> coroutineScope.launch {
                listState.animateScrollToItem(0)
            }
        }
    }

    Scaffold(
        modifier = Modifier,
        topBar = {
            KoinTopAppBar(
                title = stringResource(R.string.notification_title),
                onNavigationIconClick = onBack,
                actions = {
                    NotificationMenuButton(
                        onMarkAllAsRead = viewModel::readAllNotifications,
                        onDeleteAll = viewModel::deleteAllNotifications
                    )
                }
            )
        },
        bottomBar = {
            Text(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 16.dp)
                    .windowInsetsPadding(WindowInsets.navigationBars),
                text = stringResource(R.string.notification_auto_delete_info),
                style = RebrandKoinTheme.typography.regular14,
                color = Color(0xFFCACACA),
                textAlign = TextAlign.Center
            )
        },
        snackbarHost = {
            CustomSnackBarHost(
                hotState = snackbarHostState,
                background = Color(0xCC727272)
            )
        },
        containerColor = Color.White
    ) { paddingValues ->
        NotificationScreenImpl(
            modifier = Modifier.padding(paddingValues),
            listState = listState,
            notifications = uiState.notifications,
            onNotificationClick = viewModel::onNotificationClick,
            onDelete = viewModel::deleteNotification
        )
    }
}

@Composable
private fun NotificationMenuButton(
    onMarkAllAsRead: () -> Unit,
    onDeleteAll: () -> Unit
) {
    var expanded by remember { mutableStateOf(false) }

    Icon(
        imageVector = Icons.Default.MoreVert,
        contentDescription = null,
        modifier = Modifier
            .size(24.dp)
            .noRippleClickable { expanded = true }
    )
    DropdownMenu(
        expanded = expanded,
        containerColor = Color(0xFFFAFAFA),
        onDismissRequest = { expanded = false }
    ) {
        DropdownMenuItem(
            text = {
                Text(
                    text = stringResource(R.string.notification_mark_all_read),
                    style = RebrandKoinTheme.typography.regular13
                )
            },
            onClick = {
                onMarkAllAsRead()
                expanded = false
            }
        )
        DropdownMenuItem(
            text = {
                Text(
                    text = stringResource(R.string.notification_delete_all),
                    style = RebrandKoinTheme.typography.regular13,
                    color = Color(0xFFEC2D30)
                )
            },
            onClick = {
                onDeleteAll()
                expanded = false
            }
        )
    }
}

@Composable
private fun NotificationScreenImpl(
    listState: LazyListState,
    notifications: ImmutableList<LocalNotification>,
    onNotificationClick: (LocalNotification) -> Unit,
    onDelete: (Int) -> Unit,
    modifier: Modifier = Modifier
) {
    if (notifications.isEmpty()) {
        Column(
            modifier = modifier.fillMaxSize(),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Image(
                imageVector = ImageVector.vectorResource(R.drawable.ic_bbiko_sleep),
                contentDescription = null
            )

            Text(
                text = stringResource(R.string.notification_empty),
                style = RebrandKoinTheme.typography.medium18.merge(
                    color = RebrandKoinTheme.colors.primary500
                )
            )
        }
        return
    }

    LazyColumn(
        modifier = modifier.fillMaxSize(),
        state = listState
    ) {
        items(notifications, key = { it.id }) { notification ->
            NotificationItem(
                modifier = Modifier.animateItem(),
                type = notification.type,
                title = notification.title,
                content = notification.content,
                timestamp = notification.datetimeDiff,
                isRead = notification.isRead,
                onDelete = remember(notification.id) { { onDelete(notification.id) } },
                onClick = remember(notification.id) { { onNotificationClick(notification) } }
            )
        }
    }
}
