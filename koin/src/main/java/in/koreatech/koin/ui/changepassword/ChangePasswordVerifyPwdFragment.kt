package `in`.koreatech.koin.ui.changepassword

import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.view.View
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import dagger.hilt.android.AndroidEntryPoint
import `in`.koreatech.koin.R
import `in`.koreatech.koin.common.UiStatus
import `in`.koreatech.koin.core.util.dataBinding
import `in`.koreatech.koin.databinding.FragmentChangePasswordVerifyPwdBinding
import `in`.koreatech.koin.util.DebounceTextWatcher
import kotlinx.coroutines.launch

@AndroidEntryPoint
class ChangePasswordVerifyPwdFragment : Fragment(R.layout.fragment_change_password_verify_pwd) {

    private val binding by dataBinding<FragmentChangePasswordVerifyPwdBinding>()
    private val viewModel by activityViewModels<ChangePasswordViewModel>()

    private val passwordWatcher by lazy {
        DebounceTextWatcher(lifecycleScope) { password ->
            viewModel.verifyPassword(password)
        }
    }
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        initListeners()
        initObservers()
    }

    private fun initListeners() {
        with(binding) {
            btnNext.setOnClickListener { viewModel.changeNextPage() }
            tietVerifyPassword.addTextChangedListener(passwordWatcher)
        }
    }

    private fun initObservers() {
        lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel.verifyUiStatus.collect { status ->
                    when(status) {
                        is UiStatus.Init -> {}
                        is UiStatus.Success -> { hideError() }
                        is UiStatus.Failed -> { showError() }
                        is UiStatus.Loading -> Unit
                    }
                }
            }
        }

        lifecycleScope.launch {
            viewModel.userEmail.collect { email ->
                binding.etVerifyId.setText(email)
            }
        }
    }

    private fun showError() {
        binding.groupWarning.visibility = View.VISIBLE
        binding.btnNext.isEnabled = false
    }

    private fun hideError() {
        binding.groupWarning.visibility = View.GONE
        binding.btnNext.isEnabled = true
    }

    override fun onDestroyView() {
        binding.tietVerifyPassword.removeTextChangedListener(passwordWatcher)
        super.onDestroyView()
    }

    companion object {
        fun getInstance(): ChangePasswordVerifyPwdFragment = ChangePasswordVerifyPwdFragment()
    }
}