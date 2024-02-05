package `in`.koreatech.koin.ui.businesslogin

import `in`.koreatech.koin.R
import `in`.koreatech.koin.core.util.dataBinding
import `in`.koreatech.koin.databinding.ActivityBusinessLoginBinding
import `in`.koreatech.koin.ui.businesssignup.BusinessSignUpCheckActivity
import `in`.koreatech.koin.ui.login.LoginActivity
import `in`.koreatech.koin.util.SnackbarUtil
import `in`.koreatech.koin.util.ext.observeLiveData
import `in`.koreatech.koin.util.ext.textString
import `in`.koreatech.koin.util.ext.withLoading
import android.content.Intent
import android.os.Bundle
import androidx.activity.viewModels
import dagger.hilt.android.AndroidEntryPoint
import `in`.koreatech.koin.core.activity.ActivityBase
import `in`.koreatech.koin.ui.login.viewmodel.LoginViewModel

@AndroidEntryPoint
class BusinessLoginActivity : ActivityBase(R.layout.activity_business_login) {
    private val binding by dataBinding<ActivityBusinessLoginBinding>()

    private val loginViewModel by viewModels<LoginViewModel>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_business_login)

        initView()
        initViewModel()
    }

    private fun initView() = with(binding) {
        businessLoginButton.setOnClickListener {
            if(
                loginEdittextId.textString.isBlank() ||
                loginEdittextPw.textString.isBlank()
            ) {
                SnackbarUtil.makeShortSnackbar(binding.root, getString(R.string.login_required_field_not_filled))
            } else {
                loginViewModel.login(
                    loginEdittextId.text.toString(),
                    loginEdittextPw.text.toString()
                )
            }
        }

        businessSignupButton.setOnClickListener {
            startActivity(Intent(this@BusinessLoginActivity, BusinessSignUpCheckActivity::class.java))
        }

        isStudentButton.setOnClickListener {
            startActivity(Intent(this@BusinessLoginActivity, LoginActivity::class.java))
        }

        forgotPasswordLinearLayout.setOnClickListener {
            // startActivity(Intent(this@BusinessLoginActivity, 비밀번호_찾기 Activity))
        }
    }

    private fun initViewModel() = with(loginViewModel) {
        withLoading(this@BusinessLoginActivity, this)

        observeLiveData(loginSuccessEvent) {
            // startActivity(Intent(this@BusinessLoginActivity, 메인_사장님 Activity))
        }

        observeLiveData(loginErrorMessage) {
            SnackbarUtil.makeShortSnackbar(binding.root, it)
        }
    }
}