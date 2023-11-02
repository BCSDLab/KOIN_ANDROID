package `in`.koreatech.koin.ui.businesssignup

import `in`.koreatech.koin.R
import `in`.koreatech.koin.constant.GOTO_KOREATECH_PORTAL_SNACK_BAR_TIME
import `in`.koreatech.koin.core.activity.ActivityBase
import `in`.koreatech.koin.core.util.dataBinding
import `in`.koreatech.koin.databinding.ActivityBusinessSignUpBinding
import `in`.koreatech.koin.domain.error.signup.InCorrectEmailAddressException
import `in`.koreatech.koin.domain.error.signup.SignupAlreadySentEmailException
import `in`.koreatech.koin.domain.state.signup.SignupContinuationState
import `in`.koreatech.koin.ui.businesssignup.viewmodel.BusinessSignupViewModel
import `in`.koreatech.koin.util.FirebasePerformanceUtil
import `in`.koreatech.koin.util.SnackbarUtil
import `in`.koreatech.koin.util.ext.observeLiveData
import `in`.koreatech.koin.util.ext.withLoading
import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.activity.viewModels
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class BusinessSignUpActivity : ActivityBase(R.layout.activity_business_sign_up) {
    private val binding by dataBinding<ActivityBusinessSignUpBinding>()
    private val businessSignupViewModel by viewModels<BusinessSignupViewModel>()

    private val firebasePerformanceUtil by lazy {
        FirebasePerformanceUtil("business_signup_activity")
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_business_sign_up)

        firebasePerformanceUtil.start()
        initView()
        initViewModel()
    }

    private fun initView() = with(binding) {
        signupBackButton.setOnClickListener { finish() }

        val isAgreedPrivacyTerms = intent.getBooleanExtra("isAgreedPrivacyTerms", false)
        val isAgreedKoinTerms = intent.getBooleanExtra("isAgreedKoinTerms", false)
        signupSendVerificationButton.setOnClickListener {
            businessSignupViewModel.continueBusinessSignup(
                email =  signupEdittextId.text.toString(),
                password = signupEdittextPw.text.toString(),
                passwordConfirm = signupEdittextPwConfirm.text.toString(),
                isAgreedPrivacyTerms = isAgreedPrivacyTerms,
                isAgreedKoinTerms = isAgreedKoinTerms
            )
        }

        signupBackButton.setOnClickListener {
            finish()
        }
    }

    private fun initViewModel() = with(businessSignupViewModel) {
        withLoading(this@BusinessSignUpActivity, this)
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
                    startActivity(Intent(this@BusinessSignUpActivity, BusinessVerificationActivity::class.java))
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
            Log.d("MyTag", t.toString())
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

    override fun onDestroy() {
        firebasePerformanceUtil.stop()
        super.onDestroy()
    }
}