package `in`.koreatech.koin.ui.signup

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.Toast
import androidx.activity.viewModels
import androidx.core.content.ContentProviderCompat.requireContext
import androidx.core.widget.addTextChangedListener
import dagger.hilt.android.AndroidEntryPoint
import `in`.koreatech.koin.R
import `in`.koreatech.koin.core.activity.ActivityBase
import `in`.koreatech.koin.core.toast.ToastUtil
import `in`.koreatech.koin.databinding.ActivitySignUpWithDetailInfoBinding
import `in`.koreatech.koin.domain.error.signup.SignupAlreadySentEmailException
import `in`.koreatech.koin.domain.model.user.Gender
import `in`.koreatech.koin.domain.model.user.Graduated
import `in`.koreatech.koin.domain.state.signup.SignupContinuationState
import `in`.koreatech.koin.ui.signup.viewmodel.SignupViewModel
import `in`.koreatech.koin.util.SnackbarUtil
import `in`.koreatech.koin.util.ext.observeLiveData
import `in`.koreatech.koin.util.ext.textString
import `in`.koreatech.koin.util.ext.withLoading

@AndroidEntryPoint
class SignUpWithDetailInfoActivity : ActivityBase() {

    private lateinit var binding: ActivitySignUpWithDetailInfoBinding
    private val signupViewModel by viewModels<SignupViewModel>()
    private var checkNickName = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySignUpWithDetailInfoBinding.inflate(layoutInflater)
        setContentView(binding.root)

        initView()
        initViewModel()
        initSpinner()
    }

    private fun initView() = with(binding){
        signupBackButton.setOnClickListener{
            finish()
        }

        signupUserButtonNicknameCheck.setOnClickListener {

            if(signupUserEdittextNickName.text.toString() != "") {
                signupViewModel.checkNickname(signupUserEdittextNickName.textString)
            }
        }

        signupSendVerificationButton.setOnClickListener {
            signupViewModel.continueDetailSignup(
                portalAccount = intent.getStringExtra("email").toString(),
                gender = when{
                    signupUserRadiobuttonGenderMan.isChecked -> Gender.Man
                    signupUserRadiobuttonGenderWoman.isChecked -> Gender.Woman
                    else -> null
                },
                isGraduated = when{
                    signupUserRadiobuttonGraduate.isChecked -> Graduated.Graduate
                    signupUserRadiobuttonStudent.isChecked -> Graduated.Student
                    else -> null
                },
                major = signupUserEdittextMajor.text.toString(),
                name = signupUserEdittextName.text.toString(),
                nickName = signupUserEdittextNickName.text.toString(),
                password = intent.getStringExtra("password").toString(),
                phoneNumber = signupUserEdittextPhoneNumber.text.toString(),
                studentNumber = signupUserEdittextStudentId.text.toString(),
                isCheckNickname = checkNickName
            )
        }

        signupUserEdittextStudentId.addTextChangedListener {
            signupViewModel.getDept(it.toString())
        }

    }

    private fun initViewModel() = with(signupViewModel){
        withLoading(this@SignUpWithDetailInfoActivity, this)
        observeLiveData(signupDetailContinuationState) { state ->
            when (state) {
                SignupContinuationState.InitName -> {
                    SnackbarUtil.makeShortSnackbar(
                        binding.root,
                        getString(R.string.signup_init_name)
                    )
                }

                SignupContinuationState.InitNickName -> {
                    SnackbarUtil.makeShortSnackbar(
                        binding.root,
                        getString(R.string.signup_init_nickname)
                    )
                }

                SignupContinuationState.InitPhoneNumber -> {
                    SnackbarUtil.makeShortSnackbar(
                        binding.root,
                        getString(R.string.signup_init_phone_number)
                    )
                }

                SignupContinuationState.InitStudentId -> {
                    SnackbarUtil.makeShortSnackbar(
                        binding.root,
                        getString(R.string.signup_init_student_id)
                    )
                }

                SignupContinuationState.InitMajor -> {
                    SnackbarUtil.makeShortSnackbar(
                        binding.root,
                        getString(R.string.signup_init_major)
                    )
                }

                SignupContinuationState.CheckNickName -> {
                    SnackbarUtil.makeShortSnackbar(
                        binding.root,
                        getString(R.string.signup_check_nickname)
                    )
                }

                SignupContinuationState.CheckGender ->{
                    SnackbarUtil.makeShortSnackbar(
                        binding.root,
                        getString(R.string.signup_check_gender)
                    )
                }

                SignupContinuationState.CheckGraduate ->{
                    SnackbarUtil.makeShortSnackbar(
                        binding.root,
                        getString(R.string.signup_check_graduate)
                    )
                }

                SignupContinuationState.RequestedEmailValidation -> {
                    val intent = Intent(this@SignUpWithDetailInfoActivity, SignUpCompleteActivity::class.java)
                    startActivity(intent)
                }
            }
        }
        observeLiveData(signupContinuationError) { t ->
            SnackbarUtil.makeShortSnackbar(
                binding.root,
                when (t) {
                    is SignupAlreadySentEmailException -> getString(R.string.signup_error_email_already_send_or_email_requested)
                    else -> getString(R.string.signup_error_when_email_validation)
                }
            )
        }

        observeLiveData(nicknameDuplicatedEvent) {
            if (it) {
                ToastUtil.getInstance().makeShort(R.string.error_nickname_duplicated)
                checkNickName = false
            }
            else {
                ToastUtil.getInstance().makeShort(R.string.nickname_available)
                checkNickName = true
            }
        }

    }

    private fun initSpinner() {
        ArrayAdapter.createFromResource(
            this,
            R.array.major_array,
            android.R.layout.simple_spinner_item
        ).also { adapter ->
            adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
            binding.signupUserSpinnerMajor.adapter = adapter
        }

        binding.signupUserSpinnerMajor.onItemSelectedListener =
            object: AdapterView.OnItemSelectedListener {
                override fun onItemSelected(p0: AdapterView<*>?, p1: View?, p2: Int, p3: Long) {
                    if (p0 != null) {
                        binding.signupUserEdittextMajor.text = p0.getItemAtPosition(p2).toString()
                    }
                }

                override fun onNothingSelected(p0: AdapterView<*>?) {
                    binding.signupUserEdittextMajor.text =""
                    return
                }
            }
    }
}