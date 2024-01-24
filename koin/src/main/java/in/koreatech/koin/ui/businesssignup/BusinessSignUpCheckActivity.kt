package `in`.koreatech.koin.ui.businesssignup

import `in`.koreatech.koin.R
import `in`.koreatech.koin.core.util.dataBinding
import android.content.Intent
import android.os.Bundle
import android.widget.ImageView
import android.widget.Toast
import dagger.hilt.android.AndroidEntryPoint
import `in`.koreatech.koin.core.activity.ActivityBase
import `in`.koreatech.koin.databinding.ActivityBusinessSignupCheckBinding

@AndroidEntryPoint
class BusinessSignUpCheckActivity : ActivityBase(R.layout.activity_business_signup_check) {
    private val binding by dataBinding<ActivityBusinessSignupCheckBinding>()

    private var isActivateCheckButton = false
    private var isCheckAllCheckButton = false
    private var isAgreedPrivacyTermsButton = false
    private var isAgreedKoinTerms = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_business_signup_check)

        initView()
    }

    private fun initView() = with(binding) {
        isAllCheckButton.setOnClickListener {
            isAllCheckButtonClickEvent()
        }

        signupBackButton.setOnClickListener {
            finish()
        }

        circleCheckButton1.setOnClickListener {
            circleCheckButtonClickEvent(circleCheckButton1)
        }

        circleCheckButton2.setOnClickListener {
            circleCheckButtonClickEvent(circleCheckButton2)
        }

        cancelButton.setOnClickListener {
            finish()
        }

        checkButton.setOnClickListener {
            if(isActivateCheckButton) {
                val intent = Intent(applicationContext, BusinessSignupBaseActivity::class.java)
                startActivity(intent)
            }
            else {
                Toast.makeText(applicationContext, getString(R.string.is_not_all_check), Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun isButtonClickCheck() = with(binding) {
        isActivateCheckButton = if(isAgreedPrivacyTermsButton && isAgreedKoinTerms) {
            checkButton.setBackgroundColor(getColor(R.color.colorPrimary))
            isAllCheckButton.setImageDrawable(getDrawable(R.drawable.check_selected))
            isCheckAllCheckButton = true
            true
        } else {
            checkButton.setBackgroundColor(getColor(R.color.gray5))
            isAllCheckButton.setImageDrawable(getDrawable(R.drawable.check))
            isCheckAllCheckButton = false
            false
        }
    }

    private fun isAllCheckButtonClickEvent() = with(binding) {
        when(isCheckAllCheckButton) {
            true -> {
                isAllCheckButton.setImageDrawable(getDrawable(R.drawable.check))
                circleCheckButton1.setImageDrawable(getDrawable(R.drawable.check))
                circleCheckButton2.setImageDrawable(getDrawable(R.drawable.check))
                isCheckAllCheckButton = false
                isAgreedPrivacyTermsButton = false
                isAgreedKoinTerms = false
            }
            false -> {
                isAllCheckButton.setImageDrawable(getDrawable(R.drawable.check_selected))
                circleCheckButton1.setImageDrawable(getDrawable(R.drawable.check_selected))
                circleCheckButton2.setImageDrawable(getDrawable(R.drawable.check_selected))
                isCheckAllCheckButton = true
                isAgreedPrivacyTermsButton = true
                isAgreedKoinTerms = true
            }
        }
        isButtonClickCheck()
    }

    private fun circleCheckButtonClickEvent(button: ImageView) = with(binding) {
        when(button) {
            circleCheckButton1 -> {
                isAgreedPrivacyTermsButton = if(isAgreedPrivacyTermsButton) {
                    circleCheckButton1.setImageDrawable(getDrawable(R.drawable.check))
                    false
                } else {
                    circleCheckButton1.setImageDrawable(getDrawable(R.drawable.check_selected))
                    true
                }
            }
            circleCheckButton2 -> {
                isAgreedKoinTerms = if(isAgreedKoinTerms) {
                    circleCheckButton2.setImageDrawable(getDrawable(R.drawable.check))
                    false
                } else {
                    circleCheckButton2.setImageDrawable(getDrawable(R.drawable.check_selected))
                    true
                }
            }
        }
        isButtonClickCheck()
    }
}