package `in`.koreatech.koin.ui.forceupdate

import android.os.Bundle
import dagger.hilt.android.AndroidEntryPoint
import `in`.koreatech.koin.R
import `in`.koreatech.koin.core.activity.ActivityBase
import `in`.koreatech.koin.core.util.SystemBarsUtils
import `in`.koreatech.koin.databinding.ActivityForceUpdateBinding
import `in`.koreatech.koin.ui.splash.SplashActivity
import `in`.koreatech.koin.util.ext.navigateToPlayStore

@AndroidEntryPoint
class ForceUpdateActivity: ActivityBase() {
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
        initView()
        initEvent()
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
            finish()
        }
    }

    private fun setOnClickUpdateButton() {
        binding.btnUpdate.setOnClickListener {
            navigateToPlayStore()
        }
    }

    private fun setOnClickAlreadyUpdate() {
        binding.tvUpdate.setOnClickListener {
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