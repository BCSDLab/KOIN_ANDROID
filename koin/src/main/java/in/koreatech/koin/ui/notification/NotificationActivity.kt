package `in`.koreatech.koin.ui.notification

import android.Manifest
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.provider.Settings
import androidx.activity.viewModels
import androidx.core.content.ContextCompat
import androidx.core.view.isVisible
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import dagger.hilt.android.AndroidEntryPoint
import `in`.koreatech.koin.R
import `in`.koreatech.koin.core.activity.ActivityBase
import `in`.koreatech.koin.core.util.dataBinding
import `in`.koreatech.koin.core.util.setAppBarButtonClickedListener
import `in`.koreatech.koin.databinding.ActivityNotificationBinding
import `in`.koreatech.koin.domain.model.notification.SubscribesDetailType
import `in`.koreatech.koin.domain.model.notification.SubscribesType
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
    }

    override fun onResume() {
        super.onResume()
        if (!checkPermission()) {
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
            handleSubscription(isChecked, SubscribesType.DINING_SOLD_OUT)
            enableSubscriptionDetail(isChecked, SubscribesType.DINING_SOLD_OUT)
        }
        binding.notificationShopEvent.setOnSwitchClickListener { isChecked ->
            handleSubscription(isChecked, SubscribesType.SHOP_EVENT)
        }
        binding.notificationDiningImageUpload.setOnSwitchClickListener { isChecked ->
            handleSubscription(isChecked, SubscribesType.DINING_IMAGE_UPLOAD)
        }
    }

    private fun subscribeDetailNotification() {
        binding.notificationDiningBreakfastSoldOut.setOnSwitchClickListener { isChecked ->
            handleSubscriptionDetail(isChecked, SubscribesDetailType.BREAKFAST)
        }
        binding.notificationDiningLunchSoldOut.setOnSwitchClickListener { isChecked ->
            handleSubscriptionDetail(isChecked, SubscribesDetailType.LUNCH)
        }
        binding.notificationDiningDinnerSoldOut.setOnSwitchClickListener { isChecked ->
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

    private fun checkPermission() = NOTIFICATION_REQUIRED_PERMISSION.all {
        ContextCompat.checkSelfPermission(
            this, it
        ) == PackageManager.PERMISSION_GRANTED
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

        private val NOTIFICATION_REQUIRED_PERMISSION = mutableListOf<String>().apply {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
                add(Manifest.permission.POST_NOTIFICATIONS)
            }
        }.toTypedArray()
    }
}