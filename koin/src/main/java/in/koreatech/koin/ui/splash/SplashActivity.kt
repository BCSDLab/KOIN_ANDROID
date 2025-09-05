package `in`.koreatech.koin.ui.splash

import android.app.Activity
import android.content.Intent
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.view.ViewGroup.MarginLayoutParams
import androidx.activity.viewModels
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.net.toUri
import androidx.core.os.bundleOf
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.core.view.updateLayoutParams
import androidx.lifecycle.lifecycleScope
import com.amar.library.BuildConfig
import com.google.android.play.core.appupdate.AppUpdateManagerFactory
import com.google.android.play.core.install.model.UpdateAvailability
import dagger.hilt.android.AndroidEntryPoint
import `in`.koreatech.koin.R
import `in`.koreatech.koin.core.activity.ActivityBase
import `in`.koreatech.koin.core.designsystem.util.enableEdgeToEdgeWithLightStatusBar
import `in`.koreatech.koin.core.navigation.Navigator
import `in`.koreatech.koin.core.navigation.NavigatorType
import `in`.koreatech.koin.core.navigation.utils.EXTRA_ARTICLE_ID
import `in`.koreatech.koin.core.navigation.utils.EXTRA_BOARD_ID
import `in`.koreatech.koin.core.navigation.utils.EXTRA_CHAT_ROOM_ID
import `in`.koreatech.koin.core.navigation.utils.EXTRA_CLUB_ID
import `in`.koreatech.koin.core.navigation.utils.EXTRA_EVENT_ID
import `in`.koreatech.koin.core.navigation.utils.EXTRA_ID
import `in`.koreatech.koin.core.navigation.utils.EXTRA_NAV_TYPE
import `in`.koreatech.koin.core.navigation.utils.EXTRA_TYPE
import `in`.koreatech.koin.core.onboarding.OnboardingManager
import `in`.koreatech.koin.core.toast.ToastUtil
import `in`.koreatech.koin.domain.state.version.VersionUpdatePriority
import `in`.koreatech.koin.ui.article.ArticleActivity
import `in`.koreatech.koin.ui.forceupdate.ForceUpdateActivity
import `in`.koreatech.koin.ui.main.activity.MainActivity
import `in`.koreatech.koin.ui.splash.state.TokenState
import `in`.koreatech.koin.ui.splash.viewmodel.SplashViewModel
import `in`.koreatech.koin.util.FirebasePerformanceUtil
import `in`.koreatech.koin.util.ext.observeLiveData
import javax.inject.Inject
import kotlinx.coroutines.launch
import kotlinx.coroutines.yield

@AndroidEntryPoint
class SplashActivity : ActivityBase() {
    companion object {
        private const val INFO_REQUIRED_URI = "koin://inforequired/activity"
        private const val EXTRA_IS_FULL = "extra_is_full"
        private const val screenTitle = "스플래시"
        private const val koinStart = "koin_start"
        const val version = "version"
        const val title = "title"
        const val content = "content"
    }

    @Inject
    lateinit var navigator: Navigator

    @Inject
    lateinit var onboardingManager: OnboardingManager

    override val screenTitle = SplashActivity.screenTitle

    private val firebasePerformanceUtil by lazy {
        FirebasePerformanceUtil(koinStart)
    }

    private val splashViewModel by viewModels<SplashViewModel>()
    private val createdTime = System.currentTimeMillis()

    override fun onCreate(savedInstanceState: Bundle?) {
        enableEdgeToEdgeWithLightStatusBar()
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_start)

