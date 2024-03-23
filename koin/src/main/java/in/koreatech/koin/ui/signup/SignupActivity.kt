package `in`.koreatech.koin.ui.signup

import android.content.Intent
import android.os.Bundle
import android.view.MotionEvent
import android.view.View
import androidx.activity.viewModels
import androidx.core.view.isGone
import androidx.core.widget.addTextChangedListener
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.flowWithLifecycle
import androidx.lifecycle.lifecycleScope
import dagger.hilt.android.AndroidEntryPoint
import `in`.koreatech.koin.R
import `in`.koreatech.koin.core.activity.DataBindingActivity
import `in`.koreatech.koin.databinding.ActivitySignupBinding
import `in`.koreatech.koin.domain.state.signup.SignupContinuationState
import `in`.koreatech.koin.ui.signup.dialog.SignupKoinTermsDialog
import `in`.koreatech.koin.ui.signup.dialog.SignupPrivacyTermsDialog
import `in`.koreatech.koin.ui.signup.viewmodel.SignupViewModel
import `in`.koreatech.koin.util.FirebasePerformanceUtil
import `in`.koreatech.koin.util.SnackbarUtil
import `in`.koreatech.koin.util.ext.hideKeyboard
import `in`.koreatech.koin.util.ext.textString
import `in`.koreatech.koin.util.ext.useUnderLine
import `in`.koreatech.koin.util.ext.withLoading
import kotlinx.coroutines.launch

@AndroidEntryPoint
class SignupActivity : DataBindingActivity<ActivitySignupBinding>() {
    override val layoutId: Int
        get() = R.layout.activity_signup

    private val firebasePerformanceUtil by lazy {
        FirebasePerformanceUtil(SIGN_UP_ACTIVITY)
    }

    private val signupViewModel by viewModels<SignupViewModel>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        firebasePerformanceUtil.start()
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        initView()
        observeData()
    }

    private fun initView() = with(binding) {
        signupBackButton.setOnClickListener { finish() }
        onTermsInteractions()
        validationEmail()
        moveToDetailView()
    }

    private fun onTermsInteractions() {
        binding.signupTextViewPrivacyTerms.useUnderLine()
        binding.signupTextViewKoinTerms.useUnderLine()

        binding.signupTextViewPrivacyTerms.setOnClickListener {
            SignupPrivacyTermsDialog().show(supportFragmentManager, SIGNUP_PRIVACY_TERMS_DIALOG)
        }
        binding.signupTextViewKoinTerms.setOnClickListener {
            SignupKoinTermsDialog().show(supportFragmentManager, SIGNUP_PERSONAL_INFO_TERMS_DIALOG)
        }
    }

    private fun validationEmail() {
        binding.signupEdittextId.addTextChangedListener {
            if (binding.checkEmailDuplicatedButton.isGone) binding.checkEmailDuplicatedButton.visibility = View.VISIBLE
        }

        binding.checkEmailDuplicatedButton.setOnClickListener {
            if (binding.signupEdittextId.textString.isNotEmpty()) {
                signupViewModel.checkEmailDuplicated(
                    getString(
                        R.string.koreatech_email_postfix,
                        binding.signupEdittextId.textString.trim()
                    )
                )
            } else SnackbarUtil.makeShortSnackbar(
                binding.root,
                getString(R.string.error_forgotpassword_no_input)
            )
        }
    }

    private fun moveToDetailView() {
        binding.signupNextButton.setOnClickListener {
            signupViewModel.continueSignup(
                portalAccount = getString(
                    R.string.koreatech_email_postfix,
                    binding.signupEdittextId.textString
                ),
                password = binding.signupEdittextPw.textString,
                passwordConfirm = binding.signupEdittextPwConfirm.textString,
                isAgreedPrivacyTerms = binding.signupCheckBoxPrivacyTerms.isChecked,
                isAgreedKoinTerms = binding.signupCheckBoxKoinTerms.isChecked
            )
        }
    }

    private fun observeData() = with(signupViewModel) {
        withLoading(this@SignupActivity, this)

        lifecycleScope.launch {
            signupContinuationState.flowWithLifecycle(lifecycle, Lifecycle.State.STARTED).collect {
                when (it) {
                    SignupContinuationState.EmailDuplicated -> SnackbarUtil.makeShortSnackbar(binding.root, getString(R.string.error_email_duplicated))

                    SignupContinuationState.AvailableEmail -> {
                        SnackbarUtil.makeShortSnackbar(binding.root, getString(R.string.signup_email_check))
                        binding.checkEmailDuplicatedButton.visibility = View.GONE
                        binding.signupNextButton.visibility = View.VISIBLE
                    }

                    SignupContinuationState.EmailIsNotValidate -> SnackbarUtil.makeShortSnackbar(binding.root, getString(R.string.signup_error_check_email))

                    SignupContinuationState.NotAgreedKoinTerms -> SnackbarUtil.makeShortSnackbar(binding.root, getString(R.string.signup_error_check_koin_terms))

                    SignupContinuationState.NotAgreedPrivacyTerms -> SnackbarUtil.makeShortSnackbar(binding.root, getString(R.string.signup_error_check_privacy_terms))


                    SignupContinuationState.PasswordIsNotValidate -> SnackbarUtil.makeShortSnackbar(binding.root, getString(R.string.signup_error_check_password))

                    SignupContinuationState.PasswordNotMatching -> SnackbarUtil.makeShortSnackbar(binding.root, getString(R.string.signup_error_check_password_match))

                    SignupContinuationState.CheckComplete -> {
                        startSignupWithDetailActivity()
                    }

                    is SignupContinuationState.Failed -> SnackbarUtil.makeShortSnackbar(binding.root, it.message)
                    else -> Unit
                }
            }
        }
    }

    private fun startSignupWithDetailActivity() {
        val intent = Intent(this@SignupActivity, SignupWithDetailInfoActivity::class.java).apply {
            putExtra(SIGN_UP_EMAIL, binding.signupEdittextId.textString)
            putExtra(SIGN_UP_PASSWORD, binding.signupEdittextPw.textString)
        }
        startActivity(intent)
    }

    override fun onResume() {
        binding.signupNextButton.visibility = View.GONE
        binding.checkEmailDuplicatedButton.visibility = View.VISIBLE
        super.onResume()
    }

    override fun onDestroy() {
        firebasePerformanceUtil.stop()
        super.onDestroy()
    }

    override fun dispatchTouchEvent(ev: MotionEvent?): Boolean {
        hideKeyboard(currentFocus ?: binding.root)
        return super.dispatchTouchEvent(ev)
    }

    companion object {
        const val SIGN_UP_EMAIL = "email"
        const val SIGN_UP_PASSWORD = "password"

        private const val SIGN_UP_ACTIVITY = "signup_activity"
        private const val SIGNUP_PRIVACY_TERMS_DIALOG = "SIGNUP_PRIVACY_TERMS_DIALOG"
        private const val SIGNUP_PERSONAL_INFO_TERMS_DIALOG = "SIGNUP_PERSONAL_INFO_TERMS_DIALOG"
    }
}