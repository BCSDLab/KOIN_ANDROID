package `in`.koreatech.koin.ui.signup

import `in`.koreatech.koin.R
import `in`.koreatech.koin.contract.SignupContract
import `in`.koreatech.koin.core.activity.DataBindingActivity
import `in`.koreatech.koin.databinding.ActivitySignupBinding
import `in`.koreatech.koin.domain.state.signup.SignupContinuationState
import `in`.koreatech.koin.ui.signup.dialog.SignupKoinTermsDialog
import `in`.koreatech.koin.ui.signup.dialog.SignupPrivacyTermsDialog
import `in`.koreatech.koin.ui.signup.viewmodel.SignupKoinTermViewModel
import `in`.koreatech.koin.ui.signup.viewmodel.SignupViewModel
import `in`.koreatech.koin.util.FirebasePerformanceUtil
import `in`.koreatech.koin.util.SnackbarUtil
import `in`.koreatech.koin.util.ext.observeLiveData
import `in`.koreatech.koin.util.ext.withLoading
import android.graphics.Paint
import android.os.Bundle
import androidx.activity.viewModels
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class SignupActivityNew : DataBindingActivity<ActivitySignupBinding>() {
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
        withLoading(this@SignupActivityNew, this)
        observeLiveData(signupContinuationState) { result ->
            if (result != null) {
                try {
                    when (result.getOrThrow()) {
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

                        }
                    }
                } catch (t: Throwable) {
                    SnackbarUtil.makeShortSnackbar(binding.root, "이메일 인증 중 오류가 발생했습니다")
                }
            }
        }
    }

    private fun initView() = with(binding) {
        signupEdittextId.setText(portalAccount ?: "")

        signupTextViewPrivacyTerms.paintFlags =
            signupTextViewPrivacyTerms.paintFlags or Paint.UNDERLINE_TEXT_FLAG
        signupTextViewKoinTerms.paintFlags =
            signupTextViewKoinTerms.paintFlags or Paint.UNDERLINE_TEXT_FLAG

        signupTextViewPrivacyTerms.setOnClickListener {
            SignupPrivacyTermsDialog().show(supportFragmentManager, SIGNUP_PRIVACY_TERMS_DIALOG)
        }
        signupTextViewKoinTerms.setOnClickListener {
            SignupKoinTermsDialog().show(supportFragmentManager, SIGNUP_PRIVACY_TERMS_DIALOG)
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