package `in`.koreatech.koin.feature.setting.ui.notification

import android.content.Intent
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
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.core.content.ContextCompat.startActivity
import androidx.core.net.toUri
import androidx.hilt.navigation.compose.hiltViewModel
import `in`.koreatech.koin.core.designsystem.component.topbar.KoinTopAppBar
import `in`.koreatech.koin.core.designsystem.theme.KoinTheme
import `in`.koreatech.koin.domain.model.notification.SubscribesDetailType
import `in`.koreatech.koin.domain.model.notification.SubscribesType
import `in`.koreatech.koin.feature.setting.R
import `in`.koreatech.koin.feature.setting.component.SettingTitle
import `in`.koreatech.koin.feature.setting.constant.URLConstant
import `in`.koreatech.koin.feature.setting.ui.notification.component.NotificationItem
import `in`.koreatech.koin.feature.setting.ui.notification.component.NotificationSwitchItem
import `in`.koreatech.koin.feature.setting.ui.notification.component.NotificationSwitchSubItem

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun NotificationScreen(
    modifier: Modifier = Modifier,
    viewModel: NotificationViewModel = hiltViewModel(),
    onTopbarBackClick: () -> Unit = {}
) {
    LaunchedEffect(Unit) {
        viewModel.getPermissionInfo()
    }
    val notificationState by viewModel.notificationUiState.collectAsState()

    var isMarketingSubscribedInit by remember { mutableStateOf(false) }
    var isSoldoutSubscribedInit by remember { mutableStateOf(false) }
    var isBreakfastSubscribedInit by remember { mutableStateOf(false) }
    var isLaunchSubscribedInit by remember { mutableStateOf(false) }
    var isDinnerSubscribedInit by remember { mutableStateOf(false) }
    var isDiningImageUploadedSubscribedInit by remember { mutableStateOf(false) }
    var isChatSubscribedInit by remember { mutableStateOf(false) }
    var isEventSubscribedInit by remember { mutableStateOf(false) }
    var isReviewSubscribedInit by remember { mutableStateOf(false) }

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
        when (notificationState) {
            is NotificationUiState.Success -> {
                val notificationInfo = remember { (notificationState as NotificationUiState.Success).notificationPermissionInfo }
                notificationInfo.subscribes.forEach {
                    when (it.type) {
                        SubscribesType.MARKETING -> isMarketingSubscribedInit = it.isPermit
                        SubscribesType.DINING_SOLD_OUT -> {
                            isSoldoutSubscribedInit = it.isPermit
                            it.detailSubscribes.forEach { detail ->
                                when (detail.type) {
                                    SubscribesDetailType.BREAKFAST -> isBreakfastSubscribedInit = detail.isPermit
                                    SubscribesDetailType.LUNCH -> isLaunchSubscribedInit = detail.isPermit
                                    SubscribesDetailType.DINNER -> isDinnerSubscribedInit = detail.isPermit
                                    SubscribesDetailType.NOTHING -> {}
                                }
                            }
                        }
                        SubscribesType.DINING_IMAGE_UPLOAD -> isDiningImageUploadedSubscribedInit = it.isPermit
                        SubscribesType.LOST_ITEM_CHAT -> isChatSubscribedInit = it.isPermit
                        SubscribesType.SHOP_EVENT -> isEventSubscribedInit = it.isPermit
                        SubscribesType.REVIEW_PROMPT -> isReviewSubscribedInit = it.isPermit
                        else -> {}
                    }
                }
                NotificationScreenImpl(
                    isMarketingSubscribed = isMarketingSubscribedInit,
                    isSoldoutSubscribed = isSoldoutSubscribedInit,
                    isBreakfastSubscribed = isBreakfastSubscribedInit,
                    isLaunchSubscribed = isLaunchSubscribedInit,
                    isDinnerSubscribed = isDinnerSubscribedInit,
                    isDiningImageUploadedSubscribed = isDiningImageUploadedSubscribedInit,
                    isChatSubscribed = isChatSubscribedInit,
                    isEventSubscribed = isEventSubscribedInit,
                    isReviewSubscribed = isReviewSubscribedInit,
                    updateSubscription = viewModel::updateSubscription,
                    deleteSubscription = viewModel::deleteSubscription,
                    updateSubscriptionDetail = viewModel::updateSubscriptionDetail,
                    deleteSubscriptionDetail = viewModel::deleteSubscriptionDetail,
                    modifier = modifier
                        .padding(contentPadding)
                        .consumeWindowInsets(contentPadding)
                        .systemBarsPadding()
                )
            }
            else -> {}
        }
    }
}

