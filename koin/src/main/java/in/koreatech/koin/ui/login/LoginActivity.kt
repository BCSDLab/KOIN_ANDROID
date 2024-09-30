package `in`.koreatech.koin.ui.login

import android.content.Intent
import android.content.Intent.FLAG_ACTIVITY_CLEAR_TOP
import android.net.Uri
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
import `in`.koreatech.koin.core.analytics.EventAction
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
import `in`.koreatech.koin.util.ext.hideKeyboard
import `in`.koreatech.koin.util.ext.textString
import `in`.koreatech.koin.util.ext.withLoading
import kotlinx.coroutines.launch

@AndroidEntryPoint
class LoginActivity : ActivityBase(R.layout.activity_login) {
    private val binding by dataBinding<ActivityLoginBinding>()
    override val screenTitle = "로그인"

    private val loginViewModel by viewModels<LoginViewModel>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

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
                        is UiStatus.Success -> goToNextRoute()
                        is UiStatus.Failed -> {
                            SnackbarUtil.makeShortSnackbar(binding.root, it.status.message)
                            loginViewModel.onFailedLogin()
                        }
                    }
                }
            }
        }
    }

    private fun goToNextRoute() {
        val uri = intent.data
        val link = uri?.getQueryParameter("link")

        if (link != null) {
            try {
                val intent = Intent(Intent.ACTION_VIEW).apply {
                    flags = FLAG_ACTIVITY_CLEAR_TOP
                    data = Uri.parse(link)
                }
                startActivity(intent)
                finish()
            } catch (e: Exception) {
                startActivity(Intent(this@LoginActivity, MainActivity::class.java))
                finish()
            }
        } else {
            startActivity(Intent(this@LoginActivity, MainActivity::class.java))
            finish()
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
                EventAction.USER,
                AnalyticsConstant.Label.LOGIN,
                getString(R.string.login_complete)
            )
        }

        loginButtonSignup.setOnClickListener {
            startActivity(Intent(this@LoginActivity, SignupActivity::class.java))
            EventLogger.logClickEvent(
                EventAction.USER,
                AnalyticsConstant.Label.LOGIN,
                getString(R.string.sign_up)
            )
        }

        forgotPasswordLinearLayout.setOnClickListener {
            EventLogger.logClickEvent(
                EventAction.USER,
                AnalyticsConstant.Label.LOGIN,
                getString(R.string.find_password)
            )
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