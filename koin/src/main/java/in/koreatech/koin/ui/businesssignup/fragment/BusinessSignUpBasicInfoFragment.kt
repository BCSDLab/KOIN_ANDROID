package `in`.koreatech.koin.ui.businesssignup.fragment

import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.widget.doOnTextChanged
import androidx.fragment.app.activityViewModels
import dagger.hilt.android.AndroidEntryPoint
import `in`.koreatech.koin.R
import `in`.koreatech.koin.core.fragment.BaseFragment
import `in`.koreatech.koin.databinding.FragmentBusinessSignupBasicInfoBinding
import `in`.koreatech.koin.domain.error.signup.InCorrectEmailAddressException
import `in`.koreatech.koin.domain.error.signup.SignupAlreadySentEmailException
import `in`.koreatech.koin.domain.state.signup.SignupContinuationState
import `in`.koreatech.koin.ui.businesssignup.viewmodel.BusinessSignUpBaseViewModel
import `in`.koreatech.koin.ui.businesssignup.viewmodel.BusinessSignUpBasicInfoViewModel
import `in`.koreatech.koin.ui.businesssignup.viewmodel.BusinessVerificationViewModel
import `in`.koreatech.koin.util.FirebasePerformanceUtil
import `in`.koreatech.koin.util.SnackbarUtil
import `in`.koreatech.koin.util.ext.observeLiveData
import `in`.koreatech.koin.util.ext.textString
import `in`.koreatech.koin.util.ext.withLoading

@AndroidEntryPoint
class BusinessSignUpBasicInfoFragment: BaseFragment() {
    private var _binding: FragmentBusinessSignupBasicInfoBinding? = null
    private val binding get() = _binding!!
    private val viewModel by activityViewModels<BusinessSignUpBasicInfoViewModel>()
    private val businessSignupBaseViewModel by activityViewModels<BusinessSignUpBaseViewModel>()
    private val businessVerificationViewModel by activityViewModels<BusinessVerificationViewModel>()

    private val firebasePerformanceUtil by lazy {
        FirebasePerformanceUtil("business_signup_activity")
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentBusinessSignupBasicInfoBinding.inflate(inflater, container, false)
        val view = binding.root

//        businessSignupBaseViewModel.setFragmentTag("BASIC_INFO_FRAGMENT")

        firebasePerformanceUtil.start()
        initView()
        initViewModel()

        return view
    }

    private fun initView() = with(binding) {
        inputTextEffect()

        signupSendVerificationButton.setOnClickListener {
            viewModel.continueBusinessSignup(
                email =  signupEdittextId.text.toString(),
                password = signupEdittextPw.text.toString(),
                passwordConfirm = signupEdittextPwConfirm.text.toString(),
                isAgreedPrivacyTerms = true,
                isAgreedKoinTerms = true
            )
        }
    }

    private fun inputTextEffect() = with(binding) {
        signupEdittextId.doOnTextChanged { text, _, _, _ ->
            if(text.isNullOrBlank()) divideLine1.setBackgroundColor(this@BusinessSignUpBasicInfoFragment.requireContext().getColor(R.color.blue1))
            else divideLine1.setBackgroundColor(this@BusinessSignUpBasicInfoFragment.requireContext().getColor(R.color.black))
            isAllWrite()
        }

        signupEdittextPw.doOnTextChanged { text, _, _, _ ->
            if(text.isNullOrBlank()) divideLine2.setBackgroundColor(this@BusinessSignUpBasicInfoFragment.requireContext().getColor(R.color.blue1))
            else divideLine2.setBackgroundColor(this@BusinessSignUpBasicInfoFragment.requireContext().getColor(R.color.black))
            isAllWrite()
        }

        signupEdittextPwConfirm.doOnTextChanged { text, _, _, _ ->
            if(text.isNullOrBlank()) divideLine3.setBackgroundColor(this@BusinessSignUpBasicInfoFragment.requireContext().getColor(R.color.blue1))
            else divideLine3.setBackgroundColor(this@BusinessSignUpBasicInfoFragment.requireContext().getColor(R.color.black))
            isAllWrite()
        }
    }

    private fun isAllWrite() {
        var check = true
        if(binding.signupEdittextId.text.toString().isBlank()) check = false
        if(binding.signupEdittextPw.text.toString().isBlank()) check = false
        if(binding.signupEdittextPwConfirm.text.toString().isBlank()) check = false

        if(check) binding.signupSendVerificationButton.setBackgroundColor(this.requireContext().getColor(R.color.colorPrimary))
        else binding.signupSendVerificationButton.setBackgroundColor(this.requireContext().getColor(R.color.gray5))
    }

    private fun initViewModel() = with(viewModel) {
        withLoading(this@BusinessSignUpBasicInfoFragment, this)

        observeLiveData(businessSignupContinuationState) { state ->
            when(state) {
                SignupContinuationState.EmailIsNotValidate -> {
                    SnackbarUtil.makeShortSnackbar(
                        binding.root,
                        getString(R.string.signup_error_check_email)
                    )
                }
                SignupContinuationState.PasswordIsNotValidate -> {
                    SnackbarUtil.makeShortSnackbar(
                        binding.root,
                        getString(R.string.signup_error_check_password)
                    )
                }
                SignupContinuationState.PasswordNotMatching -> {
                    SnackbarUtil.makeShortSnackbar(
                        binding.root,
                        getString(R.string.signup_error_check_password_match)
                    )
                }
                SignupContinuationState.RequestedEmailValidation -> {
                    val email = binding.signupEdittextId.text.toString()
                    Log.d("myEmail", email)
                    val password = binding.signupEdittextPw.text.toString()
                    val passwordConfirm = binding.signupEdittextPwConfirm.text.toString()

                    businessVerificationViewModel.setSignUpInfo(email, password, passwordConfirm)

                    businessSignupBaseViewModel.setFragmentTag("VERIFICATION_FRAGMENT")
                }
                SignupContinuationState.NotAgreedKoinTerms -> {
                    SnackbarUtil.makeShortSnackbar(
                        binding.root,
                        getString(R.string.signup_error_check_koin_terms)
                    )
                }
                SignupContinuationState.NotAgreedPrivacyTerms -> {
                    SnackbarUtil.makeShortSnackbar(
                        binding.root,
                        getString(R.string.signup_error_check_privacy_terms)
                    )
                }
            }
        }
        observeLiveData(businessSignupContinuationError) { t ->
            SnackbarUtil.makeShortSnackbar(
                binding.root,
                when(t) {
                    is SignupAlreadySentEmailException -> getString(R.string.signup_error_email_already_send_or_email_requested)
                    is InCorrectEmailAddressException -> getString(R.string.signup_error_incorrect_email_address)
                    else -> getString(R.string.signup_error_when_email_validation)
                }
            )
        }
    }
}