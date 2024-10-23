package `in`.koreatech.koin.ui.notification

import android.content.Intent
import android.content.Intent.FLAG_ACTIVITY_CLEAR_TOP
import android.net.Uri
import android.os.Bundle
import android.provider.Settings
import androidx.activity.viewModels
import androidx.core.view.isVisible
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import dagger.hilt.android.AndroidEntryPoint
import `in`.koreatech.koin.R
import `in`.koreatech.koin.core.analytics.EventAction
import `in`.koreatech.koin.core.analytics.EventLogger
import `in`.koreatech.koin.core.constant.AnalyticsConstant
import `in`.koreatech.koin.core.permission.checkNotificationPermission
import `in`.koreatech.koin.core.activity.ActivityBase
import `in`.koreatech.koin.core.util.dataBinding
import `in`.koreatech.koin.core.util.setAppBarButtonClickedListener
import `in`.koreatech.koin.databinding.ActivityNotificationBinding
import `in`.koreatech.koin.domain.model.notification.SubscribesDetailType
import `in`.koreatech.koin.domain.model.notification.SubscribesType
import `in`.koreatech.koin.ui.article.ArticleActivity
import `in`.koreatech.koin.ui.notification.viewmodel.NotificationUiState
import `in`.koreatech.koin.ui.notification.viewmodel.NotificationViewModel
import kotlinx.coroutines.launch

@AndroidEntryPoint
class NotificationActivity : ActivityBase() {
    override val screenTitle: String = "알림"
    private val binding by dataBinding<ActivityNotificationBinding>(R.layout.activity_notification)
    private val viewModel: NotificationViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

        binding.koinBaseAppBar.setAppBarButtonClickedListener(
            leftButtonClicked = { onBackPressed() },
            rightButtonClicked = {}
        )

