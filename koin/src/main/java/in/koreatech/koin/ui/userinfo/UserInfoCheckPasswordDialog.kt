package `in`.koreatech.koin.ui.userinfo

import android.content.Intent
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.View
import androidx.fragment.app.DialogFragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import dagger.hilt.android.AndroidEntryPoint
import `in`.koreatech.koin.R
import `in`.koreatech.koin.common.UiStatus
import `in`.koreatech.koin.core.util.dataBinding
import `in`.koreatech.koin.databinding.DialogUserInfoCheckPasswordBinding
import `in`.koreatech.koin.ui.userinfo.viewmodel.UserInfoCheckPasswordViewModel
import `in`.koreatech.koin.util.ext.setWidthPercent
import kotlinx.coroutines.launch


@AndroidEntryPoint
class UserInfoCheckPasswordDialog : DialogFragment(R.layout.dialog_user_info_check_password) {
    private val binding by dataBinding<DialogUserInfoCheckPasswordBinding>()
    private val viewModel by viewModels<UserInfoCheckPasswordViewModel>()

    private val passwordWatcher by lazy {
        object : TextWatcher {
            var prevPwd: String = ""

            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
                prevPwd = s.toString()
            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                if (!binding.btnCheckPassword.isEnabled && prevPwd != s.toString()) {
                    this.prevPwd = s.toString()
                    hideErrors()
                    viewModel.onPwdTextChanged()
                }
            }

            override fun afterTextChanged(s: Editable?) {}

        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
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
            tietPassword.addTextChangedListener(passwordWatcher)
        }
    }

    private fun initObservers() {
        lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.STARTED) {
                launch {
                    viewModel.verifyStatus.collect {
                        when (it.status) {
                            is UiStatus.Init -> Unit
                            is UiStatus.Loading -> Unit
                            is UiStatus.Success -> {
                                hideErrors()
                                startActivity(Intent(requireContext(), UserInfoEditActivity::class.java))
                                dismiss()
                            }

                            is UiStatus.Failed -> {
                                if (!it.isEdited) {
                                    showErrors()
                                }
                            }
                        }
                    }
                }

            }
        }
    }

    private fun showErrors() = with(binding) {
        groupWarning.visibility = View.VISIBLE
        btnCheckPassword.isEnabled = false
        tilPassword.error = " "
    }

    private fun hideErrors() = with(binding) {
        groupWarning.visibility = View.GONE
        btnCheckPassword.isEnabled = true
        tilPassword.error = null
    }

    override fun onDestroyView() {
        binding.tietPassword.removeTextChangedListener(passwordWatcher)

        super.onDestroyView()
    }
}