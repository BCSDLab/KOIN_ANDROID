package `in`.koreatech.koin.ui.notification

import android.os.Bundle
import androidx.activity.viewModels
import androidx.core.view.isVisible
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import dagger.hilt.android.AndroidEntryPoint
import `in`.koreatech.koin.R
import `in`.koreatech.koin.core.util.dataBinding
import `in`.koreatech.koin.core.util.setAppBarButtonClickedListener
import `in`.koreatech.koin.databinding.ActivityNotificationBinding
import `in`.koreatech.koin.domain.model.notification.SubscribesDetailType
import `in`.koreatech.koin.domain.model.notification.SubscribesType
import `in`.koreatech.koin.ui.navigation.KoinNavigationDrawerActivity
import `in`.koreatech.koin.ui.navigation.state.MenuState
import `in`.koreatech.koin.ui.notification.viewmodel.NotificationUiState
import `in`.koreatech.koin.ui.notification.viewmodel.NotificationViewModel
import kotlinx.coroutines.launch

@AndroidEntryPoint
class NotificationActivity : KoinNavigationDrawerActivity() {
    override val menuState: MenuState = MenuState.Notification
    private val binding by dataBinding<ActivityNotificationBinding>(R.layout.activity_notification)

    override val screenTitle: String = "알림"

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
    }

    private fun observeData() {
        lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel.notificationUiState.collect { uiState ->
                    when (uiState) {
                        is NotificationUiState.Success -> {
                            uiState.notificationPermissionInfo.subscribes.forEach {
                                when (it.type) {
                                    SubscribesType.SHOP_EVENT -> {
                                        binding.notificationShopEvent.fakeChecked = it.isPermit
                                        binding.notificationShopEvent.isChecked = it.isPermit
                                    }

                                    SubscribesType.DINING_SOLD_OUT -> {
                                        binding.notificationDiningSoldOut.fakeChecked = it.isPermit
                                        binding.notificationDiningSoldOut.isChecked = it.isPermit
                                        updateDiningSoldOutVisibility(it.isPermit)
                                    }

                                    SubscribesType.NOTHING -> Unit
                                }
                                it.detailSubscribes.forEach { detail ->
                                    when (detail.type) {
                                        SubscribesDetailType.BREAKFAST -> {
                                            binding.notificationDiningBreakfastSoldOut.fakeChecked = detail.isPermit
                                            binding.notificationDiningBreakfastSoldOut.isChecked = detail.isPermit
                                        }

                                        SubscribesDetailType.LUNCH -> {
                                            binding.notificationDiningLunchSoldOut.fakeChecked = detail.isPermit
                                            binding.notificationDiningLunchSoldOut.isChecked = detail.isPermit
                                        }

                                        SubscribesDetailType.DINNER -> {
                                            binding.notificationDiningDinnerSoldOut.fakeChecked = detail.isPermit
                                            binding.notificationDiningDinnerSoldOut.isChecked = detail.isPermit
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
}