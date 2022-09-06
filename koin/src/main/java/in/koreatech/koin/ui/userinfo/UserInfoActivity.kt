package `in`.koreatech.koin.ui.userinfo

import `in`.koreatech.koin.R
import `in`.koreatech.koin.core.toast.ToastUtil
import `in`.koreatech.koin.core.util.dataBinding
import `in`.koreatech.koin.core.util.setAppBarButtonClickedListener
import `in`.koreatech.koin.databinding.ActivityUserInfoBinding
import `in`.koreatech.koin.ui.login.LoginActivity
import `in`.koreatech.koin.ui.navigation.KoinNavigationDrawerActivity
import `in`.koreatech.koin.ui.navigation.state.MenuState
import `in`.koreatech.koin.ui.userinfo.state.toUserState
import `in`.koreatech.koin.ui.userinfo.viewmodel.UserInfoViewModel
import `in`.koreatech.koin.util.SnackbarUtil
import `in`.koreatech.koin.util.ext.observeLiveData
import `in`.koreatech.koin.util.ext.withLoading
import android.content.Intent
import android.os.Bundle
import androidx.activity.viewModels
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class UserInfoActivity : KoinNavigationDrawerActivity() {
    override val menuState: MenuState = MenuState.UserInfo

    private val binding by dataBinding<ActivityUserInfoBinding>(R.layout.activity_user_info)
    private val userInfoViewModel by viewModels<UserInfoViewModel>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

        initView()
        initViewModel()

        userInfoViewModel.getUserInfo()
    }

    private fun initView() = with(binding) {
        koinBaseAppBar.setAppBarButtonClickedListener(
            leftButtonClicked = {
                onBackPressed()
            },
            rightButtonClicked = {
                startActivity(Intent(this@UserInfoActivity, UserInfoEditedActivity::class.java))
            }
        )

        userinfoButtonDeleteUser.setOnClickListener {
            SnackbarUtil.makeLongSnackbarActionYes(userinfoScrollview, getString(R.string.user_info_user_remove_message)
            ) {
                userInfoViewModel.removeUser()
            }
        }

        userinfoButtonLogoutUser.setOnClickListener {
            userInfoViewModel.logout()
        }
    }

    private fun initViewModel() = with(userInfoViewModel) {
        withLoading(this@UserInfoActivity, this)

        observeLiveData(user) { user ->
            user?.let {
                val userState = it.toUserState(this@UserInfoActivity)
                binding.userinfoTextviewId.text = userState.portalAccount
                binding.userinfoTextviewName.text = userState.username
                binding.userinfoTextviewNickName.text = userState.userNickname
                binding.userinfoTextviewAnonymousNickName.text = userState.userAnonymousNickname
                binding.userinfoTextviewPhoneNum.text = userState.phoneNumber
                binding.userinfoTextviewGender.text = userState.gender
                binding.userinfoTextviewStudentId.text = userState.studentNumber
                binding.userinfoTextviewMajor.text = userState.major
            } ?: ToastUtil.getInstance().makeShort(getString(R.string.user_info_anonymous))
        }

        observeLiveData(getUserErrorMessage) {
            ToastUtil.getInstance().makeShort(it)
        }

        observeLiveData(logoutEvent) {
            finishAffinity()
            startActivity(Intent(this@UserInfoActivity, LoginActivity::class.java))
        }

        observeLiveData(logoutErrorMessage) {
            ToastUtil.getInstance().makeShort(it)
        }

        observeLiveData(userRemoveEvent) {
            val intent = Intent(this@UserInfoActivity, LoginActivity::class.java)
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)
            startActivity(intent)
            finish()
        }

        observeLiveData(userRemoveErrorMessage) {
            ToastUtil.getInstance().makeShort(it)
        }
    }
}