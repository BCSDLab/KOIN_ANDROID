package `in`.koreatech.koin.ui.notification

import android.os.Bundle
import androidx.activity.viewModels
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
                                        binding.notificationShopEvent.isChecked = it.isPermit
                                    }

                                    SubscribesType.DINING_SOLD_OUT -> {
                                        binding.notificationDiningSoldOut.isChecked = it.isPermit
                                    }

                                    SubscribesType.NOTHING -> Unit
                                }
                                it.detailSubscribes.forEach {
                                    when (it.type) {
                                        SubscribesDetailType.BREAKFAST -> {
                                            binding.notificationDiningBreakfastSoldOut.isChecked =
                                                it.isPermit
                                        }

                                        SubscribesDetailType.LUNCH -> {
                                            binding.notificationDiningLunchSoldOut.isChecked =
                                                it.isPermit
                                        }

                                        SubscribesDetailType.DINNER -> {
                                            binding.notificationDiningDinnerSoldOut.isChecked =
                                                it.isPermit
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
}