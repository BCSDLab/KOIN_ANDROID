package `in`.koreatech.koin.ui.businesssignup.fragment

import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
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
import `in`.koreatech.koin.util.FirebasePerformanceUtil
import `in`.koreatech.koin.util.SnackbarUtil
import `in`.koreatech.koin.util.ext.observeLiveData
import `in`.koreatech.koin.util.ext.withLoading

@AndroidEntryPoint
class BusinessSignUpBasicInfoFragment: BaseFragment() {
    private var _binding: FragmentBusinessSignupBasicInfoBinding? = null
    private val binding get() = _binding!!
    private val viewModel by activityViewModels<BusinessSignUpBasicInfoViewModel>()
    private val businessSignupBaseViewModel by activityViewModels<BusinessSignUpBaseViewModel>()

    private val firebasePerformanceUtil by lazy {
        FirebasePerformanceUtil("business_signup_activity")
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentBusinessSignupBasicInfoBinding.inflate(inflater, container, false)
        val view = binding.root

        businessSignupBaseViewModel.setFragmentTag("basicInfoFragment")

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
        signupEdittextId.addTextChangedListener(object: TextWatcher {
            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) { }

            override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
                if(p0.isNullOrBlank()) divideLine1.setBackgroundColor(context!!.getColor(R.color.blue1))
                else divideLine1.setBackgroundColor(context!!.getColor(R.color.black))
                isAllWrite()
            }

            override fun afterTextChanged(p0: Editable?) { }
        })

        signupEdittextPw.addTextChangedListener(object: TextWatcher {
            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) { }

            override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
                if(p0.isNullOrBlank()) divideLine2.setBackgroundColor(context!!.getColor(R.color.blue1))
                else divideLine2.setBackgroundColor(context!!.getColor(R.color.black))
                isAllWrite()
            }

            override fun afterTextChanged(p0: Editable?) { }
        })

        signupEdittextPwConfirm.addTextChangedListener(object: TextWatcher {
            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) { }

            override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
                if(p0.isNullOrBlank()) divideLine3.setBackgroundColor(context!!.getColor(R.color.blue1))
                else divideLine3.setBackgroundColor(context!!.getColor(R.color.black))
                isAllWrite()
            }

            override fun afterTextChanged(p0: Editable?) { }
        })
    }

    private fun isAllWrite() {
        var check = true
        if(binding.signupEdittextId.text.toString().isBlank()) check = false
        if(binding.signupEdittextPw.text.toString().isBlank()) check = false
        if(binding.signupEdittextPwConfirm.text.toString().isBlank()) check = false

        if(check) binding.signupSendVerificationButton.setBackgroundColor(context!!.getColor(R.color.colorPrimary))
        else binding.signupSendVerificationButton.setBackgroundColor(context!!.getColor(R.color.gray5))
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
                    val bundle = Bundle()
                    bundle.putString("email", binding.signupEdittextId.text.toString())
                    bundle.putString("password", binding.signupEdittextPw.text.toString())
                    bundle.putString("password_confirm", binding.signupEdittextPwConfirm.text.toString())

                    val nextFragment = BusinessVerificationFragment()
                    nextFragment.arguments = bundle
                    parentFragmentManager.beginTransaction().replace(R.id.fragment_container_view, nextFragment).commit()
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