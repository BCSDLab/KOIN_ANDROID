package `in`.koreatech.koin.feature.setting.ui.notification

import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.consumeWindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.systemBarsPadding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Scaffold
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import `in`.koreatech.koin.core.designsystem.component.topbar.KoinTopAppBar
import `in`.koreatech.koin.core.designsystem.theme.KoinTheme
import `in`.koreatech.koin.feature.setting.R
import `in`.koreatech.koin.feature.setting.component.SettingTitle
import `in`.koreatech.koin.feature.setting.ui.notification.component.NotificationItem
import `in`.koreatech.koin.feature.setting.ui.notification.component.NotificationSwitchItem
import `in`.koreatech.koin.feature.setting.ui.notification.component.NotificationSwitchSubItem

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun NotificationScreen(
    modifier: Modifier = Modifier,
    onTopbarBackClick: () -> Unit = {}
) {
    Scaffold(
        containerColor = KoinTheme.colors.neutral0,
        topBar = {
            KoinTopAppBar(
                title = stringResource(R.string.notification_appbar_title),
                colors = TopAppBarDefaults.centerAlignedTopAppBarColors(
                    containerColor = KoinTheme.colors.primary500,
                    navigationIconContentColor = Color.White,
                    titleContentColor = Color.White,
                    actionIconContentColor = Color.White
                ),
                onNavigationIconClick = onTopbarBackClick
            )
        },
        contentWindowInsets = WindowInsets(0, 0, 0, 0)
    ) { contentPadding ->
        NotificationScreenImpl(
            modifier = modifier
                .padding(contentPadding)
                .consumeWindowInsets(contentPadding)
                .systemBarsPadding()
        )
    }
}

@Composable
private fun NotificationScreenImpl(
    modifier: Modifier = Modifier
) {
    LazyColumn(
        modifier = modifier
            .fillMaxSize()
    ) {
        item {
            NotificationSwitchItem(
                text = stringResource(R.string.notification_item_marketing),
                description = stringResource(R.string.notification_item_marketing_description),
                checked = false, // TODO
                onClick = {} // TODO
            )
        }
        item {
            SettingTitle(
                text = stringResource(R.string.notification_item_dining)
            )
            NotificationSwitchItem(
                text = stringResource(R.string.notification_item_dining_soldout),
                description = stringResource(R.string.notification_item_dining_soldout_description),
                checked = false, // TODO
                onClick = {} // TODO
            )
            NotificationSwitchSubItem(
                text = stringResource(R.string.notification_item_dining_breakfast),
                checked = false, // TODO
                onClick = {} // TODO
            )
            NotificationSwitchSubItem(
                text = stringResource(R.string.notification_item_dining_launch),
                checked = false, // TODO
                onClick = {} // TODO
            )
            NotificationSwitchSubItem(
                text = stringResource(R.string.notification_item_dining_dinner),
                checked = false, // TODO
                onClick = {} // TODO
            )
            NotificationSwitchItem(
                text = stringResource(R.string.notification_item_dining_image_uploaded),
                description = stringResource(R.string.notification_item_dining_image_uploaded_description),
                checked = false, // TODO
                onClick = {} // TODO
            )
        }
        item {
            SettingTitle(
                text = stringResource(R.string.notification_item_article)
            )
            NotificationItem(
                text = stringResource(R.string.notification_item_article_keyword),
                description = stringResource(R.string.notification_item_article_keyword_description),
                onClick = {} // TODO
            )
            NotificationSwitchItem(
                text = stringResource(R.string.notification_item_chat),
                description = stringResource(R.string.notification_item_chat_description),
                checked = false, // TODO
                onClick = {} // TODO
            )
        }
        item {
            SettingTitle(
                text = stringResource(R.string.notification_item_nearby)
            )
            NotificationSwitchItem(
                text = stringResource(R.string.notification_item_nearby_event),
                description = stringResource(R.string.notification_item_nearby_event_description),
                checked = false, // TODO
                onClick = {} // TODO
            )
            NotificationSwitchItem(
                text = stringResource(R.string.notification_item_nearby_review),
                description = stringResource(R.string.notification_item_nearby_review_description),
                checked = false, // TODO
                onClick = {} // TODO
            )
        }
    }
}

@Preview(showBackground = true)
@Composable
private fun NotificationScreenPreview() {
    NotificationScreenImpl()
}
