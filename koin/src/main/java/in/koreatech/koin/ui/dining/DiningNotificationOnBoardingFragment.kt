package `in`.koreatech.koin.ui.dining

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import dagger.hilt.android.AndroidEntryPoint
import `in`.koreatech.koin.R
import `in`.koreatech.koin.databinding.DiningNotificationOnBoardingBottomSheetBinding
import `in`.koreatech.koin.domain.model.notification.SubscribesDetailType
import `in`.koreatech.koin.domain.model.notification.SubscribesType
import `in`.koreatech.koin.ui.notification.NotificationActivity
import `in`.koreatech.koin.ui.notification.viewmodel.NotificationUiState
import `in`.koreatech.koin.ui.notification.viewmodel.NotificationViewModel
import kotlinx.coroutines.launch

@AndroidEntryPoint
class DiningNotificationOnBoardingFragment : BottomSheetDialogFragment() {
    private var binding: DiningNotificationOnBoardingBottomSheetBinding? = null
    private val viewModel by viewModels<NotificationViewModel>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setStyle(STYLE_NORMAL, R.style.CustomBottomSheetDialog)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        viewModel.getPermissionInfo()
        return DiningNotificationOnBoardingBottomSheetBinding.inflate(inflater, container, false).also {
            binding = it
        }.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding?.apply {
            viewLifecycleOwner.lifecycleScope.launch {
                repeatOnLifecycle(Lifecycle.State.STARTED) {

                    viewModel.notificationUiState.collect { uiState ->
                        when (uiState) {
                            is NotificationUiState.Success -> {
                                uiState.notificationPermissionInfo.subscribes.forEach {
                                    when (it.type) {

                                        SubscribesType.DINING_SOLD_OUT -> with(notificationDiningSoldOut) {
                                            if (isChecked != it.isPermit) {
                                                fakeChecked = it.isPermit
                                                isChecked = it.isPermit
                                            }
                                        }

                                        SubscribesType.DINING_IMAGE_UPLOAD -> with(notificationSetImageUploadNotification) {
                                            if (isChecked != it.isPermit) {
                                                fakeChecked = it.isPermit
                                                isChecked = it.isPermit
                                            }
                                        }

                                        SubscribesType.NOTHING -> Unit
                                        else -> Unit
                                    }
                                }
                            }

                            is NotificationUiState.Failed -> {}
                            is NotificationUiState.Nothing -> {}
                        }
                    }
                }
            }
            btnNavigateToNotificationSetting.setOnClickListener {
                dismiss()
                val intent = Intent(requireContext(), NotificationActivity::class.java)
                startActivity(intent)
            }
            textButtonClose.setOnClickListener {
                dismiss()
            }

            notificationDiningSoldOut.setOnSwitchClickListener { isChecked ->
                if (isChecked) {
                    viewModel.updateSubscription(SubscribesType.DINING_SOLD_OUT)
                    viewModel.updateSubscriptionDetail(SubscribesDetailType.BREAKFAST)
                    viewModel.updateSubscriptionDetail(SubscribesDetailType.LUNCH)
                    viewModel.updateSubscriptionDetail(SubscribesDetailType.DINNER)
                }
                else viewModel.deleteSubscription(SubscribesType.DINING_SOLD_OUT)
            }

            notificationSetImageUploadNotification.setOnSwitchClickListener { isChecked ->
                if (isChecked) viewModel.updateSubscription(SubscribesType.DINING_IMAGE_UPLOAD)
                else viewModel.deleteSubscription(SubscribesType.DINING_IMAGE_UPLOAD)
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        binding = null
    }
}
