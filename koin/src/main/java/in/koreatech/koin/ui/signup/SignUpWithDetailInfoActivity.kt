package `in`.koreatech.koin.ui.signup

import android.content.Intent
import android.os.Bundle
import android.view.MotionEvent
import androidx.activity.viewModels
import androidx.core.widget.addTextChangedListener
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.flowWithLifecycle
import androidx.lifecycle.lifecycleScope
import dagger.hilt.android.AndroidEntryPoint
import `in`.koreatech.koin.R
import `in`.koreatech.koin.core.activity.ActivityBase
import `in`.koreatech.koin.databinding.ActivitySignUpWithDetailInfoBinding
import `in`.koreatech.koin.domain.error.signup.SignupAlreadySentEmailException
import `in`.koreatech.koin.domain.model.user.Gender
import `in`.koreatech.koin.domain.model.user.Graduated
import `in`.koreatech.koin.domain.state.signup.SignupContinuationState
import `in`.koreatech.koin.ui.signup.SignupActivity.Companion.SIGN_UP_EMAIL
import `in`.koreatech.koin.ui.signup.SignupActivity.Companion.SIGN_UP_PASSWORD
import `in`.koreatech.koin.ui.signup.viewmodel.SignupViewModel
import `in`.koreatech.koin.util.SnackbarUtil
import `in`.koreatech.koin.util.ext.hideKeyboard
import `in`.koreatech.koin.util.ext.observeLiveData
import `in`.koreatech.koin.util.ext.textString
import `in`.koreatech.koin.util.ext.withLoading
import kotlinx.coroutines.launch

@AndroidEntryPoint
class SignupWithDetailInfoActivity : ActivityBase() {

    private lateinit var binding: ActivitySignUpWithDetailInfoBinding
    private val signupViewModel by viewModels<SignupViewModel>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySignUpWithDetailInfoBinding.inflate(layoutInflater)
        setContentView(binding.root)

        initView()
        observeData()
    }

    private fun initView() = with(binding) {
        signupBackButton.setOnClickListener { finish() }

        signupViewModel.setAccount(
            intent.getStringExtra(SIGN_UP_EMAIL) ?: "",
            intent.getStringExtra(SIGN_UP_PASSWORD) ?: ""
        )

        signupUserButtonNicknameCheck.setOnClickListener {
            if (signupUserEdittextNickName.text.toString() != "") {
                signupViewModel.checkNickname(signupUserEdittextNickName.textString.trim())
            }
        }

        signupSendVerificationButton.setOnClickListener {
            signupViewModel.continueDetailSignup(
                portalAccount = signupViewModel.portalEmail,
                gender = when {
                    signupUserRadiobuttonGenderMan.isChecked -> Gender.Man
                    signupUserRadiobuttonGenderWoman.isChecked -> Gender.Woman
                    else -> null
                },
                isGraduated = when {
                    signupUserRadiobuttonGraduate.isChecked -> Graduated.Graduate
                    signupUserRadiobuttonStudent.isChecked -> Graduated.Student
                    else -> null
                },
                major = signupUserEdittextMajor.text.toString(),
                name = signupUserEdittextName.text.toString().trim(),
                nickName = signupUserEdittextNickName.text.toString().trim(),
                password = signupViewModel.password.trim(),
                phoneNumber = signupUserEdittextPhoneNumber.text.toString(),
                studentNumber = signupUserEdittextStudentId.text.toString(),
                isCheckNickname = signupViewModel.isCheckedNickname
            )
        }


        signupUserEdittextNickName.addTextChangedListener {
            if (signupViewModel.isCheckedNickname) signupViewModel.isCheckedNickname = false
        }

        signupUserEdittextStudentId.addTextChangedListener {
            if (signupViewModel.isPerformDept) signupViewModel.isPerformDept = false
            signupViewModel.getDept(it.toString())
        }

        signupUserEdittextMajor.apply {
            isEnabled = false
            hint = context.getString(R.string.user_info_id_hint)
        }
    }

    private fun observeData() = with(signupViewModel) {
        withLoading(this@SignupWithDetailInfoActivity, this)

        lifecycleScope.launch {
            signupViewModel.signupContinuationState.flowWithLifecycle(
                lifecycle,
                Lifecycle.State.STARTED
            ).collect { state ->
                when (state) {
                    SignupContinuationState.InitName -> SnackbarUtil.makeShortSnackbar(binding.root, getString(R.string.signup_init_name))

                    SignupContinuationState.InitPhoneNumber -> SnackbarUtil.makeShortSnackbar(binding.root,getString(R.string.signup_init_phone_number))

                    SignupContinuationState.InitStudentId -> SnackbarUtil.makeShortSnackbar(binding.root, getString(R.string.signup_init_student_id))

                    SignupContinuationState.CheckNickName -> SnackbarUtil.makeShortSnackbar(binding.root,getString(R.string.signup_check_nickname))

                    SignupContinuationState.CheckGender -> SnackbarUtil.makeShortSnackbar(binding.root,getString(R.string.signup_check_gender))

                    SignupContinuationState.CheckGraduate -> SnackbarUtil.makeShortSnackbar(binding.root, getString(R.string.signup_check_graduate))

                    SignupContinuationState.CheckDept -> SnackbarUtil.makeShortSnackbar(binding.root, getString(R.string.user_info_no_major))

                    SignupContinuationState.NicknameDuplicated -> SnackbarUtil.makeShortSnackbar(binding.root,getString(R.string.error_nickname_duplicated))

                    SignupContinuationState.AvailableNickname -> SnackbarUtil.makeShortSnackbar(binding.root, getString(R.string.signup_nickname_available))

                    SignupContinuationState.RequestedEmailValidation -> {
                        startSignupCompleteActivity()
                    }

                    is SignupContinuationState.Failed -> {
                        when (state.throwable) {
                            is SignupAlreadySentEmailException -> getString(R.string.signup_error_email_already_send_or_email_requested)
                            else -> {
                                SnackbarUtil.makeShortSnackbar(binding.root, state.message)
                            }
                        }
                    }

                    else -> Unit
                }
            }
        }

        observeLiveData(dept) {
            binding.signupUserEdittextMajor.setText(it)
            binding.signupUserEdittextMajorError.text = ""
        }

        observeLiveData(getDeptErrorMessage) {
            binding.signupUserEdittextMajorError.text = it
        }
    }

    override fun dispatchTouchEvent(ev: MotionEvent?): Boolean {
        hideKeyboard(currentFocus ?: binding.root)
        return super.dispatchTouchEvent(ev)
    }

    private fun startSignupCompleteActivity() {
        val intent = Intent(this@SignupWithDetailInfoActivity, SignUpCompleteActivity::class.java)
        startActivity(intent)
    }
}