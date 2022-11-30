package `in`.koreatech.koin.ui.signup

import `in`.koreatech.koin.R
import `in`.koreatech.koin.constant.GOTO_KOREATECH_PORTAL_SNACK_BAR_TIME
import `in`.koreatech.koin.contract.SignupContract
import `in`.koreatech.koin.core.activity.DataBindingActivity
import `in`.koreatech.koin.databinding.ActivitySignupBinding
import `in`.koreatech.koin.domain.error.signup.SignupAlreadySentEmailException
import `in`.koreatech.koin.domain.state.signup.SignupContinuationState
import `in`.koreatech.koin.ui.signup.dialog.SignupKoinTermsDialog
import `in`.koreatech.koin.ui.signup.dialog.SignupPrivacyTermsDialog
import `in`.koreatech.koin.ui.signup.viewmodel.SignupViewModel
import `in`.koreatech.koin.util.FirebasePerformanceUtil
import `in`.koreatech.koin.util.SnackbarUtil
import `in`.koreatech.koin.util.ext.observeLiveData
import `in`.koreatech.koin.util.ext.textString
import `in`.koreatech.koin.util.ext.withLoading
import android.graphics.Paint
import android.os.Bundle
import androidx.activity.viewModels
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class SignupActivity : DataBindingActivity<ActivitySignupBinding>() {
    override val layoutId: Int
        get() = R.layout.activity_signup

    private val firebasePerformanceUtil by lazy {
        FirebasePerformanceUtil("signup_activity")
    }

    private val signupViewModel by viewModels<SignupViewModel>()
    private val portalAccount by lazy {
        intent.extras?.getString(SignupContract.PORTAL_ACCOUNT)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        firebasePerformanceUtil.start()
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        initView()
        initViewModel()
    }

    private fun initViewModel() = with(signupViewModel) {
        withLoading(this@SignupActivity, this)
        observeLiveData(signupContinuationState) { state ->
            when (state) {
                SignupContinuationState.EmailIsNotValidate -> {
                    SnackbarUtil.makeShortSnackbar(
                        binding.root,
                        getString(R.string.signup_error_check_email)
                    )
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
                    showRequestedEmailValidationDialog()
                }
            }
        }
        observeLiveData(signupContinuationError) { t ->
            SnackbarUtil.makeShortSnackbar(
                binding.root,
                when (t) {
                    is SignupAlreadySentEmailException -> getString(R.string.signup_error_email_already_send_or_email_requested)
                    else -> getString(R.string.signup_error_when_email_validation)
                }
            )
        }
    }

    private fun showRequestedEmailValidationDialog() {
        SnackbarUtil.makeSnackbarActionWebView(
            this,
            R.id.signup_box,
            getString(R.string.signup_email_validation_completed_message),
            getString(R.string.signup_email_validation_completed_title),
            getString(R.string.koreatech_url),
            GOTO_KOREATECH_PORTAL_SNACK_BAR_TIME
        )
    }

    private fun initView() = with(binding) {
        signupBackButton.setOnClickListener { finish() }

        signupEdittextId.setText(portalAccount ?: "")

        signupTextViewPrivacyTerms.paintFlags =
            signupTextViewPrivacyTerms.paintFlags or Paint.UNDERLINE_TEXT_FLAG
        signupTextViewKoinTerms.paintFlags =
            signupTextViewKoinTerms.paintFlags or Paint.UNDERLINE_TEXT_FLAG

        signupTextViewPrivacyTerms.setOnClickListener {
            SignupPrivacyTermsDialog().show(supportFragmentManager, SIGNUP_PRIVACY_TERMS_DIALOG)
        }
        signupTextViewKoinTerms.setOnClickListener {
            SignupKoinTermsDialog().show(supportFragmentManager, SIGNUP_PERSONAL_INFO_TERMS_DIALOG)
        }

        signupSendVerificationButton.setOnClickListener {
            signupViewModel.continueSignup(
                portalAccount = signupEdittextId.textString,
                password = signupEdittextPw.textString,
                passwordConfirm = signupEdittextPwConfirm.textString,
                isAgreedPrivacyTerms = signupCheckBoxPrivacyTerms.isChecked,
                isAgreedKoinTerms = signupCheckBoxKoinTerms.isChecked
            )
        }
    }

    override fun onDestroy() {
        firebasePerformanceUtil.stop()
        super.onDestroy()
    }

    companion object {
        private const val SIGNUP_PRIVACY_TERMS_DIALOG = "SIGNUP_PRIVACY_TERMS_DIALOG"
        private const val SIGNUP_PERSONAL_INFO_TERMS_DIALOG = "SIGNUP_PERSONAL_INFO_TERMS_DIALOG"
    }
}