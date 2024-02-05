package `in`.koreatech.koin.ui.businesssignup.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import `in`.koreatech.koin.R
import `in`.koreatech.koin.core.fragment.BaseFragment
import `in`.koreatech.koin.databinding.FragmentBusinessVerificationBinding
import `in`.koreatech.koin.domain.error.owner.OwnerError
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
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import java.util.concurrent.TimeUnit

class BusinessVerificationFragment: BaseFragment() {
    private var _binding: FragmentBusinessVerificationBinding? = null
    private val binding get() = _binding!!
    private val businessVerificationViewModel by activityViewModels<BusinessVerificationViewModel>()
    private val businessSignupBasicInfoViewModel by activityViewModels<BusinessSignUpBasicInfoViewModel>()
    private val businessSignupBaseViewModel by activityViewModels<BusinessSignUpBaseViewModel>()
    private lateinit var email: String
    private lateinit var password: String
    private lateinit var passwordConfirm: String

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentBusinessVerificationBinding.inflate(inflater, container, false)
        val view = binding.root

        binding.lifecycleOwner = this

        businessSignupBaseViewModel.setFragmentTag("VERIFICATION_FRAGMENT")

        initView()
        initViewModel()

        return view
    }

    private fun initView() = with(binding) {
        resendButton.setOnClickListener {
            businessSignupBasicInfoViewModel.continueBusinessSignup(
                email = email,
                password = password,
                passwordConfirm = passwordConfirm,
                isAgreedPrivacyTerms = true,
                isAgreedKoinTerms = true
            )

            SnackbarUtil.makeShortSnackbar(binding.root, "재전송되었습니다.")

            businessVerificationViewModel.startTimer()
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
                    is OwnerError.OverDueTimeException -> getString(R.string.overdue_time)
                    is OwnerError.IncorrectVerificationCodeException -> getString(R.string.incorrect_verification_code)
                    else -> getString(R.string.business_sign_up_error_when_verification_code)
                }
            )
        }

        lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.CREATED) {
                startTimer()
            }
        }

        observeLiveData(signUpInfo) { info ->
            email = info.first
            password = info.second
            passwordConfirm = info.third

            binding.accountVerificationInfoText.text = "${email}로\n 발송된 인증번호 6자리를 입력해주세요."
        }

        observeLiveData(curTime) {
            binding.limitTimeTextView.text = it
        }
    }
}