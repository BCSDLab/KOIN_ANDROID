package `in`.koreatech.koin.ui.splash

import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.activity.viewModels
import androidx.core.view.WindowInsetsCompat
import androidx.core.view.WindowInsetsControllerCompat
import androidx.lifecycle.lifecycleScope
import com.google.android.play.core.appupdate.AppUpdateManagerFactory
import com.google.android.play.core.install.model.UpdateAvailability
import com.kakao.sdk.common.KakaoSdk
import dagger.hilt.android.AndroidEntryPoint
import `in`.koreatech.koin.BuildConfig
import `in`.koreatech.koin.R
import `in`.koreatech.koin.contract.LoginContract
import `in`.koreatech.koin.core.activity.ActivityBase
import `in`.koreatech.koin.core.network.RetrofitManager
import `in`.koreatech.koin.core.toast.ToastUtil
import `in`.koreatech.koin.data.sharedpreference.UserInfoSharedPreferencesHelper
import `in`.koreatech.koin.domain.state.version.VersionUpdatePriority
import `in`.koreatech.koin.ui.main.activity.MainActivity
import `in`.koreatech.koin.ui.splash.viewmodel.SplashViewModel
import `in`.koreatech.koin.util.FirebasePerformanceUtil
import `in`.koreatech.koin.util.ext.observeLiveData
import kotlinx.coroutines.launch
import kotlinx.coroutines.yield

@AndroidEntryPoint
class SplashActivity : ActivityBase() {

    private val firebasePerformanceUtil by lazy {
        FirebasePerformanceUtil("koin_start")
    }
    override val screenTitle = "스플래시"

    private val splashViewModel by viewModels<SplashViewModel>()
    private val createdTime = System.currentTimeMillis()

    private val loginActivityLauncher = registerForActivityResult(LoginContract()) {}

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_start)
        KakaoSdk.init(this, resources.getString(R.string.kakao_app_key))
        WindowInsetsControllerCompat(window, window.decorView).let { controller ->
            controller.hide(WindowInsetsCompat.Type.systemBars())
            controller.systemBarsBehavior =
                WindowInsetsControllerCompat.BEHAVIOR_SHOW_TRANSIENT_BARS_BY_SWIPE
        }

        firebasePerformanceUtil.start()
        RetrofitManager.getInstance().init()
        UserInfoSharedPreferencesHelper.getInstance().init(applicationContext)
        checkInAppUpdate()
        initViewModel()
    }

    override fun onAttachedToWindow() {
        super.onAttachedToWindow()
        splashViewModel.checkUpdate()
    }

    private fun initViewModel() = with(splashViewModel) {
        observeLiveData(version) { (currentVersion, latestVersion, versionUpdatePriority) ->
            when (versionUpdatePriority) {
                VersionUpdatePriority.High, VersionUpdatePriority.Medium -> {
                    createVersionUpdateDialog(
                        currentVersion,
                        latestVersion,
                        versionUpdatePriority
                    )
                }

                else -> Unit
            }
        }

        observeLiveData(checkVersionError) {
            ToastUtil.getInstance().makeShort(R.string.version_check_failed)
        }

        observeLiveData(tokenState) {
            gotoMainActivityOrDelay()
        }
    }

    private fun createVersionUpdateDialog(
        currentVersion: String,
        latestVersion: String,
        versionUpdatePriority: VersionUpdatePriority
    ) {
        val dialog =
            VersionUpdateDialog(versionUpdatePriority, currentVersion, latestVersion)
        dialog.show(supportFragmentManager, "Dialog")

    }

    private fun checkInAppUpdate() {
        val appUpdateManager = AppUpdateManagerFactory.create(this)
        appUpdateManager.appUpdateInfo.addOnSuccessListener { appUpdateInfo ->
            // 업데이트가 필요한 상황이거나 최신 버전이라면, 버전 코드 업데이트 진행
            if(appUpdateInfo.updateAvailability() == UpdateAvailability.UPDATE_AVAILABLE
                || appUpdateInfo.updateAvailability() == UpdateAvailability.UPDATE_AVAILABLE) {
                splashViewModel.updateLatestVersion(appUpdateInfo.availableVersionCode())
            }
        }
        appUpdateManager.appUpdateInfo.addOnFailureListener { e ->
            Log.e("dhk", "Fail to get latest app: exception: ${e}")

            // 개발 환경에서는 항상 버전을 불러올 수 없으므로 작업 환경 버전으로 설정
            if(BuildConfig.DEBUG) {
                splashViewModel.updateLatestVersion(BuildConfig.VERSION_CODE)
            }
        }
    }

    private fun gotoMainActivityOrDelay() {
        lifecycleScope.launch {
            while (System.currentTimeMillis() - createdTime < 2000) yield()

            startActivity(Intent(this@SplashActivity, MainActivity::class.java))
            overridePendingTransition(R.anim.fade, R.anim.hold)
            finish()
            firebasePerformanceUtil.stop()
        }
    }

    private fun gotoLoginActivityOrDelay() {
        lifecycleScope.launch {
            while (System.currentTimeMillis() - createdTime < 2000) yield()

            loginActivityLauncher.launch(Unit)
            overridePendingTransition(R.anim.fade, R.anim.hold)
            finish()
            firebasePerformanceUtil.stop()
        }
    }
}