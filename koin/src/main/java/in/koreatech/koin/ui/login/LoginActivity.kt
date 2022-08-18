package `in`.koreatech.koin.ui.login

import `in`.koreatech.koin.R
import `in`.koreatech.koin.core.activity.DataBindingActivity
import `in`.koreatech.koin.core.toast.ToastUtil
import `in`.koreatech.koin.databinding.ActivityLoginBinding
import `in`.koreatech.koin.ui.login.viewmodel.LoginViewModel
import `in`.koreatech.koin.ui.main.MainActivity
import `in`.koreatech.koin.util.SnackbarUtil
import `in`.koreatech.koin.util.ext.observeLiveData
import `in`.koreatech.koin.util.ext.textString
import android.content.Intent
import android.os.Build.VERSION_CODES.P
import android.os.Bundle
import android.view.inputmethod.InputMethodManager
import androidx.activity.viewModels
import com.google.android.material.snackbar.Snackbar
import dagger.hilt.android.AndroidEntryPoint
import java.util.*

@AndroidEntryPoint
class LoginActivity : DataBindingActivity<ActivityLoginBinding>() {
    override val layoutId: Int
        get() = R.layout.activity_login

    private val loginViewModel by viewModels<LoginViewModel>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        initView()
        initViewModel()
    }

    private fun initViewModel() = with(loginViewModel) {
        observeLiveData(loginSuccessEvent) {
            startActivity(Intent(this@LoginActivity, MainActivity::class.java))
        }

        observeLiveData(loginErrorMessage) {
            SnackbarUtil.makeShortSnackbar(binding.root, it)
        }

        observeLiveData(isLoading) {
            if(it) showProgressDialog("로딩 중...")
            else hideProgressDialog()
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
                    portalAccount = loginEdittextId.text.toString(),
                    password = loginEdittextPw.text.toString()
                )
            }
        }

        loginButtonSignup.setOnClickListener {

        }

        forgotPasswordLinearLayout.setOnClickListener {

        }

        anonymousLoginLinearLayout.setOnClickListener {

        }
    }
}