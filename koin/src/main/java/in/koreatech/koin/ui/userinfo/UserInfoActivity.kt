package `in`.koreatech.koin.ui.userinfo

import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.activity.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.flowWithLifecycle
import androidx.lifecycle.lifecycleScope
import dagger.hilt.android.AndroidEntryPoint
import `in`.koreatech.koin.R
import `in`.koreatech.koin.common.UiStatus
import `in`.koreatech.koin.core.activity.ActivityBase
import `in`.koreatech.koin.core.toast.ToastUtil
import `in`.koreatech.koin.core.util.dataBinding
import `in`.koreatech.koin.core.util.setAppBarButtonClickedListener
import `in`.koreatech.koin.databinding.ActivityUserInfoBinding
import `in`.koreatech.koin.ui.login.LoginActivity
import `in`.koreatech.koin.ui.userinfo.contract.UserInfoEditContract
import `in`.koreatech.koin.ui.userinfo.state.toUserState
import `in`.koreatech.koin.ui.userinfo.viewmodel.UserInfoViewModel
import `in`.koreatech.koin.util.SnackbarUtil
import `in`.koreatech.koin.util.ext.observeLiveData
import `in`.koreatech.koin.util.ext.withLoading
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach

@AndroidEntryPoint
class UserInfoActivity : ActivityBase() {
    private val binding by dataBinding<ActivityUserInfoBinding>(R.layout.activity_user_info)
    override val screenTitle = "내 정보"
    private val userInfoViewModel by viewModels<UserInfoViewModel>()

    private val userInfoEditActivityNew = registerForActivityResult(UserInfoEditContract()) { edited ->
        if (edited) userInfoViewModel.getUserInfo()
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

        initView()
        initViewModel()

        userInfoViewModel.getUserInfo()
    }

    private fun initView() = with(binding) {
        appbarUserInfo.setAppBarButtonClickedListener(
            leftButtonClicked = {
                onBackPressed()
            },
            rightButtonClicked = {
                userInfoEditActivityNew.launch(Unit)
            }
        )

        btnLeave.setOnClickListener {
            UserLeaveDialog().show(supportFragmentManager, "dialog")
        }
    }

    private fun initViewModel() = with(userInfoViewModel) {
        withLoading(this@UserInfoActivity, this)

        observeLiveData(user) { user ->
            if (user != null && user.isStudent) {
                val userState = user.toUserState(this@UserInfoActivity)
                with(binding) {
                    svId.labelText = userState.email
                    svName.labelText = userState.username
                    svNickname.labelText = userState.userNickname
                    svPhoneNumber.labelText = userState.phoneNumber
                    svStudentNumber.labelText = userState.studentNumber
                    svMajor.labelText = userState.major
                    svGender.labelText = userState.gender
                }
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
