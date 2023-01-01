package `in`.koreatech.koin.ui.userinfo

import `in`.koreatech.koin.R
import `in`.koreatech.koin.core.toast.ToastUtil
import `in`.koreatech.koin.core.util.dataBinding
import `in`.koreatech.koin.core.util.setAppBarButtonClickedListener
import `in`.koreatech.koin.databinding.ActivityUserInfoEditedBinding
import `in`.koreatech.koin.domain.model.user.Gender
import `in`.koreatech.koin.ui.navigation.KoinNavigationDrawerActivity
import `in`.koreatech.koin.ui.navigation.state.MenuState
import `in`.koreatech.koin.ui.userinfo.contract.UserInfoEditContract
import `in`.koreatech.koin.ui.userinfo.viewmodel.UserInfoEditViewModel
import `in`.koreatech.koin.util.ext.*
import android.os.Bundle
import androidx.activity.viewModels
import androidx.core.widget.addTextChangedListener
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class UserInfoEditActivity : KoinNavigationDrawerActivity() {
    override val menuState: MenuState = MenuState.UserInfo

    private val binding by dataBinding<ActivityUserInfoEditedBinding>(R.layout.activity_user_info_edited)
    private val userInfoEditViewModel by viewModels<UserInfoEditViewModel>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

        initView()
        initViewModel()

        userInfoEditViewModel.getUserInfo()
    }

    private fun initView() = with(binding) {
        koinBaseAppBar.setAppBarButtonClickedListener(
            leftButtonClicked = {
                onBackPressed()
            },
            rightButtonClicked = {
                userInfoEditViewModel.updateUserInfo(
                    name = binding.userinfoeditedEdittextName.textString,
                    nickname = binding.userinfoeditedEdittextNickName.textString,
                    separatedPhoneNumber = arrayOf(
                        binding.userinfoeditedEdittextPhoneNum1,
                        binding.userinfoeditedEdittextPhoneNum2,
                        binding.userinfoeditedEdittextPhoneNum3
                    ).map { it.textString },
                    gender = when {
                        binding.userinfoeditedRadiobuttonGenderMan.isChecked -> Gender.Man
                        binding.userinfoeditedRadiobuttonGenderWoman.isChecked -> Gender.Woman
                        else -> null
                    },
                    studentId = binding.userinfoeditedEdittextStudentId.textString
                )
            }
        )

        userinfoeditedEdittextName.addTextChangedListener {
            userInfoEditViewModel
        }

        userinfoeditedEdittextStudentId.addTextChangedListener {
            userInfoEditViewModel.getDept(it.toString())
        }

        userinfoeditedButtonNicknameCheck.setOnClickListener {
            userInfoEditViewModel.checkNickname(userinfoeditedEdittextNickName.textString)
        }
    }

    private fun initViewModel() {
        with(userInfoEditViewModel) {
            withLoading(this@UserInfoEditActivity, this)

            observeLiveData(user) { user ->
                if (user != null) {
                    with(binding) {
                        userinfoeditedTextviewId.text =
                            getString(R.string.koreatech_email_postfix, user.portalAccount)
                        userinfoeditedTextviewAnonymousNickName.text = user.anonymousNickname

                        userinfoeditedEdittextName.apply {
                            setText(user.name)

                            if (user.name.isNullOrEmpty()) {
                                isEnabled = true
                                setDefaultBackground()
                            } else {
                                isEnabled = false
                                setTransparentBackground()
                            }
                        }

                        userinfoeditedEdittextNickName.setText(user.nickname)

                        if (user.phoneNumber.isNullOrEmpty()) {
                            userinfoeditedEdittextPhoneNum1.setText("")
                            userinfoeditedEdittextPhoneNum2.setText("")
                            userinfoeditedEdittextPhoneNum3.setText("")
                        } else {
                            val (first, middle, end) = user.phoneNumber!!.splitPhoneNumber()
                            userinfoeditedEdittextPhoneNum1.setText(first)
                            userinfoeditedEdittextPhoneNum2.setText(middle)
                            userinfoeditedEdittextPhoneNum3.setText(end)
                        }

                        when (user.gender) {
                            Gender.Man -> userinfoeditedRadiobuttonGenderMan.isChecked = true
                            Gender.Woman -> userinfoeditedRadiobuttonGenderWoman.isChecked = true
                            else -> {
                                userinfoeditedRadiobuttonGenderMan.isChecked = false
                                userinfoeditedRadiobuttonGenderWoman.isChecked = false
                            }
                        }

                        userinfoeditedEdittextStudentId.apply {
                            setText(user.studentNumber)
                            if (user.studentNumber == null || user.studentNumber?.isEmpty() == false) {
                                isEnabled = true
                                setDefaultBackground()
                                userinfoeditedEdittextMajor.setText("")
                            } else {
                                isEnabled = false
                                setTransparentBackground()
                                userInfoEditViewModel.getDept(user.studentNumber!!)
                            }
                        }

                        userinfoeditedEdittextMajor.apply {
                            isEnabled = false
                            hint = context.getString(R.string.user_info_id_hint)
                        }
                    }


                } else {
                    ToastUtil.getInstance().makeShort(getString(R.string.user_info_anonymous))
                    finish()
                }
            }

            observeLiveData(dept) {
                binding.userinfoeditedEdittextMajor.setText(it)
                binding.userinfoeditedEdittextMajorError.text = ""
            }

            observeLiveData(getDeptErrorMessage) {
                binding.userinfoeditedEdittextMajorError.text = it
            }

            observeLiveData(toastErrorMessage) {
                ToastUtil.getInstance().makeShort(it)
            }

            observeLiveData(nicknameDuplicatedEvent) {
                if (it) ToastUtil.getInstance().makeShort(R.string.error_nickname_duplicated)
                else ToastUtil.getInstance().makeShort(R.string.nickname_available)
            }

            observeLiveData(userInfoEditedEvent) {
                ToastUtil.getInstance().makeShort(getString(R.string.user_info_edited))
                setResult(UserInfoEditContract.RESULT_USER_INFO_EDITED)
                finish()
            }
        }
    }
}