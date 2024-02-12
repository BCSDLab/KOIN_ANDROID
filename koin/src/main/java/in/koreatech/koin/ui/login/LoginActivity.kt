package `in`.koreatech.koin.ui.login

import `in`.koreatech.koin.R
import `in`.koreatech.koin.core.activity.ActivityBase
import `in`.koreatech.koin.core.util.dataBinding
import `in`.koreatech.koin.databinding.ActivityLoginBinding
import `in`.koreatech.koin.ui.business.registerstore.activity.RegisterStoreActivity
import `in`.koreatech.koin.ui.forgotpassword.ForgotPasswordActivity
import `in`.koreatech.koin.ui.login.viewmodel.LoginViewModel
import `in`.koreatech.koin.ui.main.activity.MainActivity
import `in`.koreatech.koin.ui.signup.SignupActivity
import `in`.koreatech.koin.util.SnackbarUtil
import `in`.koreatech.koin.util.ext.observeLiveData
import `in`.koreatech.koin.util.ext.textString
import `in`.koreatech.koin.util.ext.withLoading
import android.content.Intent
import android.os.Bundle
import android.view.inputmethod.InputMethodManager
import androidx.activity.viewModels
import dagger.hilt.android.AndroidEntryPoint
import `in`.koreatech.koin.ui.business.insertmemu.activity.InsertMenuActivity
import java.util.*

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
        
        observeLiveData(loginSuccessEvent) {
            startActivity(Intent(this@LoginActivity, MainActivity::class.java))
        }

        observeLiveData(loginErrorMessage) {
            SnackbarUtil.makeShortSnackbar(binding.root, it)
        }
    }

    private fun initView() = with(binding) {
        loginEdittextId.setOnEditorActionListener { v, actionId, event ->
            loginEdittextPw.requestFocus()
        }
        loginEdittextPw.setOnEditorActionListener { v, actionId, event ->
            currentFocus?.let { view ->
                val imm = getSystemService(INPUT_METHOD_SERVICE) as InputMethodManager
                Objects.requireNonNull(imm).hideSoftInputFromWindow(view.windowToken, 0)
            } ?: false
        }

        loginButton.setOnClickListener {
            currentFocus?.let { view ->
                val imm = getSystemService(INPUT_METHOD_SERVICE) as InputMethodManager
                Objects.requireNonNull(imm).hideSoftInputFromWindow(view.windowToken, 0)
            }

            if (
                loginEdittextId.textString.isBlank() ||
                loginEdittextPw.textString.isBlank()
            ) {
                SnackbarUtil.makeShortSnackbar(binding.root, getString(R.string.login_required_field_not_filled))
            } else {
                loginViewModel.login(
                    email =  getString(R.string.koreatech_email_postfix, loginEdittextId.text),
                    password = loginEdittextPw.text.toString()
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

        isMaster.setOnClickListener {
            startActivity(Intent(this@LoginActivity, RegisterStoreActivity::class.java))
        } /*임시로 사장님이신가요? 버튼을 누르면 넘어가도록 함*/

    }
}