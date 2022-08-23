package `in`.koreatech.koin.ui.forgotpassword

import `in`.koreatech.koin.R
import `in`.koreatech.koin.core.activity.DataBindingActivity
import `in`.koreatech.koin.databinding.ActivityForgotPasswordBinding
import `in`.koreatech.koin.ui.forgotpassword.viewmodel.ForgotPasswordViewModel
import `in`.koreatech.koin.util.SnackbarUtil
import `in`.koreatech.koin.util.ext.hideSoftKeyboard
import `in`.koreatech.koin.util.ext.observeLiveData
import `in`.koreatech.koin.util.ext.textString
import `in`.koreatech.koin.util.ext.withLoading
import android.os.Bundle
import androidx.activity.viewModels
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class ForgotPasswordActivity : DataBindingActivity<ActivityForgotPasswordBinding>() {
    override val layoutId: Int
        get() = R.layout.activity_forgot_password

    private val forgotPasswordViewModel by viewModels<ForgotPasswordViewModel>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        initView()
        initViewModel()
    }

    private fun initViewModel() = with(forgotPasswordViewModel) {
        withLoading(this@ForgotPasswordActivity, forgotPasswordViewModel)
        observeLiveData(passwordResetEmailRequested) {
            SnackbarUtil.makeSnackbarActionWebView(
                this@ForgotPasswordActivity,
                R.id.forgot_password_id_edittext,
                "학교 메일로 비밀번호 초기화를 완료해 주세요. 이동하실래요?",
                "KOREATECH E-mail 인증",
                getString(R.string.koreatech_url),
                5000
            )
        }

        observeLiveData(passwordResetEmailRequestError) { t ->
            SnackbarUtil.makeShortSnackbar(binding.root, t.message)
        }
    }

    private fun initView() = with(binding) {
        resetPasswordButton.setOnClickListener {
            hideSoftKeyboard()
            forgotPasswordViewModel.requestFindPasswordEmail(forgotPasswordIdEdittext.textString)
        }

        forgotPasswordIdEdittext.setOnEditorActionListener { v, actionId, event ->
            hideSoftKeyboard()
            false
        }

        forgotPasswordToLoginButton.setOnClickListener {
            finish()
        }
    }

}