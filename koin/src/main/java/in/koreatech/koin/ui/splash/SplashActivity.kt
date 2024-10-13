package `in`.koreatech.koin.ui.splash

import android.content.Intent
import android.os.Bundle
import androidx.activity.viewModels
import androidx.core.os.bundleOf
import androidx.lifecycle.lifecycleScope
import dagger.hilt.android.AndroidEntryPoint
import android.util.Log
import com.google.android.play.core.appupdate.AppUpdateManagerFactory
import com.google.android.play.core.install.model.UpdateAvailability
import `in`.koreatech.koin.BuildConfig
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
        checkInAppUpdate()
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

    private fun checkInAppUpdate() {
        val appUpdateManager = AppUpdateManagerFactory.create(this)
        appUpdateManager.appUpdateInfo.addOnSuccessListener { appUpdateInfo ->
            when (appUpdateInfo.updateAvailability()) {
                UpdateAvailability.DEVELOPER_TRIGGERED_UPDATE_IN_PROGRESS,
                UpdateAvailability.UPDATE_AVAILABLE -> {
                    // 업데이트가 필요한 상황이거나 업데이트 중이라면 최신 버전값 저장
                    splashViewModel.updateLatestVersion(appUpdateInfo.availableVersionCode())
                }

                UpdateAvailability.UPDATE_NOT_AVAILABLE,
                UpdateAvailability.UNKNOWN -> {
                    // 업데이트 가능 유무를 모르거나, 업데이트가 불가능 한 경우 현재 버전 저장
                    splashViewModel.updateLatestVersion(BuildConfig.VERSION_CODE)
                }
            }
        }

        appUpdateManager.appUpdateInfo.addOnFailureListener { e ->
            // 업데이트 정보를 받아오는데 실패한 경우 현재 버전 저장
            Log.e("SplashActivity", "Fail to get latest app: exception: ${e}")
            splashViewModel.updateLatestVersion(BuildConfig.VERSION_CODE)
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