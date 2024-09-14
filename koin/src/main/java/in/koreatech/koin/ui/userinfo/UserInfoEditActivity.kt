package `in`.koreatech.koin.ui.userinfo

import android.os.Bundle
import androidx.activity.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import dagger.hilt.android.AndroidEntryPoint
import `in`.koreatech.koin.R
import `in`.koreatech.koin.core.activity.ActivityBase
import `in`.koreatech.koin.core.toast.ToastUtil
import `in`.koreatech.koin.core.util.dataBinding
import `in`.koreatech.koin.core.util.setAppBarButtonClickedListener
import `in`.koreatech.koin.databinding.ActivityUserInfoEditedBinding
import `in`.koreatech.koin.domain.model.user.Gender
import `in`.koreatech.koin.domain.model.user.User
import `in`.koreatech.koin.ui.userinfo.contract.UserInfoEditContract
import `in`.koreatech.koin.ui.userinfo.state.NicknameCheckState
import `in`.koreatech.koin.ui.userinfo.viewmodel.UserInfoEditViewModel
import `in`.koreatech.koin.util.ext.observeLiveData
import `in`.koreatech.koin.util.ext.withLoading
import kotlinx.coroutines.launch

@AndroidEntryPoint
class UserInfoEditActivity : ActivityBase() {
    private val binding by dataBinding<ActivityUserInfoEditedBinding>(R.layout.activity_user_info_edited)
    override val screenTitle = "내 정보 수정"
    private val userInfoEditViewModel by viewModels<UserInfoEditViewModel>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

        initView()
        initViewModel()

        userInfoEditViewModel.getUserInfo()
    }

    private fun initView() = with(binding) {
        appbarUserInfoEdit.setAppBarButtonClickedListener(
            leftButtonClicked = {
                onBackPressed()
            },
            rightButtonClicked = {}
        )
        spinnerMajor.lifecycleOwner = this@UserInfoEditActivity

        btnConfirm.setOnClickListener {
            userInfoEditViewModel.updateUserInfo(
                name = etName.text.toString(),
                nickname = etNickname.text.toString(),
                rawPhoneNumber = tvPhoneNumber.text.toString(),
                gender =
                    if (rbGenderMan.isChecked)
                        Gender.Man
                    else if (rbGenderWoman.isChecked)
                        Gender.Woman
                    else
                        Gender.Unknown,
                studentId = etStudentId.text.toString(),
                major = spinnerMajor.text.toString()
            )
        }
    }

    private fun initViewModel() {
        with(userInfoEditViewModel) {
            withLoading(this@UserInfoEditActivity, this)

            observeLiveData(user) { user ->
                when (user) {
                    User.Anonymous -> {
                        ToastUtil.getInstance().makeShort(getString(R.string.user_info_anonymous))
                        finish()
                    }

                    is User.Student ->
                        with(binding) {
                            tvId.text = user.email
                            etName.setText(user.name)
                            etNickname.setText(user.nickname)
                            tvPhoneNumber.setText(user.phoneNumber?.filter { it != '-' })
                            etStudentId.setText(user.studentNumber)
                            when (user.gender) {
                                is Gender.Man -> {
                                    binding.rbGenderMan.isChecked = true
                                }

                                is Gender.Woman -> {
                                    binding.rbGenderWoman.isChecked = true
                                }

                                is Gender.Unknown -> {
                                    binding.rbGenderMan.isChecked = false
                                    binding.rbGenderWoman.isChecked = false
                                }
                            }
                        }
                }
            }

            observeLiveData(toastErrorMessage) {
                ToastUtil.getInstance().makeShort(it)
            }

            observeLiveData(nicknameDuplicatedEvent) {
                when (it) {
                    NicknameCheckState.POSSIBLE -> {
                        ToastUtil.getInstance().makeShort(R.string.nickname_available)
                    }

                    NicknameCheckState.SAME_AS_BEFORE -> {
                        ToastUtil.getInstance().makeShort(R.string.edit_user_error_same_as_before)
                    }

                    NicknameCheckState.EXIST -> {
                        ToastUtil.getInstance().makeShort(R.string.error_nickname_duplicated)
                    }
                }
            }

            observeLiveData(userInfoEditedEvent) {
                ToastUtil.getInstance().makeShort(getString(R.string.user_info_edited))
                setResult(UserInfoEditContract.RESULT_USER_INFO_EDITED)
                finish()
            }

            lifecycleScope.launch {
                repeatOnLifecycle(Lifecycle.State.STARTED) {
                    depts.collect { (depts, userMajor) ->
                        binding.spinnerMajor.setItems(depts)
                        with(binding.spinnerMajor) {
                            setItems(depts)
                            val pos = depts.indexOf(userMajor)
                            if (pos != -1) {
                                selectItemByIndex(pos)
                            }
                        }
                    }
                }
            }
        }
    }

//    private fun checkGenderBtn(gender: Gender) {
//        when(gender) {
//            is Gender.Man -> {
//                binding.btnGenderMale.setBackgroundColor(resources.getColor(R.color.primary_500, null))
//                binding.btnGenderMale.setTextColor(resources.getColor(R.color.neutral_0, null))
//
//                binding.btnGenderFemale.setBackgroundColor(resources.getColor(R.color.neutral_100, null))
//                binding.btnGenderFemale.setTextColor(resources.getColor(R.color.neutral_800, null))
//            }
//            is Gender.Woman -> {
//                binding.btnGenderFemale.setBackgroundColor(resources.getColor(R.color.primary_500, null))
//                binding.btnGenderFemale.setTextColor(resources.getColor(R.color.neutral_0, null))
//
//                binding.btnGenderMale.setBackgroundColor(resources.getColor(R.color.neutral_100, null))
//                binding.btnGenderMale.setTextColor(resources.getColor(R.color.neutral_800, null))
//            }
//            else -> {
//                binding.btnGenderFemale.setBackgroundColor(resources.getColor(R.color.neutral_100, null))
//                binding.btnGenderFemale.setTextColor(resources.getColor(R.color.neutral_800, null))
//
//                binding.btnGenderMale.setBackgroundColor(resources.getColor(R.color.neutral_100, null))
//                binding.btnGenderMale.setTextColor(resources.getColor(R.color.neutral_800, null))
//            }
//        }
//    }
}