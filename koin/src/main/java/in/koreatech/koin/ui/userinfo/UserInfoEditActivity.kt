package `in`.koreatech.koin.ui.userinfo

import android.os.Bundle
import android.util.Log
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
import `in`.koreatech.koin.util.DebounceTextWatcher
import `in`.koreatech.koin.util.SnackbarUtil
import `in`.koreatech.koin.util.ext.observeLiveData
import `in`.koreatech.koin.util.ext.textString
import `in`.koreatech.koin.util.ext.withLoading
import kotlinx.coroutines.launch

@AndroidEntryPoint
class UserInfoEditActivity : ActivityBase() {
    private val binding by dataBinding<ActivityUserInfoEditedBinding>(R.layout.activity_user_info_edited)
    override val screenTitle = "내 정보 수정"
    private val userInfoEditViewModel by viewModels<UserInfoEditViewModel>()

    private val nicknameWatcher by lazy {
        DebounceTextWatcher(lifecycleScope, 0L) {
            userInfoEditViewModel.onNickNameChanged(it)
        }
    }


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

        etNickname.addTextChangedListener(nicknameWatcher)
        spinnerMajor.lifecycleOwner = this@UserInfoEditActivity

        btnConfirm.setOnClickListener {
            userInfoEditViewModel.updateUserInfo(
                name = etName.text.toString(),
                nickname = etNickname.text.toString(),
                rawPhoneNumber = etPhoneNumber.text.toString(),
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

        btnNicknameDuplication.setOnClickListener {
            userInfoEditViewModel.checkNickname(etNickname.textString)
        }
        invalidateNickNameViews(false)
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
                            etPhoneNumber.setText(user.phoneNumber)
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
                    NicknameCheckState.NEED_CHECK -> {
                        invalidateNickNameViews(true)
                    }

                    NicknameCheckState.POSSIBLE -> {
                        invalidateNickNameViews(false)
                    }

                    NicknameCheckState.SAME_AS_BEFORE -> {
                        invalidateNickNameViews(false)
                    }

                    NicknameCheckState.EXIST -> {
                        SnackbarUtil.makeShortSnackbar(binding.root, getString(R.string.error_nickname_duplicated))
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

    private fun invalidateNickNameViews(isDuplicated: Boolean) {
        with(binding) {
            if (isDuplicated) {
                btnConfirm.text = getString(R.string.user_info_nickname_duplication)
                btnConfirm.isEnabled = false
                btnNicknameDuplication.isEnabled = true
            } else {
                btnConfirm.text = getString(R.string.common_save)
                btnConfirm.isEnabled = true
                btnNicknameDuplication.isEnabled = false
            }
        }
    }

    override fun onDestroy() {
        binding.etNickname.removeTextChangedListener(nicknameWatcher)

        super.onDestroy()
    }
}