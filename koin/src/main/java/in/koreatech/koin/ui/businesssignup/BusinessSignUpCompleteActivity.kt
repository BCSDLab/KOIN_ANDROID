package `in`.koreatech.koin.ui.businesssignup

import android.content.Intent
import `in`.koreatech.koin.R
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import `in`.koreatech.koin.core.util.dataBinding
import `in`.koreatech.koin.databinding.ActivityBusinessSignUpCompleteBinding
import `in`.koreatech.koin.ui.businesslogin.BusinessLoginActivity

class BusinessSignUpCompleteActivity : AppCompatActivity() {
    private val binding by dataBinding<ActivityBusinessSignUpCompleteBinding>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_business_sign_up_complete)

        initView()
    }

    private fun initView() = with(binding) {
        signupBackButton.setOnClickListener {
            finish()
        }

        goLoginActivityButton.setOnClickListener {
            startActivity(Intent(this@BusinessSignUpCompleteActivity, BusinessLoginActivity::class.java))
        }
    }
}