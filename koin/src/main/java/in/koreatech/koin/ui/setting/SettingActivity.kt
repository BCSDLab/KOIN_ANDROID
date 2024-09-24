package `in`.koreatech.koin.ui.setting

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import androidx.activity.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import com.google.android.gms.oss.licenses.OssLicensesMenuActivity
import com.google.android.material.snackbar.Snackbar
import dagger.hilt.android.AndroidEntryPoint
import `in`.koreatech.koin.R
import `in`.koreatech.koin.constant.URL
import `in`.koreatech.koin.core.activity.ActivityBase
import `in`.koreatech.koin.core.appbar.AppBarBase
import `in`.koreatech.koin.core.toast.ToastUtil
import `in`.koreatech.koin.databinding.ActivitySettingBinding
import `in`.koreatech.koin.ui.changepassword.ChangePasswordContract
import `in`.koreatech.koin.ui.login.LoginActivity
import `in`.koreatech.koin.ui.notification.NotificationActivity
import `in`.koreatech.koin.ui.term.TermActivity
import `in`.koreatech.koin.ui.userinfo.UserInfoActivity
import `in`.koreatech.koin.util.SnackbarUtil
import kotlinx.coroutines.launch

@AndroidEntryPoint
class SettingActivity : ActivityBase() {
    override val screenTitle: String
        get() = "설정"

    private lateinit var binding: ActivitySettingBinding
    private val viewModel by viewModels<SettingViewModel>()

    private val loginSnackBar: Snackbar by lazy {
        Snackbar.make(binding.root, R.string.snack_bar_login_title, Snackbar.LENGTH_LONG).apply {
            setTextColor(getColor(R.color.neutral_0))
            setActionTextColor(getColor(R.color.sub_sub500))
            setBackgroundTint(getColor(R.color.primary_900))
            setAction(R.string.snack_bar_login_action_text) {
                startActivity(Intent(this@SettingActivity, LoginActivity::class.java))
                dismiss()
            }
        }
    }

    private val changePasswordResult =
        registerForActivityResult(ChangePasswordContract()) { isChanged ->
            if (isChanged) SnackbarUtil.makeShortSnackbar(binding.root, getString(R.string.change_password_change_complete))
        }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySettingBinding.inflate(layoutInflater)
        setContentView(binding.root)

        initView()
        initListeners()
        initObservers()
    }


    private fun initView() {
    }

    private fun initListeners() {
        with(binding) {
            appbarSetting.setOnClickListener {
                when (it.id) {
                    AppBarBase.getLeftButtonId() -> {
                        onBackPressedDispatcher.onBackPressed()
                    }
                }
            }

            svProfile.setOnSettingClickListener {
                if (viewModel.isStudent)
                    startActivity(Intent(this@SettingActivity, UserInfoActivity::class.java))
                else
                    loginSnackBar.show()
            }
            svChangePassword.setOnSettingClickListener {
                if (viewModel.isStudent) {
                    changePasswordResult.launch(Unit)
                } else
                    loginSnackBar.show()
            }
            svNotification.setOnSettingClickListener {
                startActivity(Intent(this@SettingActivity, NotificationActivity::class.java))
            }
            svPrivacyPolicy.setOnSettingClickListener {
                startActivity(Intent(this@SettingActivity, TermActivity::class.java).apply {
                    putExtra(TermActivity.KEY_TERM, TermActivity.TERM_PRIVACY_POLICY)
                })
            }
            svKoinTerms.setOnSettingClickListener {
                startActivity(Intent(this@SettingActivity, TermActivity::class.java).apply {
                    putExtra(TermActivity.KEY_TERM, TermActivity.TERM_KOIN)
                })
            }
            svOpenSourceLicense.setOnSettingClickListener {
                startActivity(Intent(this@SettingActivity, OssLicensesMenuActivity::class.java))
            }
            svContact.setOnSettingClickListener {
                startActivity(Intent(Intent.ACTION_VIEW, Uri.parse(URL.KOIN_ASK_FORM)))
            }
        }
    }

    private fun initObservers() {
        lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel.versionState.collect { v ->
                    when (v) {
                        is VersionState.Latest -> {
                            binding.tvClientVersion.text = v.currentVersion
                            binding.tvAppVersion.apply {
                                text = getString(R.string.setting_app_version_is_latest)
                                setTextColor(getColor(R.color.neutral_500))
                            }
                        }

                        is VersionState.Outdated -> {
                            binding.tvClientVersion.text = v.currentVersion
                            binding.tvAppVersion.apply {
                                text = getString(R.string.setting_app_version_display_latest, v.latestVersion)
                                setTextColor(getColor(R.color.primary_500))
                            }
                        }

                        is VersionState.Init -> {
                            binding.tvClientVersion.text = null
                            binding.tvAppVersion.text = null
                        }
                    }
                }
            }
        }

        lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel.versionError.collect {
                    ToastUtil.getInstance().makeShort(R.string.version_check_failed)
                }
            }
        }
    }

    override fun onDestroy() {
        loginSnackBar.dismiss()
        super.onDestroy()
    }
}