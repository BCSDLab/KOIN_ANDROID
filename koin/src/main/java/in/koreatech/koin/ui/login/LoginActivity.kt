package `in`.koreatech.koin.ui.login

import android.content.Intent
import android.os.Bundle
import android.view.MotionEvent
import androidx.activity.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import dagger.hilt.android.AndroidEntryPoint
import `in`.koreatech.koin.R
import `in`.koreatech.koin.common.UiStatus
import `in`.koreatech.koin.core.activity.ActivityBase
import `in`.koreatech.koin.core.analytics.EventLogger
import `in`.koreatech.koin.core.constant.AnalyticsConstant
import `in`.koreatech.koin.core.util.dataBinding
import `in`.koreatech.koin.databinding.ActivityLoginBinding
import `in`.koreatech.koin.ui.businesslogin.BusinessLoginActivity
import `in`.koreatech.koin.ui.forgotpassword.ForgotPasswordActivity
import `in`.koreatech.koin.ui.login.viewmodel.LoginViewModel
import `in`.koreatech.koin.ui.main.activity.MainActivity
import `in`.koreatech.koin.ui.signup.SignupActivity
import `in`.koreatech.koin.util.SnackbarUtil
import `in`.koreatech.koin.util.ext.getSerializableExtraCompat
import `in`.koreatech.koin.util.ext.hideKeyboard
import `in`.koreatech.koin.util.ext.textString
import `in`.koreatech.koin.util.ext.withLoading
import kotlinx.coroutines.launch

@AndroidEntryPoint
class LoginActivity : ActivityBase(R.layout.activity_login) {
    private val binding by dataBinding<ActivityLoginBinding>()
    override val screenTitle = "로그인"

    private val loginViewModel by viewModels<LoginViewModel>()
    private lateinit var nextNavigateActivityClass: Class<*>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        nextNavigateActivityClass = intent.getSerializableExtraCompat<Class<*>>("activity") ?: MainActivity::class.java
        initView()
        initViewModel()
    }

    private fun initViewModel() = with(loginViewModel) {
        withLoading(this@LoginActivity, this)

        lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.STARTED) {
                loginState.collect {
                    when (it.status) {
                        is UiStatus.Init -> Unit
                        is UiStatus.Loading -> Unit
                        is UiStatus.Success -> startActivity(Intent(this@LoginActivity, nextNavigateActivityClass))
                        is UiStatus.Failed -> {
                            SnackbarUtil.makeShortSnackbar(binding.root, it.status.message)
                            loginViewModel.onFailedLogin()
                        }
                    }
                }
            }
        }
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
            EventLogger.logClickEvent(
                AnalyticsConstant.Domain.USER,
                AnalyticsConstant.Label.LOGIN,
                getString(R.string.login)
            )
        }

        loginButtonSignup.setOnClickListener {
            startActivity(Intent(this@LoginActivity, SignupActivity::class.java))
            EventLogger.logClickEvent(
                AnalyticsConstant.Domain.USER,
                AnalyticsConstant.Label.START_SIGN_UP,
                getString(R.string.sign_up)
            )
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