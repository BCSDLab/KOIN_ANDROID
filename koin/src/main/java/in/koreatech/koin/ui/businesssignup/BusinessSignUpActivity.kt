package `in`.koreatech.koin.ui.businesssignup

import `in`.koreatech.koin.R
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
import `in`.koreatech.koin.util.ext.textString
import `in`.koreatech.koin.util.ext.withLoading
import android.content.Intent
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import androidx.activity.viewModels
import androidx.core.widget.addTextChangedListener
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch

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
        inputTextEffect()

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

    private fun inputTextEffect() = with(binding) {
        signupEdittextId.addTextChangedListener(object: TextWatcher {
            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) { }

            override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
                if(p0.isNullOrBlank()) divideLine1.setBackgroundColor(getColor(R.color.blue1))
                else divideLine1.setBackgroundColor(getColor(R.color.black))
                isAllWrite()
            }

            override fun afterTextChanged(p0: Editable?) { }
        })

        signupEdittextPw.addTextChangedListener(object: TextWatcher {
            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) { }

            override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
                if(p0.isNullOrBlank()) divideLine2.setBackgroundColor(getColor(R.color.blue1))
                else divideLine2.setBackgroundColor(getColor(R.color.black))
                isAllWrite()
            }

            override fun afterTextChanged(p0: Editable?) { }
        })

        signupEdittextPwConfirm.addTextChangedListener(object: TextWatcher {
            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) { }

            override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
                if(p0.isNullOrBlank()) divideLine3.setBackgroundColor(getColor(R.color.blue1))
                else divideLine3.setBackgroundColor(getColor(R.color.black))
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

        if(check) binding.signupSendVerificationButton.setBackgroundColor(getColor(R.color.colorPrimary))
        else binding.signupSendVerificationButton.setBackgroundColor(getColor(R.color.gray5))
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
                    val intent = Intent(applicationContext, BusinessVerificationActivity::class.java)
                    intent.putExtra("email", binding.signupEdittextId.text.toString())
                    intent.putExtra("password", binding.signupEdittextPw.text.toString())
                    intent.putExtra("password_confirm", binding.signupEdittextPwConfirm.text.toString())

                    startActivity(intent)
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