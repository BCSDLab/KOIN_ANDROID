package `in`.koreatech.koin.ui.businesssignup

import `in`.koreatech.koin.R
import `in`.koreatech.koin.core.activity.ActivityBase
import `in`.koreatech.koin.core.util.dataBinding
import `in`.koreatech.koin.databinding.ActivityBusinessVerificationBinding
import `in`.koreatech.koin.domain.error.owner.IncorrectVerificationCodeException
import `in`.koreatech.koin.domain.error.owner.OverDueTimeException
import `in`.koreatech.koin.domain.error.signup.SignupAlreadySentEmailException
import `in`.koreatech.koin.ui.businesssignup.viewmodel.BusinessSignupViewModel
import `in`.koreatech.koin.ui.businesssignup.viewmodel.BusinessVerificationViewModel
import `in`.koreatech.koin.util.SnackbarUtil
import `in`.koreatech.koin.util.ext.observeLiveData
import `in`.koreatech.koin.util.ext.withLoading
import android.content.Intent
import android.os.Bundle
import androidx.activity.viewModels
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.*
import java.util.concurrent.TimeUnit

@AndroidEntryPoint
class BusinessVerificationActivity : ActivityBase(R.layout.activity_business_verification) {
    private val binding by dataBinding<ActivityBusinessVerificationBinding>()
    private val businessVerificationViewModel by viewModels<BusinessVerificationViewModel>()
    private val businessSignupViewModel by viewModels<BusinessSignupViewModel>()
    private var timer: Job? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_business_verification)

        startTimer()
        initView()
        initViewModel()
    }

    private fun initView() = with(binding) {
        val email = intent.getStringExtra("email")
        accountVerificationInfoText.text = "${email}로\n발송된 인증번호 6자리를 입력해 주세요."

        resendButton.setOnClickListener {
            timer!!.cancel()

            val password = intent.getStringExtra("password")
            val passwordConfirm = intent.getStringExtra("password_confirm");

            businessSignupViewModel.continueBusinessSignup(
                email = email!!,
                password = password!!,
                passwordConfirm = passwordConfirm!!,
                isAgreedPrivacyTerms = true,
                isAgreedKoinTerms = true
            )

            binding.limitTimeTextView.text = getString(R.string.time_limit_five_minute)
            startTimer()
        }

        nextButton.setOnClickListener {
            businessVerificationViewModel.continueVerificationEmail(
                email = if(!email.isNullOrBlank()) email else "",
                verificationCode = editCertificationNumberText.text.toString()
            )
        }

        signupBackButton.setOnClickListener {
            finish()
        }

    }

    private fun initViewModel() = with(businessVerificationViewModel) {
        withLoading(this@BusinessVerificationActivity, this)

        observeLiveData(businessVerificationContinuationState) {
            startActivity(Intent(this@BusinessVerificationActivity, BusinessCertificationActivity::class.java))
        }

        observeLiveData(businessVerificationContinuationError) { t ->
            SnackbarUtil.makeShortSnackbar(
                binding.root,
                when(t) {
                    is SignupAlreadySentEmailException -> getString(R.string.signup_error_email_already_send_or_email_requested)
                    is OverDueTimeException -> getString(R.string.overdue_time)
                    is IncorrectVerificationCodeException -> getString(R.string.incorrect_verification_code)
                    else -> getString(R.string.business_sign_up_error_when_verification_code)
                }
            )
        }
    }

    private fun startTimer() {
        val delayTime = 1000L
        var timeRemaining = TimeUnit.MINUTES.toMillis(5)

        timer = CoroutineScope(Dispatchers.Main).launch {
            while (timeRemaining > 0) {
                delay(delayTime)

                val curMinutes = TimeUnit.MILLISECONDS.toMinutes(timeRemaining)
                val curSeconds = TimeUnit.MILLISECONDS.toSeconds(timeRemaining) - TimeUnit.MINUTES.toSeconds(curMinutes)

                val limitTimeTextView = binding.limitTimeTextView
                limitTimeTextView.text = String.format("%02d:%02d", curMinutes, curSeconds)

                timeRemaining -= delayTime
            }
        }
    }
}