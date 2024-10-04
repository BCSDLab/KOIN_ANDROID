package `in`.koreatech.koin.ui.changepassword

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import dagger.hilt.android.AndroidEntryPoint
import `in`.koreatech.koin.R
import `in`.koreatech.koin.core.util.dataBinding
import `in`.koreatech.koin.databinding.FragmentChangePasswordChangePwdBinding
import `in`.koreatech.koin.util.DebounceTextWatcher
import kotlinx.coroutines.launch

@AndroidEntryPoint
class ChangePasswordChangePwdFragment : Fragment(R.layout.fragment_change_password_change_pwd) {

    private val binding by dataBinding<FragmentChangePasswordChangePwdBinding>()
    private val viewModel by activityViewModels<ChangePasswordViewModel>()

    private val checkPwdFormatWatcher by lazy {
        DebounceTextWatcher(lifecycleScope) {
            viewModel.checkPasswordFormat(it)
        }
    }

    private val confirmPwdWatcher by lazy {
        DebounceTextWatcher(scope = lifecycleScope, debounceTime = 0L) {
            viewModel.onConfirmPasswordChanged(it)
        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        initListeners()
        initObservers()
    }

    private fun initListeners() {
        with(binding) {
            tietChangePassword.addTextChangedListener(checkPwdFormatWatcher)
            tietConfirmPassword.addTextChangedListener(confirmPwdWatcher)
            btnComplete.setOnClickListener {
                viewModel.changeNextPage()
            }
        }
    }

    private fun initObservers() {
        lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel.confirmPwdFormat.collect { (isIncludeEnglish, isIncludeNumber, isIncludeSymbol, isValidLength) ->
                    with(binding) {
                        tvChangeTermsEnglish.isEnabled = isIncludeEnglish
                        tvChangeTermsNumber.isEnabled = isIncludeNumber
                        tvChangeTermsSymbol.isEnabled = isIncludeSymbol
                        tvChangeTermsLength.isEnabled = isValidLength
                    }
                }
            }
        }
        lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel.isConfirmPwdSame.collect { isSame ->
                    with(binding) {
                        groupWarning.visibility = if (!isSame && !tietConfirmPassword.text.isNullOrBlank()){
                            View.VISIBLE
                        } else {
                            View.GONE
                        }
                    }
                }
            }
        }
        lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel.isAbleToComplete.collect { isAbleToComplete ->
                    binding.btnComplete.isEnabled = isAbleToComplete
                }
            }
        }
    }

    override fun onDestroyView() {
        with(binding) {
            tietChangePassword.removeTextChangedListener(checkPwdFormatWatcher)
            tietConfirmPassword.removeTextChangedListener(confirmPwdWatcher)
        }
        super.onDestroyView()
    }


    companion object {
        fun getInstance(): ChangePasswordChangePwdFragment = ChangePasswordChangePwdFragment()
    }
}