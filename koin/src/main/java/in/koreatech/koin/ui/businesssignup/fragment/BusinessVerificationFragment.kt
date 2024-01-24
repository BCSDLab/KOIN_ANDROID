package `in`.koreatech.koin.ui.businesssignup.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.activityViewModels
import `in`.koreatech.koin.R
import `in`.koreatech.koin.core.fragment.BaseFragment
import `in`.koreatech.koin.databinding.FragmentBusinessVerificationBinding
import `in`.koreatech.koin.domain.error.owner.IncorrectVerificationCodeException
import `in`.koreatech.koin.domain.error.owner.OverDueTimeException
import `in`.koreatech.koin.domain.error.signup.SignupAlreadySentEmailException
import `in`.koreatech.koin.ui.businesssignup.viewmodel.BusinessSignUpBaseViewModel
import `in`.koreatech.koin.ui.businesssignup.viewmodel.BusinessSignUpBasicInfoViewModel
import `in`.koreatech.koin.ui.businesssignup.viewmodel.BusinessVerificationViewModel
import `in`.koreatech.koin.util.SnackbarUtil
import `in`.koreatech.koin.util.ext.observeLiveData
import `in`.koreatech.koin.util.ext.withLoading
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import java.util.concurrent.TimeUnit

class BusinessVerificationFragment: BaseFragment() {
    private var _binding: FragmentBusinessVerificationBinding? = null
    private val binding get() = _binding!!
    private val businessVerificationViewModel by activityViewModels<BusinessVerificationViewModel>()
    private val businessSignupBasicInfoViewModel by activityViewModels<BusinessSignUpBasicInfoViewModel>()
    private val businessSignupBaseViewModel by activityViewModels<BusinessSignUpBaseViewModel>()

    private var timer: Job? = null

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentBusinessVerificationBinding.inflate(inflater, container, false)
        val view = binding.root

        businessSignupBaseViewModel.setFragmentTag("verificationFragment")

        startTimer()
        initView()
        initViewModel()

        return view
    }

    private fun initView() = with(binding) {
        val email = arguments?.getString("email") ?: "error"
        accountVerificationInfoText.text = "${email}로\n 발송된 인증번호 6자리를 입력해 주세요."

        resendButton.setOnClickListener {
            timer!!.cancel()

            val password = arguments?.getString("password") ?: ""
            val passwordConfirm = arguments?.getString("password_confirm") ?: "error"

            businessSignupBasicInfoViewModel.continueBusinessSignup(
                email = email,
                password = password,
                passwordConfirm = passwordConfirm,
                isAgreedPrivacyTerms = true,
                isAgreedKoinTerms = true
            )

            SnackbarUtil.makeShortSnackbar(binding.root, "재전송되었습니다.")

            binding.limitTimeTextView.text = getString(R.string.time_limit_five_minute)
            startTimer()
        }

        nextButton.setOnClickListener {
            businessVerificationViewModel.continueVerificationEmail(
                email = email.ifBlank { "" },
                verificationCode = editCertificationNumberText.text.toString()
            )
        }
    }

    private fun initViewModel() = with(businessVerificationViewModel) {
        withLoading(this@BusinessVerificationFragment, this)

        observeLiveData(businessVerificationContinuationState) {
            val nextFragment = BusinessCertificationFragment()
            parentFragmentManager.beginTransaction().replace(R.id.fragment_container_view, nextFragment).commit()
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