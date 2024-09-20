package `in`.koreatech.koin.ui.userinfo

import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.fragment.app.DialogFragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import dagger.hilt.android.AndroidEntryPoint
import `in`.koreatech.koin.R
import `in`.koreatech.koin.common.UiStatus
import `in`.koreatech.koin.core.toast.ToastUtil
import `in`.koreatech.koin.core.util.dataBinding
import `in`.koreatech.koin.databinding.DialogUserLeaveBinding
import `in`.koreatech.koin.ui.login.LoginActivity
import `in`.koreatech.koin.util.ext.setWidthPercent
import kotlinx.coroutines.launch


@AndroidEntryPoint
class UserLeaveDialog : DialogFragment(R.layout.dialog_user_leave) {
    private val binding by dataBinding<DialogUserLeaveBinding>()
    private val viewModel by viewModels<UserLeaveViewModel>()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {

        initView()
        initObservers()

        super.onViewCreated(view, savedInstanceState)
    }

    private fun initView() {
        setWidthPercent(90)
        binding.btnCancel.setOnClickListener {
            dismiss()
        }
        binding.btnLeave.setOnClickListener {
            viewModel.leaveUser()
        }
    }

    private fun initObservers() {
        lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel.uiStatus.collect {
                    when (it) {
                        is UiStatus.Init -> {}
                        is UiStatus.Loading -> {}
                        is UiStatus.Success -> {
                            dismiss()
                            requireActivity().finishAffinity()
                            startActivity(Intent(requireContext(), LoginActivity::class.java))
                        }

                        is UiStatus.Failed -> {
                            ToastUtil.getInstance().makeShort(it.message)
                        }
                    }
                }
            }
        }
    }
}