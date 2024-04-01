package `in`.koreatech.koin.ui.forgotpassword

import `in`.koreatech.koin.R
import `in`.koreatech.koin.constant.GOTO_KOREATECH_PORTAL_SNACK_BAR_TIME
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
    override val screenTitle = "비밀번호 초기화"

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
                getString(R.string.forgotpassword_sent_email_message),
                getString(R.string.forgotpassword_sent_email_title),
                getString(R.string.koreatech_url),
                GOTO_KOREATECH_PORTAL_SNACK_BAR_TIME
            )
        }

        observeLiveData(passwordResetEmailRequestErrorMessage) { message ->
            SnackbarUtil.makeShortSnackbar(binding.root, message)
        }
    }

    private fun initView() = with(binding) {
        resetPasswordButton.setOnClickListener {
            hideSoftKeyboard()
            forgotPasswordViewModel.requestFindPasswordEmail(getString(R.string.koreatech_email_postfix, forgotPasswordIdEdittext.textString.trim()))
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