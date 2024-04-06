package `in`.koreatech.koin.ui.signup

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import `in`.koreatech.koin.R
import `in`.koreatech.koin.core.activity.ActivityBase
import `in`.koreatech.koin.databinding.ActivitySignupCompleteBinding
import `in`.koreatech.koin.ui.login.LoginActivity

class SignUpCompleteActivity : ActivityBase() {

    private lateinit var binding: ActivitySignupCompleteBinding
    override val screenTitle = "회원가입 완료"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySignupCompleteBinding.inflate(layoutInflater)
        setContentView(binding.root)

        initView()
    }

    override fun onBackPressed() {
        //super.onBackPressed()
    }

    private fun initView() = with(binding){
        goToLoginButton.setOnClickListener{
            val intent = Intent(this@SignUpCompleteActivity, LoginActivity::class.java)
            startActivity(intent)
        }

        goToShcoolHomePageButton.setOnClickListener {
            val intent = Intent(Intent.ACTION_VIEW, Uri.parse(getString(R.string.koreatech_url)))
            startActivity(intent)
        }
    }

}