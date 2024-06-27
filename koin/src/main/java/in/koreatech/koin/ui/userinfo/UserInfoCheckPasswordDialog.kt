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
import `in`.koreatech.koin.core.toast.ToastUtil
import `in`.koreatech.koin.core.util.dataBinding
import `in`.koreatech.koin.databinding.DialogUserInfoCheckPasswordBinding
import `in`.koreatech.koin.ui.userinfo.viewmodel.UserInfoCheckPasswordViewModel
import `in`.koreatech.koin.util.ext.observeLiveData
import `in`.koreatech.koin.util.ext.setWidthPercent
import kotlinx.coroutines.launch


@AndroidEntryPoint
class UserInfoCheckPasswordDialog : DialogFragment(R.layout.dialog_user_info_check_password) {
    private val binding by dataBinding<DialogUserInfoCheckPasswordBinding>()
    private val viewModel by viewModels<UserInfoCheckPasswordViewModel>()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        setWidthPercent(90)

        initView()
        initObservers()
    }

    private fun initView() {
        with(binding) {
            btnCheckPassword.setOnClickListener {
                viewModel.verifyPassword(tietPassword.text.toString())
            }
            ivDialogClose.setOnClickListener {
                dismiss()
            }
        }
    }

    private fun initObservers() {
        observeLiveData(viewModel.errorToast) {
            ToastUtil.getInstance().makeShort(it)
        }

        lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel.passwordValidEvent.collect {
                    dismiss()
                    startActivity(Intent(requireContext(), UserInfoEditActivity::class.java))
                }
            }
        }
    }
}