        initView()
        initObserve()
        checkInAppUpdate()
    }

    private fun initView() {
        splashViewModel.checkUpdate()
        firebasePerformanceUtil.start()
        val constraintLayoutSplashRoot = findViewById<ConstraintLayout>(R.id.constraint_layout_splash_root)
        ViewCompat.setOnApplyWindowInsetsListener(constraintLayoutSplashRoot) { view, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())

            view.updateLayoutParams<MarginLayoutParams> {
                topMargin = systemBars.top
                bottomMargin = systemBars.bottom
            }

            WindowInsetsCompat.CONSUMED
        }
    }

    private fun initObserve() = with(splashViewModel) {
        observeLiveData(version) { version ->
            when (version.versionUpdatePriority) {
                VersionUpdatePriority.Importance ->
                    goToForceUpdateActivity(
                        version.title,
                        version.content
                    )

                VersionUpdatePriority.None -> Unit
            }
        }

        observeLiveData(checkVersionError) {
            ToastUtil.getInstance().makeShort(R.string.version_check_failed)
        }

        observeLiveData(tokenState) { state ->
            handleIntentOrLaunch(state)
        }
    }

    private fun goToForceUpdateActivity(title: String, content: String) {
        lifecycleScope.launch {
            delay()
            Intent(this@SplashActivity, ForceUpdateActivity::class.java).apply {
                putExtra(
                    version,
                    bundleOf(SplashActivity.title to title, SplashActivity.content to content)
                )
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
                UpdateAvailability.UPDATE_AVAILABLE
                -> {
                    // 업데이트가 필요한 상황이거나 업데이트 중이라면 최신 버전값 저장
                    splashViewModel.updateLatestVersion(appUpdateInfo.availableVersionCode())
                }

                UpdateAvailability.UPDATE_NOT_AVAILABLE,
                UpdateAvailability.UNKNOWN
                -> {
                    // 업데이트 가능 유무를 모르거나, 업데이트가 불가능 한 경우 현재 버전 저장
                    splashViewModel.updateLatestVersion(BuildConfig.VERSION_CODE)
                }
            }
        }

        appUpdateManager.appUpdateInfo.addOnFailureListener { e ->
            // 업데이트 정보를 받아오는데 실패한 경우 현재 버전 저장
            Log.e("SplashActivity", "Fail to get latest app: exception: $e")
            splashViewModel.updateLatestVersion(BuildConfig.VERSION_CODE)
        }
    }

    private fun handleIntentOrLaunch(state: TokenState) {
        val uriPrefix = intent.data?.path?.split("/")?.getOrNull(1)
        when (uriPrefix) {
            "articles" -> {
                gotoArticleActivityOrDelay(state)
            }
            else -> {
                gotoMainActivityOrDelay(state)
            }
        }
    }

    private fun gotoInfoRequiredActivity() {
        lifecycleScope.launch {
            with(onboardingManager) {
                showModalIfNeeded(
                    actionTrue = { launchInfoRequiredActivity(true) }
                )
            }
        }
    }

    private fun launchInfoRequiredActivity(check: Boolean) {
        val intent = Intent(Intent.ACTION_VIEW, INFO_REQUIRED_URI.toUri())
            .putExtra(EXTRA_IS_FULL, check)
            .addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
        this@SplashActivity.startActivity(intent)
        finishWithTransition()
    }

    private fun finishWithTransition() {
        if (Build.VERSION.SDK_INT >= 34) {
            overrideActivityTransition(Activity.OVERRIDE_TRANSITION_OPEN, 0, 0)
        } else {
            overridePendingTransition(0, 0)
        }
    }

    private fun gotoArticleActivityOrDelay(state: TokenState) {
        val path = intent.data?.path?.split("/")?.getOrNull(2)
        lifecycleScope.launch {
            delay()
            val intent =
                when (path) {
                    "lost-item" -> {
                        Intent(Intent.ACTION_VIEW).apply {
                            data = Uri.parse("koin://article/activity?fragment=article_lost_and_found")
                        }
                    }
                    else -> {
                        Intent(this@SplashActivity, ArticleActivity::class.java)
                    }
                }
            startActivity(intent)
            overridePendingTransition(R.anim.fade, R.anim.hold)
            if (state == TokenState.Valid) {
                gotoInfoRequiredActivity()
            }
            finish()
            firebasePerformanceUtil.stop()
        }
    }

    private fun gotoMainActivityOrDelay(state: TokenState) {
        val targetId = intent.getIntExtra(EXTRA_ID, -1)
        val targetBoardId = intent.getIntExtra(EXTRA_BOARD_ID, -1)
        val targetArticleId = intent.getIntExtra(EXTRA_ARTICLE_ID, -1)
        val targetChatId = intent.getIntExtra(EXTRA_CHAT_ROOM_ID, -1)
        val targetClubId = intent.getIntExtra(EXTRA_CLUB_ID, -1)
        val targetEventId = intent.getIntExtra(EXTRA_EVENT_ID, -1)
        val type = intent.getStringExtra(EXTRA_TYPE) ?: ""
        val navType = intent.getStringExtra(EXTRA_NAV_TYPE) ?: ""

        lifecycleScope.launch {
            delay()
            val intent = if (navType == NavigatorType.MAIN.type) {
                navigator.navigateTo(
                    context = this@SplashActivity,
                    type = Pair(EXTRA_TYPE, type),
                    *arrayOf(
                        Pair(EXTRA_ID, targetId),
                        Pair(EXTRA_BOARD_ID, targetBoardId),
                        Pair(EXTRA_ARTICLE_ID, targetArticleId),
                        Pair(EXTRA_CHAT_ROOM_ID, targetChatId),
                        Pair(EXTRA_CLUB_ID, targetClubId),
                        Pair(EXTRA_EVENT_ID, targetEventId)
                    )
                )
            } else {
                Intent(this@SplashActivity, MainActivity::class.java)
            }

            startActivity(intent)
            overridePendingTransition(R.anim.fade, R.anim.hold)
            if (state == TokenState.Valid) {
                gotoInfoRequiredActivity()
            }
            finish()
            firebasePerformanceUtil.stop()
        }
    }

    private suspend fun delay() {
        while (System.currentTimeMillis() - createdTime < 2000) yield()
    }
}
