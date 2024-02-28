package `in`.koreatech.koin.ui.businesssignup

import `in`.koreatech.koin.R
import `in`.koreatech.koin.core.util.dataBinding
import android.content.Intent
import android.os.Bundle
import android.widget.ImageView
import android.widget.Toast
import androidx.activity.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import dagger.hilt.android.AndroidEntryPoint
import `in`.koreatech.koin.core.activity.ActivityBase
import `in`.koreatech.koin.databinding.ActivityBusinessSignupCheckBinding
import `in`.koreatech.koin.ui.businesssignup.viewmodel.BusinessSignUpCheckViewModel
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

@AndroidEntryPoint
class BusinessSignUpCheckActivity : ActivityBase(R.layout.activity_business_signup_check) {
    private val binding by dataBinding<ActivityBusinessSignupCheckBinding>()
    private val viewModel by viewModels<BusinessSignUpCheckViewModel>()

    private var isActivateCheckButton = false
    private var isAllCheckButton = false
    private var isAgreedPrivacyTermsButton = false
    private var isAgreedKoinTerms = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        initView()
        initViewModel()
    }

    private fun initView() = with(binding) {
        allCheckButton.setOnClickListener {
            isAllCheckButtonClickEvent()
        }

        signupBackButton.setOnClickListener {
            finish()
        }

        agreedPrivacyTermsButton.setOnClickListener {
            checkButtonClickEvent(agreedPrivacyTermsButton)
        }

        agreedKoinTermsButton.setOnClickListener {
            checkButtonClickEvent(agreedKoinTermsButton)
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

    private fun initViewModel() = with(viewModel) {
        lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.STARTED) {
                allCheckButtonState.collectLatest {
                    binding.allCheckButton.setImageDrawable(getDrawable(it))
                    isAllCheckButton = it != R.drawable.check

                    if(it == R.drawable.check_selected) {
                        binding.checkButton.setBackgroundColor(getColor(R.color.colorPrimary))
                    }
                }
            }
        }

        lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.STARTED) {
                agreedPrivacyTermsButtonState.collectLatest {
                    binding.agreedPrivacyTermsButton.setImageDrawable(getDrawable(it))
                    isAgreedPrivacyTermsButton = it != R.drawable.check
                }
            }
        }

        lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.STARTED) {
                agreedKoinTermsButtonState.collectLatest {
                    binding.agreedKoinTermsButton.setImageDrawable(getDrawable(it))
                    isAgreedKoinTerms = it != R.drawable.check
                }
            }
        }
    }

    private fun isButtonClickCheck() = with(binding) {
        isActivateCheckButton = if(isAgreedPrivacyTermsButton && isAgreedKoinTerms) {
            checkButton.setBackgroundColor(getColor(R.color.colorPrimary))
            allCheckButton.setImageDrawable(getDrawable(R.drawable.check_selected))
            viewModel.updateButtonState(R.drawable.check_selected, 1)
            isAllCheckButton = true
            true
        } else {
            checkButton.setBackgroundColor(getColor(R.color.gray5))
            allCheckButton.setImageDrawable(getDrawable(R.drawable.check))
            viewModel.updateButtonState(R.drawable.check, 1)
            isAllCheckButton = false
            false
        }
    }

    private fun isAllCheckButtonClickEvent() = with(binding) {
        when(isAllCheckButton) {
            true -> {
                allCheckButton.setImageDrawable(getDrawable(R.drawable.check))
                agreedPrivacyTermsButton.setImageDrawable(getDrawable(R.drawable.check))
                agreedKoinTermsButton.setImageDrawable(getDrawable(R.drawable.check))

                viewModel.updateButtonState(R.drawable.check, 1)
                viewModel.updateButtonState(R.drawable.check, 2)
                viewModel.updateButtonState(R.drawable.check, 3)

                isAllCheckButton = false
                isAgreedPrivacyTermsButton = false
                isAgreedKoinTerms = false
            }
            false -> {
                allCheckButton.setImageDrawable(getDrawable(R.drawable.check_selected))
                agreedPrivacyTermsButton.setImageDrawable(getDrawable(R.drawable.check_selected))
                agreedKoinTermsButton.setImageDrawable(getDrawable(R.drawable.check_selected))

                viewModel.updateButtonState(R.drawable.check_selected, 1)
                viewModel.updateButtonState(R.drawable.check_selected, 2)
                viewModel.updateButtonState(R.drawable.check_selected, 3)

                isAllCheckButton = true
                isAgreedPrivacyTermsButton = true
                isAgreedKoinTerms = true
            }
        }
        isButtonClickCheck()
    }

    private fun checkButtonClickEvent(button: ImageView) = with(binding) {
        when(button) {
            agreedPrivacyTermsButton -> {
                isAgreedPrivacyTermsButton = if(isAgreedPrivacyTermsButton) {
                    agreedPrivacyTermsButton.setImageDrawable(getDrawable(R.drawable.check))
                    viewModel.updateButtonState(R.drawable.check, 2)
                    false
                } else {
                    agreedPrivacyTermsButton.setImageDrawable(getDrawable(R.drawable.check_selected))
                    viewModel.updateButtonState(R.drawable.check_selected, 2)
                    true
                }
            }
            agreedKoinTermsButton -> {
                isAgreedKoinTerms = if(isAgreedKoinTerms) {
                    agreedKoinTermsButton.setImageDrawable(getDrawable(R.drawable.check))
                    viewModel.updateButtonState(R.drawable.check, 3)
                    false
                } else {
                    agreedKoinTermsButton.setImageDrawable(getDrawable(R.drawable.check_selected))
                    viewModel.updateButtonState(R.drawable.check_selected, 3)
                    true
                }
            }
        }
        isButtonClickCheck()
    }
}