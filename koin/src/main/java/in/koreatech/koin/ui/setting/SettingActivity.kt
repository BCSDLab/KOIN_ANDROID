package `in`.koreatech.koin.ui.setting

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import androidx.activity.viewModels
import androidx.appcompat.app.AlertDialog
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import dagger.hilt.android.AndroidEntryPoint
import `in`.koreatech.koin.R
import `in`.koreatech.koin.constant.URL
import `in`.koreatech.koin.core.activity.ActivityBase
import `in`.koreatech.koin.core.toast.ToastUtil
import `in`.koreatech.koin.databinding.ActivitySettingBinding
import `in`.koreatech.koin.ui.notification.NotificationActivity
import `in`.koreatech.koin.ui.userinfo.UserInfoActivity
import kotlinx.coroutines.launch

@AndroidEntryPoint
class SettingActivity : ActivityBase() {
    override val screenTitle: String
        get() = "설정"

    private lateinit var binding: ActivitySettingBinding
    private val viewModel by viewModels<SettingViewModel>()

    private val loginAlertDialog: AlertDialog by lazy {
        AlertDialog.Builder(this)
            .setTitle(getString(R.string.user_only))
            .setMessage(getString(R.string.login_request))
            .setCancelable(false)
            .setPositiveButton(getString(R.string.navigation_ok)) { dialog, _ ->
                dialog.dismiss()
            }
            .setNegativeButton(getString(R.string.navigation_cancel)) { dialog, _ ->
                dialog.cancel()
            }.create()
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
            svProfile.setOnSettingClickListener {
                if (viewModel.isStudent)
                    startActivity(Intent(this@SettingActivity, UserInfoActivity::class.java))
                else
                    loginAlertDialog.show()
            }
            svChangePassword.setOnSettingClickListener {
                if (viewModel.isStudent)
                    //
                else
                    loginAlertDialog.show()
            }
            svNotification.setOnSettingClickListener {
                startActivity(Intent(this@SettingActivity, NotificationActivity::class.java))
            }
            svPrivacyPolicy.setOnSettingClickListener {
                //
            }
            svKoinTerms.setOnSettingClickListener {
                //
            }
            svOpenSourceLicense.setOnSettingClickListener {
                //
            }
            svContact.setOnSettingClickListener {
                startActivity(Intent(Intent.ACTION_VIEW, Uri.parse(URL.BCSD_ASK_FORM)))
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
}