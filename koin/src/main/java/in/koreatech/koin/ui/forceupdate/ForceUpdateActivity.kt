package `in`.koreatech.koin.ui.forceupdate

import android.os.Bundle
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.core.view.updatePadding
import dagger.hilt.android.AndroidEntryPoint
import `in`.koreatech.koin.BuildConfig
import `in`.koreatech.koin.R
import `in`.koreatech.koin.core.activity.ActivityBase
import `in`.koreatech.koin.core.analytics.EventAction
import `in`.koreatech.koin.core.analytics.EventLogger
import `in`.koreatech.koin.core.analytics.ForceUpdateAnalyticsConstant
import `in`.koreatech.koin.core.util.SystemBarsUtils
import `in`.koreatech.koin.databinding.ActivityForceUpdateBinding
import `in`.koreatech.koin.ui.splash.SplashActivity
import `in`.koreatech.koin.util.ext.navigateToPlayStore

@AndroidEntryPoint
class ForceUpdateActivity : ActivityBase() {
    companion object {
        private const val screenTitle = "강제업데이트"
    }

    override val screenTitle: String
        get() = ForceUpdateActivity.screenTitle

    private lateinit var binding: ActivityForceUpdateBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityForceUpdateBinding.inflate(layoutInflater)
        setContentView(binding.root)
        EventLogger.logCustomEvent(
            action = ForceUpdateAnalyticsConstant.EVENT_NAME,
            category = ForceUpdateAnalyticsConstant.Category.EXPOSURE,
            label = ForceUpdateAnalyticsConstant.Label.EXPOSURE,
            value = "v${BuildConfig.VERSION_NAME}"
        )
        initView()
        initEvent()
    }

    override fun onUserLeaveHint() {
        super.onUserLeaveHint()
        EventLogger.logCustomEvent(
            action = ForceUpdateAnalyticsConstant.EVENT_NAME,
            category = ForceUpdateAnalyticsConstant.Category.EXIT,
            label = ForceUpdateAnalyticsConstant.Label.EXIT,
            value = ForceUpdateAnalyticsConstant.Value.EXIT
        )
    }

    private fun initView() {
        handleIntent()
        SystemBarsUtils(this).apply {
            setStatusBarColor(window, R.color.primary_600)
            setNavigationBarColor(window, R.color.primary_600)
        }

        ViewCompat.setOnApplyWindowInsetsListener(binding.root) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.updatePadding(
                left = v.left,
                right = v.right,
                top = v.top + systemBars.top,
                bottom = v.top + systemBars.bottom
            )
            WindowInsetsCompat.CONSUMED
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
                action = ForceUpdateAnalyticsConstant.EVENT_NAME,
                category = ForceUpdateAnalyticsConstant.Category.EXIT,
                label = ForceUpdateAnalyticsConstant.Label.EXIT,
                value = ForceUpdateAnalyticsConstant.Value.EXIT
            )
            finish()
        }
    }

    private fun setOnClickUpdateButton() {
        binding.btnUpdate.setOnClickListener {
            EventLogger.logClickEvent(
                action = EventAction.DA1,
                label = ForceUpdateAnalyticsConstant.Label.STORE_ENTRY,
                value = ForceUpdateAnalyticsConstant.Value.UPDATE
            )
            navigateToPlayStore()
        }
    }

    private fun setOnClickAlreadyUpdate() {
        binding.tvUpdate.setOnClickListener {
            EventLogger.logClickEvent(
                action = EventAction.DA1,
                label = ForceUpdateAnalyticsConstant.Label.ALREADY_DONE,
                value = ForceUpdateAnalyticsConstant.Value.ALREADY_DONE
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
