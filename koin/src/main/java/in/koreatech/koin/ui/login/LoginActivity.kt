package `in`.koreatech.koin.ui.login

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.MotionEvent
import androidx.activity.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.flowWithLifecycle
import androidx.lifecycle.lifecycleScope
import dagger.hilt.android.AndroidEntryPoint
import `in`.koreatech.koin.R
import `in`.koreatech.koin.common.UiStatus
import `in`.koreatech.koin.core.activity.ActivityBase
import `in`.koreatech.koin.core.util.dataBinding
import `in`.koreatech.koin.databinding.ActivityLoginBinding
import `in`.koreatech.koin.ui.businesslogin.BusinessLoginActivity
import `in`.koreatech.koin.ui.forgotpassword.ForgotPasswordActivity
import `in`.koreatech.koin.ui.login.viewmodel.LoginViewModel
import `in`.koreatech.koin.ui.main.activity.MainActivity
import `in`.koreatech.koin.ui.signup.SignupActivity
import `in`.koreatech.koin.util.SnackbarUtil
import `in`.koreatech.koin.util.ext.hideKeyboard
import `in`.koreatech.koin.util.ext.textString
import `in`.koreatech.koin.util.ext.withLoading
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch

@AndroidEntryPoint
class LoginActivity : ActivityBase(R.layout.activity_login) {
    private val binding by dataBinding<ActivityLoginBinding>()

    private val loginViewModel by viewModels<LoginViewModel>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        initView()
        initViewModel()
    }

    private fun initViewModel() = with(loginViewModel) {
        withLoading(this@LoginActivity, this)

        loginState.flowWithLifecycle(lifecycle).onEach {
            when (it.status) {
                is UiStatus.Init -> Unit
                is UiStatus.Loading -> Unit
                is UiStatus.Success -> startActivity(Intent(this@LoginActivity, MainActivity::class.java))
                is UiStatus.Failed -> SnackbarUtil.makeShortSnackbar(binding.root, it.status.message)
            }
        }.launchIn(lifecycleScope)
    }

    private fun initView() = with(binding) {
        loginEdittextId.setOnEditorActionListener { v, actionId, event ->
            loginEdittextPw.requestFocus()
        }
        loginEdittextPw.setOnEditorActionListener { v, actionId, event ->
            currentFocus?.also { view ->
                hideKeyboard(view)
            }
            false
        }

        loginButton.setOnClickListener {
            currentFocus?.let { view ->
                hideKeyboard(view)
            }

            if (
                loginEdittextId.textString.isBlank() ||
                loginEdittextPw.textString.isBlank()
            ) {
                SnackbarUtil.makeShortSnackbar(
                    binding.root,
                    getString(R.string.login_required_field_not_filled)
                )
            } else {
                loginViewModel.login(
                    email = getString(
                        R.string.koreatech_email_postfix,
                        loginEdittextId.text.trim()
                    ),
                    password = loginEdittextPw.text.toString().trim()
                )
            }
        }

        loginButtonSignup.setOnClickListener {
            startActivity(Intent(this@LoginActivity, SignupActivity::class.java))
        }

        forgotPasswordLinearLayout.setOnClickListener {
            startActivity(Intent(this@LoginActivity, ForgotPasswordActivity::class.java))
        }

        anonymousLoginLinearLayout.setOnClickListener {
            startActivity(Intent(this@LoginActivity, MainActivity::class.java))
            finish()
        }

        isBusinessButton.setOnClickListener {
            startActivity(Intent(this@LoginActivity, BusinessLoginActivity::class.java))
        }
    }

    override fun dispatchTouchEvent(ev: MotionEvent?): Boolean {
        hideKeyboard(currentFocus ?: binding.root)
        return super.dispatchTouchEvent(ev)
    }
}