package `in`.koreatech.koin.ui.businesslogin

import `in`.koreatech.koin.R
import `in`.koreatech.koin.core.util.dataBinding
import `in`.koreatech.koin.databinding.ActivityBusinessLoginBinding
import `in`.koreatech.koin.ui.businesslogin.viewmodel.BusinessLoginViewModel
import `in`.koreatech.koin.ui.businesssignup.BusinessSignUpCheck
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

@AndroidEntryPoint
class BusinessLoginActivity : ActivityBase(R.layout.activity_business_login) {
    private val binding by dataBinding<ActivityBusinessLoginBinding>()

    private val businessLoginViewModel by viewModels<BusinessLoginViewModel>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_business_login)

        initView()
    }

    private fun initView() = with(binding) {
        businessLoginButton.setOnClickListener {
            if(
                loginEdittextId.textString.isBlank() ||
                loginEdittextPw.textString.isBlank()
            ) {
                SnackbarUtil.makeShortSnackbar(binding.root, getString(R.string.login_required_field_not_filled))
            } else {
                businessLoginViewModel.login(
                    email = loginEdittextId.text.toString(),
                    password = loginEdittextPw.text.toString()
                )
            }
        }

        businessSignupButton.setOnClickListener {
            startActivity(Intent(this@BusinessLoginActivity, BusinessSignUpCheck::class.java))
        }

        isStudentButton.setOnClickListener {
            startActivity(Intent(this@BusinessLoginActivity, LoginActivity::class.java))
        }
    }

    private fun initViewModel() = with(businessLoginViewModel) {
        withLoading(this@BusinessLoginActivity, this)

        observeLiveData(loginSuccessEvent) {
            // startActivity(Intent(this@BusinessLoginActivity, 메인_사장님 Activity))
        }

        observeLiveData(loginErrorMessage) {
            SnackbarUtil.makeShortSnackbar(binding.root, it)
        }
    }
}