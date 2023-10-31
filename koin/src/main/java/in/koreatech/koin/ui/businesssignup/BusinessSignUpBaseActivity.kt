package `in`.koreatech.koin.ui.businesssignup

import `in`.koreatech.koin.R
import `in`.koreatech.koin.core.activity.ActivityBase
import `in`.koreatech.koin.core.util.dataBinding
import `in`.koreatech.koin.databinding.ActivityBusinessSignUpBaseBinding
import android.content.Intent
import android.os.Bundle
import android.widget.ImageView
import android.widget.Toast

class BusinessSignUpBaseActivity : ActivityBase(R.layout.activity_business_sign_up_base) {
    private val binding by dataBinding<ActivityBusinessSignUpBaseBinding>()
    private var activateCheckButton = false
    private var isAllCheckButtonCheck = false
    private var button1 = false
    private var button2 = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_business_sign_up_base)

        initView()
    }

    private fun initView() = with(binding) {
        isAllCheckButton.setOnClickListener {
            isAllCheckButtonClickEvent()
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
            if(activateCheckButton) {
                val intent = Intent(applicationContext, BusinessSignUpActivity::class.java)
                intent.putExtra("isAgreedPrivacyTerms", true)
                intent.putExtra("isAgreedKoinTerms", true)
                startActivity(intent)
            }
            else {
                Toast.makeText(applicationContext, getString(R.string.is_not_all_check), Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun isButtonClickCheck() = with(binding) {
        activateCheckButton = if(button1 && button2) {
            checkButton.setBackgroundColor(getColor(R.color.colorPrimary))
            isAllCheckButton.setImageDrawable(getDrawable(R.drawable.check_selected))
            isAllCheckButtonCheck = true
            true
        } else {
            checkButton.setBackgroundColor(getColor(R.color.gray5))
            isAllCheckButton.setImageDrawable(getDrawable(R.drawable.check))
            isAllCheckButtonCheck = false
            false
        }
    }

    private fun isAllCheckButtonClickEvent() = with(binding) {
        when(isAllCheckButtonCheck) {
            true -> {
                isAllCheckButton.setImageDrawable(getDrawable(R.drawable.check))
                circleCheckButton1.setImageDrawable(getDrawable(R.drawable.check))
                circleCheckButton2.setImageDrawable(getDrawable(R.drawable.check))
                isAllCheckButtonCheck = false
                button1 = false
                button2 = false
            }
            false -> {
                isAllCheckButton.setImageDrawable(getDrawable(R.drawable.check_selected))
                circleCheckButton1.setImageDrawable(getDrawable(R.drawable.check_selected))
                circleCheckButton2.setImageDrawable(getDrawable(R.drawable.check_selected))
                isAllCheckButtonCheck = true
                button1 = true
                button2 = true
            }
        }
        isButtonClickCheck()
    }

    private fun circleCheckButtonClickEvent(button: ImageView) = with(binding) {
        when(button) {
            circleCheckButton1 -> {
                button1 = if(button1) {
                    circleCheckButton1.setImageDrawable(getDrawable(R.drawable.check))
                    false
                } else {
                    circleCheckButton1.setImageDrawable(getDrawable(R.drawable.check_selected))
                    true
                }
            }
            circleCheckButton2 -> {
                button2 = if(button2) {
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