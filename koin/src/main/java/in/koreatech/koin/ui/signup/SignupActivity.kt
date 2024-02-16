package `in`.koreatech.koin.ui.signup

import android.content.Intent
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
import android.util.Log
import android.view.View
import androidx.activity.viewModels
import dagger.hilt.android.AndroidEntryPoint
import `in`.koreatech.koin.core.toast.ToastUtil

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
                SignupContinuationState.CheckComplete -> {
                    val intent = Intent(this@SignupActivity, SignUpWithDetailInfoActivity::class.java)
                    intent.putExtra("email", binding.signupEdittextId.textString)
                    intent.putExtra("password", binding.signupEdittextPw.textString)
                    startActivity(intent)
                }
            }
        }

        observeLiveData(emailDuplicatedEvent) {
            if (it) {
                SnackbarUtil.makeShortSnackbar(binding.root, getString(R.string.error_email_duplicated))
            }
            else {
                SnackbarUtil.makeShortSnackbar(binding.root, getString(R.string.signup_email_check))
                binding.signupNextButton.visibility = View.VISIBLE
                binding.checkEmailDuplicatedButton.visibility = View.GONE
            }
        }
    }

    private fun initView() = with(binding) {
        signupBackButton.setOnClickListener { finish() }

        checkEmailDuplicatedButton.visibility = View.GONE
        //signupNextButton.visibility = View.GONE

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

        checkEmailDuplicatedButton.setOnClickListener {
            signupViewModel.checkEmailDuplicated(binding.signupEdittextId.textString)
        }

        signupNextButton.setOnClickListener {
            signupViewModel.continueSignup(
                portalAccount = binding.signupEdittextId.textString,
                password = binding.signupEdittextPw.textString,
                passwordConfirm = binding.signupEdittextPwConfirm.textString,
                isAgreedPrivacyTerms = binding.signupCheckBoxPrivacyTerms.isChecked,
                isAgreedKoinTerms = binding.signupCheckBoxKoinTerms.isChecked
            )
        }
    }

    override fun onRestart() {
        binding.signupNextButton.visibility = View.GONE
        binding.checkEmailDuplicatedButton.visibility = View.VISIBLE
        super.onRestart()
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