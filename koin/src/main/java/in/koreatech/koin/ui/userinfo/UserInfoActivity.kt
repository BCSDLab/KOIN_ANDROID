package `in`.koreatech.koin.ui.userinfo

import android.content.Intent
import android.os.Bundle
import androidx.activity.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.flowWithLifecycle
import androidx.lifecycle.lifecycleScope
import dagger.hilt.android.AndroidEntryPoint
import `in`.koreatech.koin.R
import `in`.koreatech.koin.common.UiStatus
import `in`.koreatech.koin.core.toast.ToastUtil
import `in`.koreatech.koin.core.util.dataBinding
import `in`.koreatech.koin.core.util.setAppBarButtonClickedListener
import `in`.koreatech.koin.databinding.ActivityUserInfoBinding
import `in`.koreatech.koin.ui.login.LoginActivity
import `in`.koreatech.koin.ui.navigation.KoinNavigationDrawerActivity
import `in`.koreatech.koin.ui.navigation.state.MenuState
import `in`.koreatech.koin.ui.userinfo.contract.UserInfoEditContract
import `in`.koreatech.koin.ui.userinfo.state.toUserState
import `in`.koreatech.koin.ui.userinfo.viewmodel.UserInfoViewModel
import `in`.koreatech.koin.util.SnackbarUtil
import `in`.koreatech.koin.util.ext.observeLiveData
import `in`.koreatech.koin.util.ext.withLoading
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch

@AndroidEntryPoint
class UserInfoActivity : KoinNavigationDrawerActivity() {
    override val menuState: MenuState = MenuState.UserInfo

    private val binding by dataBinding<ActivityUserInfoBinding>(R.layout.activity_user_info)
    override val screenTitle = "내 정보"
    private val userInfoViewModel by viewModels<UserInfoViewModel>()

    private val userInfoEditActivityNew = registerForActivityResult(UserInfoEditContract()) { edited ->
        if(edited) userInfoViewModel.getUserInfo()
    }

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
                userInfoEditActivityNew.launch(Unit)
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
            if(user != null) {
                val userState = user.toUserState(this@UserInfoActivity)
                binding.userinfoTextviewId.text = userState.email
                binding.userinfoTextviewName.text = userState.username
                binding.userinfoTextviewNickName.text = userState.userNickname
                binding.userinfoTextviewAnonymousNickName.text = userState.userAnonymousNickname
                binding.userinfoTextviewPhoneNum.text = userState.phoneNumber
                binding.userinfoTextviewGender.text = userState.gender
                binding.userinfoTextviewStudentId.text = userState.studentNumber
                binding.userinfoTextviewMajor.text = userState.major
            } else {
                ToastUtil.getInstance().makeShort(getString(R.string.user_info_anonymous))
                finish()
            }
        }

        userInfoState.flowWithLifecycle(lifecycle, Lifecycle.State.STARTED).onEach {
            when (it.status) {
                UiStatus.Init -> Unit
                UiStatus.Loading -> Unit
                UiStatus.Success -> goToLoginActivity()
                is UiStatus.Failed -> ToastUtil.getInstance().makeShort(it.status.message)
            }
        }.launchIn(lifecycleScope)
    }
    private fun goToLoginActivity() {
        finishAffinity()
        startActivity(Intent(this@UserInfoActivity, LoginActivity::class.java))
    }

    override fun onDestroy() {
        super.onDestroy()
        SnackbarUtil.snackbar = null
    }
}
