package `in`.koreatech.koin.ui.splash

import `in`.koreatech.koin.R
import `in`.koreatech.koin.contract.LoginContract
import `in`.koreatech.koin.core.activity.ActivityBase
import `in`.koreatech.koin.core.network.RetrofitManager
import `in`.koreatech.koin.core.toast.ToastUtil
import `in`.koreatech.koin.data.sharedpreference.UserInfoSharedPreferencesHelper
import `in`.koreatech.koin.domain.state.version.VersionUpdatePriority
import `in`.koreatech.koin.ui.main.MainActivity
import `in`.koreatech.koin.ui.splash.viewmodel.SplashViewModel
import `in`.koreatech.koin.util.FirebasePerformanceUtil
import `in`.koreatech.koin.util.ext.observeLiveData
import android.content.ActivityNotFoundException
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import androidx.activity.viewModels
import androidx.core.content.ContextCompat
import androidx.core.view.WindowInsetsCompat
import androidx.core.view.WindowInsetsControllerCompat
import androidx.lifecycle.lifecycleScope
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch
import kotlinx.coroutines.yield

@AndroidEntryPoint
class SplashActivity : ActivityBase() {

    private val firebasePerformanceUtil by lazy {
        FirebasePerformanceUtil("koin_start")
    }

    private val splashViewModel by viewModels<SplashViewModel>()
    private val createdTime = System.currentTimeMillis()

    private val loginActivityLauncher = registerForActivityResult(LoginContract()) {}

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_start)
        WindowInsetsControllerCompat(window, window.decorView).let { controller ->
            controller.hide(WindowInsetsCompat.Type.systemBars())
            controller.systemBarsBehavior =
                WindowInsetsControllerCompat.BEHAVIOR_SHOW_TRANSIENT_BARS_BY_SWIPE
        }

        firebasePerformanceUtil.start()
        RetrofitManager.getInstance().init()
        UserInfoSharedPreferencesHelper.getInstance().init(applicationContext)

        initViewModel()
        splashViewModel.checkUpdate()
    }

    private fun initViewModel() = with(splashViewModel) {
        observeLiveData(updateCheckResult) {
            try {
                if (it != null) {
                    if (it.isSuccess) {
                        val (currentVersion, latestVersion, versionUpdatePriority) = it.getOrThrow()
                        when (versionUpdatePriority) {
                            VersionUpdatePriority.High, VersionUpdatePriority.Medium -> {
                                createVersionUpdateDialog(
                                    currentVersion,
                                    latestVersion,
                                    versionUpdatePriority
                                )
                            }
                        }
                    } else {
                        ToastUtil.getInstance().makeShort(R.string.version_check_failed)
                    }
                }
            } catch (t: Throwable) {
                ToastUtil.getInstance().makeShort(R.string.version_check_failed)
            }
        }

        observeLiveData(tokenIsSaved) {
            if (it == false)
                gotoLoginActivityOrDelay()
        }

        observeLiveData(tokenIsValid) {
            if (it != null) {
                if (it.isSuccess) {
                    gotoMainActivityOrDelay()
                } else {
                    gotoLoginActivityOrDelay()
                }
            }
        }
    }

    private fun createVersionUpdateDialog(
        currentVersion: String,
        latestVersion: String,
        versionUpdatePriority: VersionUpdatePriority
    ) {
        val dialog =
            VersionUpdateDialog(versionUpdatePriority, currentVersion, latestVersion)
        dialog.setDialogOptionClickListener(
            onLaterButtonClicked = {
                splashViewModel.checkToken()
            },
            onUpdateButtonClicked = {
                gotoPlayStore()
                finish()
            }
        )
        dialog.show(supportFragmentManager, "Dialog")

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

    private fun gotoPlayStore() {
        val appPackageName: String = packageName
        try {
            val appStoreIntent =
                Intent(Intent.ACTION_VIEW, Uri.parse("market://details?id=$appPackageName"))
            appStoreIntent.setPackage("com.android.vending")
            ContextCompat.startActivity(this, appStoreIntent, null)
        } catch (exception: ActivityNotFoundException) {
            ContextCompat.startActivity(
                this,
                Intent(
                    Intent.ACTION_VIEW,
                    Uri.parse("https://play.google.com/store/apps/details?id=$appPackageName")
                ),
                null
            )
        }
    }
}