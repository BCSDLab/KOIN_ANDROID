package `in`.koreatech.koin.ui.forceupdate

import android.os.Bundle
import dagger.hilt.android.AndroidEntryPoint
import `in`.koreatech.koin.BuildConfig
import `in`.koreatech.koin.R
import `in`.koreatech.koin.core.activity.ActivityBase
import `in`.koreatech.koin.core.analytics.EventLogger
import `in`.koreatech.koin.core.util.SystemBarsUtils
import `in`.koreatech.koin.databinding.ActivityForceUpdateBinding
import `in`.koreatech.koin.ui.splash.SplashActivity
import `in`.koreatech.koin.util.ext.navigateToPlayStore

@AndroidEntryPoint
class ForceUpdateActivity: ActivityBase() {
    companion object {
        private const val screenTitle = "강제업데이트"
        private const val action = "force_update"
        private const val viewCategory = "page_view"
        private const val exitCategory = "page_exit"
        private const val updateCategory = "update"
        private const val clickCategory = "click"
    }
    override val screenTitle: String
        get() = ForceUpdateActivity.screenTitle

    private lateinit var binding: ActivityForceUpdateBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityForceUpdateBinding.inflate(layoutInflater)
        setContentView(binding.root)
        EventLogger.logCustomEvent(
            action = action,
            category = viewCategory,
            label = "forced_update_page_view",
            value = "v${BuildConfig.VERSION_NAME}"
        )
        initView()
        initEvent()
    }

    override fun onUserLeaveHint() {
        super.onUserLeaveHint()
        EventLogger.logCustomEvent(
            action = action,
            category = exitCategory,
            label = "forced_update_exit",
            value = "홈버튼"
        )
    }

    private fun initView() {
        handleIntent()
        SystemBarsUtils(this).apply {
            setStatusBarColor(window, R.color.primary_600)
            setNavigationBarColor(window, R.color.primary_600)
        }
    }

    private fun initEvent() {
        setOnClickExit()
        setOnClickUpdateButton()
        setOnClickAlreadyUpdate()
    }

    private fun setOnClickExit() {
        binding.ivExit.setOnClickListener {
            EventLogger.logCustomEvent(
                action = action,
                category = exitCategory,
                label = "forced_update_exit",
                value = "나가기버튼"
            )
            finish()
        }
    }

    private fun setOnClickUpdateButton() {
        binding.btnUpdate.setOnClickListener {
            EventLogger.logCustomEvent(
                action = action,
                category = updateCategory,
                label = "forced_update_confirm",
                value = "업데이트하기"
            )
            navigateToPlayStore()
        }
    }

    private fun setOnClickAlreadyUpdate() {
        binding.tvUpdate.setOnClickListener {
            EventLogger.logCustomEvent(
                action = action,
                category = clickCategory,
                label = "forced_update_already_done",
                value = "이미업데이트"
            )
            ForceUpdateDialog.apply {
                newInstance().show(supportFragmentManager, TAG)
            }
        }
    }

    private fun handleIntent() {
        intent.getBundleExtra(SplashActivity.version).let { version ->
            binding.tvTitle.text = version?.getString(SplashActivity.title)
            binding.tvContent.text = version?.getString(SplashActivity.content)
        }
    }
}