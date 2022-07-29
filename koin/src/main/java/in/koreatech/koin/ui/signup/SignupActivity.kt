package `in`.koreatech.koin.ui.signup

import `in`.koreatech.koin.R
import `in`.koreatech.koin.contract.SignupContract
import `in`.koreatech.koin.core.activity.DataBindingActivity
import `in`.koreatech.koin.databinding.ActivitySignupBinding
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
        observeLiveData(signupContinuationState) { result ->
            if (result != null) {
                result.onSuccess { signupContinuationState ->
                    when (signupContinuationState) {
                        SignupContinuationState.EmailIsNotValidate -> {
                            SnackbarUtil.makeShortSnackbar(binding.root, "이메일을 확인해 주세요")
                        }
                        SignupContinuationState.NotAgreedKoinTerms -> {
                            SnackbarUtil.makeShortSnackbar(binding.root, "코인 이용약관에 동의해 주세요")
                        }
                        SignupContinuationState.NotAgreedPrivacyTerms -> {
                            SnackbarUtil.makeShortSnackbar(binding.root, "개인정보 이용약관에 동의해 주세요")
                        }
                        SignupContinuationState.PasswordIsNotValidate -> {
                            SnackbarUtil.makeShortSnackbar(
                                binding.root,
                                "특수문자를 포함한 영어와 숫자 6~18 자리 비밀번호를 입력해주세요"
                            )
                        }
                        SignupContinuationState.PasswordNotMatching -> {
                            SnackbarUtil.makeShortSnackbar(binding.root, "비밀번호가 일치하지 않습니다")
                        }
                        SignupContinuationState.RequestedEmailValidation -> {
                            showRequestedEmailValidationDialog()
                        }
                        SignupContinuationState.AlreadySentEmailValidation -> {
                            SnackbarUtil.makeShortSnackbar(binding.root, getString(R.string.email_already_send_or_email_requested))
                        }
                    }
                }.onFailure {
                    SnackbarUtil.makeShortSnackbar(binding.root, "이메일 인증 중 오류가 발생했습니다")
                }
            }
        }
    }

    private fun showRequestedEmailValidationDialog() {
        SnackbarUtil.makeSnackbarActionWebView(
            this,
            R.id.signup_box,
            "10분안에 학교 메일로 인증을 완료해 주세요. 이동하실래요?",
            "KOREATECH E-mail 인증",
            getString(R.string.koreatech_url),
            5000
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