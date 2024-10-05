package `in`.koreatech.koin.ui.splash

import android.app.Activity
import android.content.Intent
import android.os.Build
import android.os.Bundle
import androidx.activity.viewModels
import androidx.core.os.bundleOf
import androidx.lifecycle.lifecycleScope
import dagger.hilt.android.AndroidEntryPoint
import `in`.koreatech.koin.R
import `in`.koreatech.koin.core.activity.ActivityBase
import `in`.koreatech.koin.core.toast.ToastUtil
import `in`.koreatech.koin.core.util.SystemBarsUtils
import `in`.koreatech.koin.domain.state.version.VersionUpdatePriority
import `in`.koreatech.koin.ui.forceupdate.ForceUpdateActivity
import `in`.koreatech.koin.ui.main.activity.MainActivity
import `in`.koreatech.koin.ui.splash.viewmodel.SplashViewModel
import `in`.koreatech.koin.util.FirebasePerformanceUtil
import `in`.koreatech.koin.util.ext.observeLiveData
import kotlinx.coroutines.launch
import kotlinx.coroutines.yield

@AndroidEntryPoint
class SplashActivity : ActivityBase() {
    companion object {
        private const val screenTitle = "스플래시"
        private const val koinStart = "koin_start"
        const val version = "version"
        const val title = "title"
        const val content = "content"
    }

    override val screenTitle = SplashActivity.screenTitle

    private val firebasePerformanceUtil by lazy {
        FirebasePerformanceUtil(koinStart)
    }

    private val splashViewModel by viewModels<SplashViewModel>()
    private val createdTime = System.currentTimeMillis()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_start)

        initView()
        initObserve()
    }

    private fun initView() {
        splashViewModel.checkUpdate()
        firebasePerformanceUtil.start()
        SystemBarsUtils(this).apply {
            setImmersiveMode(window)
        }
    }

    private fun initObserve() = with(splashViewModel) {
        observeLiveData(version) { version ->
            when (version.versionUpdatePriority) {
                VersionUpdatePriority.Importance -> goToForceUpdateActivity(version.title, version.content)
                VersionUpdatePriority.None -> Unit
            }
        }

        observeLiveData(checkVersionError) {
            ToastUtil.getInstance().makeShort(R.string.version_check_failed)
        }

        observeLiveData(tokenState) {
            gotoMainActivityOrDelay()
        }
    }

    private fun goToForceUpdateActivity(title: String, content: String) {
        lifecycleScope.launch {
            delay()
            Intent(this@SplashActivity, ForceUpdateActivity::class.java).apply {
                putExtra(version, bundleOf(SplashActivity.title to title, SplashActivity.content to content))
            }.let { intent ->
                startActivity(intent)
                overridePendingTransition(R.anim.slide_in, R.anim.hold)
                finish()
            }
        }
    }

    private fun gotoMainActivityOrDelay() {
        lifecycleScope.launch {
            delay()
            startActivity(Intent(this@SplashActivity, MainActivity::class.java))
            overridePendingTransition(R.anim.fade, R.anim.hold)
            finish()
            firebasePerformanceUtil.stop()
        }
    }

    private suspend fun delay() {
        while (System.currentTimeMillis() - createdTime < 2000) yield()
    }
}