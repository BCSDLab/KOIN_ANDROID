package `in`.koreatech.koin.ui.splash

import `in`.koreatech.koin.R
import `in`.koreatech.koin.core.activity.ActivityBase
import `in`.koreatech.koin.core.network.RetrofitManager
import `in`.koreatech.koin.core.toast.ToastUtil
import `in`.koreatech.koin.data.sharedpreference.UserInfoSharedPreferencesHelper
import `in`.koreatech.koin.domain.model.version.VersionUpdatePriority
import `in`.koreatech.koin.ui.login.contract.LoginContract
import `in`.koreatech.koin.ui.main.MainActivity
import `in`.koreatech.koin.ui.splash.viewmodel.SplashViewModel
import `in`.koreatech.koin.util.FirebasePerformanceUtil
import `in`.koreatech.koin.util.ext.observeLiveData
import android.content.Intent
import android.graphics.Point
import android.os.Bundle
import android.view.Gravity
import androidx.activity.viewModels
import androidx.core.view.WindowInsetsCompat
import androidx.core.view.WindowInsetsControllerCompat
import androidx.lifecycle.lifecycleScope
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch
import kotlinx.coroutines.yield

@AndroidEntryPoint
class SplashActivity : ActivityBase(), VersionDialogClickListener {

    private lateinit var firebasePerformanceUtil: FirebasePerformanceUtil

    private val splashViewModel by viewModels<SplashViewModel>()
    private val createdTime = System.currentTimeMillis()

    private val loginActivityLauncher = registerForActivityResult(LoginContract()) {}

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_start)
        WindowInsetsControllerCompat(window, window.decorView).let { controller ->
            controller.hide(WindowInsetsCompat.Type.systemBars())
            controller.systemBarsBehavior = WindowInsetsControllerCompat.BEHAVIOR_SHOW_TRANSIENT_BARS_BY_SWIPE
        }
        init()
        initViewModel()
        splashViewModel.checkUpdate()
    }

    private fun initViewModel() = with(splashViewModel) {
        observeLiveData(updateCheckResult) {
            try {
                if(it != null) {
                    if(it.isSuccess) {
                        val (currentVersion, latestVersion, versionUpdatePriority) = it.getOrThrow()
                        when(versionUpdatePriority) {
                            VersionUpdatePriority.High -> createVersionUpdateDialog(currentVersion, latestVersion, 0)
                            VersionUpdatePriority.Low -> createVersionUpdateDialog(currentVersion, latestVersion, 1)
                            VersionUpdatePriority.Medium -> checkToken()
                            VersionUpdatePriority.None -> checkToken()
                        }
                    } else {
                        ToastUtil.getInstance().makeShort(R.string.version_check_failed)
                        checkToken()
                    }
                }
            } catch (t: Throwable) {
                ToastUtil.getInstance().makeShort(R.string.version_check_failed)
                checkToken()
            }
        }

        observeLiveData(tokenIsSaved) {
            if(it == false)
                gotoLoginActivityOrDelay()
        }

        observeLiveData(tokenIsValid) {
            if(it != null) {
                if(it.isSuccess) {
                    gotoMainActivityOrDelay()
                } else {
                    gotoLoginActivityOrDelay()
                }
            }
        }
    }

    fun init() {
        this.firebasePerformanceUtil = FirebasePerformanceUtil("koin_start")
        this.firebasePerformanceUtil.start()
        RetrofitManager.getInstance().init()
        UserInfoSharedPreferencesHelper.getInstance().init(applicationContext)
    }

    private fun createVersionUpdateDialog(currentVersion: String, latestVersion: String, type: Int) {
        val dialog = VersionUpdateDialog(this, type, currentVersion, latestVersion, this)
        dialog.window!!.setGravity(Gravity.CENTER)
        dialog.show()
        val display = windowManager.defaultDisplay
        val size = Point()
        display.getSize(size)
        val window = dialog.window
        val x = (size.x * 0.8f).toInt()
        val y = (size.y * 0.4f).toInt()
        window!!.setLayout(x, y)
    }

    fun gotoMainActivityOrDelay() {
        lifecycleScope.launch {
            while(System.currentTimeMillis() - createdTime < 2000) yield()

            startActivity(Intent(this@SplashActivity, MainActivity::class.java))
            overridePendingTransition(R.anim.fade, R.anim.hold)
            finish()
            firebasePerformanceUtil.stop()
        }
    }

    fun gotoLoginActivityOrDelay() {
        lifecycleScope.launch {
            while(System.currentTimeMillis() - createdTime < 2000) yield()

            loginActivityLauncher.launch(Unit)
            overridePendingTransition(R.anim.fade, R.anim.hold)
            finish()
            firebasePerformanceUtil.stop()
        }
    }

    override fun onLaterClick() {
        splashViewModel.checkToken()
    }

    override fun onUpdateClick() {
        finish()
    }
}