@Composable
private fun NotificationScreenImpl(
    isMarketingSubscribed: Boolean,
    isSoldoutSubscribed: Boolean,
    isBreakfastSubscribed: Boolean,
    isLaunchSubscribed: Boolean,
    isDinnerSubscribed: Boolean,
    isDiningImageUploadedSubscribed: Boolean,
    isChatSubscribed: Boolean,
    isEventSubscribed: Boolean,
    isReviewSubscribed: Boolean,
    modifier: Modifier = Modifier,
    updateSubscription: (SubscribesType) -> Unit = {},
    deleteSubscription: (SubscribesType) -> Unit = {},
    updateSubscriptionDetail: (SubscribesDetailType) -> Unit = {},
    deleteSubscriptionDetail: (SubscribesDetailType) -> Unit = {}
) {
    val context = LocalContext.current
    var isMarketingSubscribed by remember { mutableStateOf(isMarketingSubscribed) }
    var isSoldoutSubscribed by remember { mutableStateOf(isSoldoutSubscribed) }
    var isBreakfastSubscribed by remember { mutableStateOf(isBreakfastSubscribed) }
    var isLaunchSubscribed by remember { mutableStateOf(isLaunchSubscribed) }
    var isDinnerSubscribed by remember { mutableStateOf(isDinnerSubscribed) }
    var isDiningImageUploadedSubscribed by remember { mutableStateOf(isDiningImageUploadedSubscribed) }
    var isChatSubscribed by remember { mutableStateOf(isChatSubscribed) }
    var isEventSubscribed by remember { mutableStateOf(isEventSubscribed) }
    var isReviewSubscribed by remember { mutableStateOf(isReviewSubscribed) }
    LazyColumn(
        modifier = modifier
            .fillMaxSize()
    ) {
        item {
            NotificationSwitchItem(
                text = stringResource(R.string.notification_item_marketing),
                description = stringResource(R.string.notification_item_marketing_description),
                checked = isMarketingSubscribed,
                onClick = {
                    if (isMarketingSubscribed) {
                        deleteSubscription(SubscribesType.MARKETING)
                    } else {
                        updateSubscription(SubscribesType.MARKETING)
                    }
                    isMarketingSubscribed = !isMarketingSubscribed
                }
            )
        }
        item {
            SettingTitle(
                text = stringResource(R.string.notification_item_dining)
            )
            NotificationSwitchItem(
                text = stringResource(R.string.notification_item_dining_soldout),
                description = stringResource(R.string.notification_item_dining_soldout_description),
                checked = isSoldoutSubscribed,
                onClick = {
                    if (isSoldoutSubscribed) {
                        deleteSubscription(SubscribesType.DINING_SOLD_OUT)
                    } else {
                        updateSubscription(SubscribesType.DINING_SOLD_OUT)
                    }
                    isSoldoutSubscribed = !isSoldoutSubscribed
                }
            )
            if (isSoldoutSubscribed) {
                NotificationSwitchSubItem(
                    text = stringResource(R.string.notification_item_dining_breakfast),
                    checked = isBreakfastSubscribed,
                    onClick = {
                        if (isBreakfastSubscribed) {
                            deleteSubscriptionDetail(SubscribesDetailType.BREAKFAST)
                        } else {
                            updateSubscriptionDetail(SubscribesDetailType.BREAKFAST)
                        }
                        isBreakfastSubscribed = !isBreakfastSubscribed
                    }
                )
                NotificationSwitchSubItem(
                    text = stringResource(R.string.notification_item_dining_launch),
                    checked = isLaunchSubscribed,
                    onClick = {
                        if (isLaunchSubscribed) {
                            deleteSubscriptionDetail(SubscribesDetailType.LUNCH)
                        } else {
                            updateSubscriptionDetail(SubscribesDetailType.LUNCH)
                        }
                        isLaunchSubscribed = !isLaunchSubscribed
                    }
                )
                NotificationSwitchSubItem(
                    text = stringResource(R.string.notification_item_dining_dinner),
                    checked = isDinnerSubscribed,
                    onClick = {
                        if (isDinnerSubscribed) {
                            deleteSubscriptionDetail(SubscribesDetailType.DINNER)
                        } else {
                            updateSubscriptionDetail(SubscribesDetailType.DINNER)
                        }
                        isDinnerSubscribed = !isDinnerSubscribed
                    }
                )
            }
            NotificationSwitchItem(
                text = stringResource(R.string.notification_item_dining_image_uploaded),
                description = stringResource(R.string.notification_item_dining_image_uploaded_description),
                checked = isDiningImageUploadedSubscribed,
                onClick = {
                    if (isDiningImageUploadedSubscribed) {
                        deleteSubscription(SubscribesType.DINING_IMAGE_UPLOAD)
                    } else {
                        updateSubscription(SubscribesType.DINING_IMAGE_UPLOAD)
                    }
                    isDiningImageUploadedSubscribed = !isDiningImageUploadedSubscribed
                }
            )
        }
        item {
            SettingTitle(
                text = stringResource(R.string.notification_item_article)
            )
            NotificationItem(
                text = stringResource(R.string.notification_item_article_keyword),
                description = stringResource(R.string.notification_item_article_keyword_description),
                onClick = {
                    context.startActivity(Intent(Intent.ACTION_VIEW, URLConstant.articleKeyword.toUri()))
                }
            )
            NotificationSwitchItem(
                text = stringResource(R.string.notification_item_chat),
                description = stringResource(R.string.notification_item_chat_description),
                checked = isChatSubscribed,
                onClick = {
                    if (isChatSubscribed) {
                        deleteSubscription(SubscribesType.LOST_ITEM_CHAT)
                    } else {
                        updateSubscription(SubscribesType.LOST_ITEM_CHAT)
                    }
                    isChatSubscribed = !isChatSubscribed
                }
            )
        }
        item {
            SettingTitle(
                text = stringResource(R.string.notification_item_nearby)
            )
            NotificationSwitchItem(
                text = stringResource(R.string.notification_item_nearby_event),
                description = stringResource(R.string.notification_item_nearby_event_description),
                checked = isEventSubscribed,
                onClick = {
                    if (isEventSubscribed) {
                        deleteSubscription(SubscribesType.SHOP_EVENT)
                    } else {
                        updateSubscription(SubscribesType.SHOP_EVENT)
                    }
                    isEventSubscribed = !isEventSubscribed
                }
            )
            NotificationSwitchItem(
                text = stringResource(R.string.notification_item_nearby_review),
                description = stringResource(R.string.notification_item_nearby_review_description),
                checked = isReviewSubscribed,
                onClick = {
                    if (isReviewSubscribed) {
                        deleteSubscription(SubscribesType.REVIEW_PROMPT)
                    } else {
                        updateSubscription(SubscribesType.REVIEW_PROMPT)
                    }
                    isReviewSubscribed = !isReviewSubscribed
                }
            )
        }
    }
}

@Preview(showBackground = true)
@Composable
private fun NotificationScreenPreview() {
    NotificationScreenImpl(
        isMarketingSubscribed = false,
        isSoldoutSubscribed = true,
        isBreakfastSubscribed = false,
        isLaunchSubscribed = false,
        isDinnerSubscribed = false,
        isDiningImageUploadedSubscribed = false,
        isChatSubscribed = false,
        isEventSubscribed = false,
        isReviewSubscribed = false
    )
}
