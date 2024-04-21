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
                                        binding.notificationEvent.isChecked = it.isPermit
                                    }

                                    SubscribesType.DINING_SOLD_OUT -> {
                                        binding.notificationDiningSoldOut.isChecked = it.isPermit
                                    }

                                    SubscribesType.NOTHING -> Unit
                                }
                            }
                        }

                        is NotificationUiState.Error -> {}
                        is NotificationUiState.Nothing -> {}
                    }
                }
            }
        }
    }

    private fun onSubscribe() {
        binding.notificationDiningSoldOut.setOnSwitchClickListener { isChecked ->
            handleSubscription(isChecked, SubscribesType.DINING_SOLD_OUT)
        }
        binding.notificationEvent.setOnSwitchClickListener { isChecked ->
            handleSubscription(isChecked, SubscribesType.SHOP_EVENT)
        }
    }

    private fun handleSubscription(isChecked: Boolean, type: SubscribesType) {
        if (isChecked) viewModel.updateSubscription(type)
        else viewModel.deleteSubscription(type)
    }
}