        observeData()
        onSubscribe()
        setOnClickNotificationSetting()
        binding.clGotoArticleKeyword.setOnClickListener {
            val intent = Intent(Intent.ACTION_VIEW).apply {
                data = Uri.parse("koin://article/activity?fragment=article_keyword")
            }
            startActivity(intent)
        }
    }

    override fun onResume() {
        super.onResume()
        if (!checkNotificationPermission()) {
            permissionDenied()
        } else {
            permissionGranted()
        }
    }

    private fun permissionGranted() {
        viewModel.getPermissionInfo()
        with(binding) {
            textViewNotificationSetting.isVisible = false
            notificationDiningSoldOut.isEnabled = true
            notificationShopEvent.isEnabled = true
            notificationDiningImageUpload.isEnabled = true
        }
    }

    private fun permissionDenied() {
        updateDiningSoldOutVisibility(false)
        with(binding) {
            textViewNotificationSetting.isVisible = true
            notificationDiningSoldOut.disableAll()
            notificationShopEvent.disableAll()
            notificationDiningImageUpload.disableAll()
        }
    }

    private fun observeData() {
        lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel.notificationUiState.collect { uiState ->
                    when (uiState) {
                        is NotificationUiState.Success -> {
                            uiState.notificationPermissionInfo.subscribes.forEach {
                                when (it.type) {
                                    SubscribesType.SHOP_EVENT -> with(binding.notificationShopEvent) {
                                        if (isChecked != it.isPermit) {
                                            fakeChecked = it.isPermit
                                            isChecked = it.isPermit
                                        }
                                    }

                                    SubscribesType.DINING_SOLD_OUT -> with(binding.notificationDiningSoldOut) {
                                        if (isChecked != it.isPermit) {
                                            fakeChecked = it.isPermit
                                            isChecked = it.isPermit
                                            updateDiningSoldOutVisibility(it.isPermit)
                                        }
                                    }

                                    SubscribesType.DINING_IMAGE_UPLOAD -> with(binding.notificationDiningImageUpload) {
                                        if (isChecked != it.isPermit) {
                                            fakeChecked = it.isPermit
                                            isChecked = it.isPermit
                                        }
                                    }

                                    SubscribesType.NOTHING -> Unit
                                    else -> Unit
                                }
                            }
                            uiState.notificationPermissionInfo.subscribes.forEach {
                                it.detailSubscribes.forEach { detail ->
                                    when (detail.type) {
                                        SubscribesDetailType.BREAKFAST -> with(binding.notificationDiningBreakfastSoldOut) {
                                            if (isChecked != detail.isPermit) {
                                                fakeChecked = detail.isPermit
                                                isChecked = detail.isPermit
                                            }
                                        }

                                        SubscribesDetailType.LUNCH -> with(binding.notificationDiningLunchSoldOut) {
                                            if (isChecked != detail.isPermit) {
                                                fakeChecked = detail.isPermit
                                                isChecked = detail.isPermit
                                            }
                                        }

                                        SubscribesDetailType.DINNER -> with(binding.notificationDiningDinnerSoldOut) {
                                            if (isChecked != detail.isPermit) {
                                                fakeChecked = detail.isPermit
                                                isChecked = detail.isPermit
                                            }
                                        }

                                        SubscribesDetailType.NOTHING -> Unit
                                    }
                                }
                            }
                        }

                        is NotificationUiState.Failed -> {}
                        is NotificationUiState.Nothing -> {}
                        else -> {}
                    }
                }
            }
        }
    }

    private fun onSubscribe() {
        subscribeNotification()
        subscribeDetailNotification()
    }

    private fun subscribeNotification() {
        binding.notificationDiningSoldOut.setOnSwitchClickListener { isChecked ->
            EventLogger.logClickEvent(
                EventAction.CAMPUS,
                AnalyticsConstant.Label.NOTIFICATION_SOLD_OUT,
                if (isChecked) "on" else "off"
            )
            handleSubscription(isChecked, SubscribesType.DINING_SOLD_OUT)
            enableSubscriptionDetail(isChecked, SubscribesType.DINING_SOLD_OUT)
        }
        binding.notificationShopEvent.setOnSwitchClickListener { isChecked ->
            handleSubscription(isChecked, SubscribesType.SHOP_EVENT)
        }
        binding.notificationDiningImageUpload.setOnSwitchClickListener { isChecked ->
            EventLogger.logClickEvent(
                EventAction.CAMPUS,
                AnalyticsConstant.Label.NOTIFICATION_MENU_IMAGE_UPLOAD,
                if (isChecked) "on" else "off"
            )
            handleSubscription(isChecked, SubscribesType.DINING_IMAGE_UPLOAD)
        }
    }

    private fun subscribeDetailNotification() {
        binding.notificationDiningBreakfastSoldOut.setOnSwitchClickListener { isChecked ->
            EventLogger.logClickEvent(
                EventAction.CAMPUS,
                AnalyticsConstant.Label.NOTIFICATION_BREAKFAST_SOLD_OUT,
                if (isChecked) "on" else "off"
            )
            handleSubscriptionDetail(isChecked, SubscribesDetailType.BREAKFAST)
        }
        binding.notificationDiningLunchSoldOut.setOnSwitchClickListener { isChecked ->
            EventLogger.logClickEvent(
                EventAction.CAMPUS,
                AnalyticsConstant.Label.NOTIFICATION_LUNCH_SOLD_OUT,
                if (isChecked) "on" else "off"
            )
            handleSubscriptionDetail(isChecked, SubscribesDetailType.LUNCH)
        }
        binding.notificationDiningDinnerSoldOut.setOnSwitchClickListener { isChecked ->
            EventLogger.logClickEvent(
                EventAction.CAMPUS,
                AnalyticsConstant.Label.NOTIFICATION_DINNER_SOLD_OUT,
                if (isChecked) "on" else "off"
            )
            handleSubscriptionDetail(isChecked, SubscribesDetailType.DINNER)
        }
    }

    private fun handleSubscription(isChecked: Boolean, type: SubscribesType) {
        if (isChecked) viewModel.updateSubscription(type)
        else viewModel.deleteSubscription(type)
    }

    private fun handleSubscriptionDetail(isChecked: Boolean, type: SubscribesDetailType) {
        if (isChecked) viewModel.updateSubscriptionDetail(type)
        else viewModel.deleteSubscriptionDetail(type)
    }

    private fun enableSubscriptionDetail(isChecked: Boolean, subscribesType: SubscribesType) {
        if (subscribesType == SubscribesType.DINING_SOLD_OUT) {
            updateDiningSoldOutVisibility(isChecked)
        }
    }

    private fun updateDiningSoldOutVisibility(isChecked: Boolean) {
        binding.notificationDiningBreakfastSoldOut.isVisible = isChecked
        binding.notificationDiningLunchSoldOut.isVisible = isChecked
        binding.notificationDiningDinnerSoldOut.isVisible = isChecked
    }

    private fun setOnClickNotificationSetting() {
        binding.textViewNotificationSetting.setOnClickListener {
            intentAppSettings()
        }
    }

    private fun intentAppSettings() {
        val intent = Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS).apply {
            data = Uri.fromParts(PACKAGE, packageName, null)
        }
        startActivity(intent)
    }

    companion object {
        const val PACKAGE = "package"
    }
}