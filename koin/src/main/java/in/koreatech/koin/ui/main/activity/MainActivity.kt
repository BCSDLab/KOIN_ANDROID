package `in`.koreatech.koin.ui.main.activity

import android.Manifest
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Build
import android.os.Bundle
import androidx.activity.result.contract.ActivityResultContracts
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.core.graphics.Insets
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.core.view.isVisible
import androidx.core.view.updatePadding
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.navigation.NavController
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.NavigationUI
import androidx.navigation.ui.setupWithNavController
import dagger.hilt.android.AndroidEntryPoint
import `in`.koreatech.koin.R
import `in`.koreatech.koin.core.analytics.AnalyticsConstant
import `in`.koreatech.koin.core.analytics.EventLogger
import `in`.koreatech.koin.core.designsystem.util.enableEdgeToEdgeWithLightStatusBar
import `in`.koreatech.koin.core.navigation.Navigator
import `in`.koreatech.koin.core.navigation.utils.EXTRA_ARTICLE_ID
import `in`.koreatech.koin.core.navigation.utils.EXTRA_BOARD_ID
import `in`.koreatech.koin.core.navigation.utils.EXTRA_CHAT_ROOM_ID
import `in`.koreatech.koin.core.navigation.utils.EXTRA_CLUB_ID
import `in`.koreatech.koin.core.navigation.utils.EXTRA_EVENT_ID
import `in`.koreatech.koin.core.navigation.utils.EXTRA_ID
import `in`.koreatech.koin.core.navigation.utils.EXTRA_TYPE
import `in`.koreatech.koin.databinding.ActivityNewMainBinding
import `in`.koreatech.koin.feature.banner.ui.BannerActivity
import `in`.koreatech.koin.navigation.SchemeType
import `in`.koreatech.koin.ui.main.viewmodel.MainActivityViewModel
import javax.inject.Inject
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

@AndroidEntryPoint
class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityNewMainBinding
    private lateinit var navController: NavController
    private val viewModel by viewModels<MainActivityViewModel>()

    @Inject
    lateinit var navigator: Navigator

    private fun navigationLogValue(itemId: Int): Pair<String, String>? = when (itemId) {
        R.id.bottom_navigation_home -> AnalyticsConstant.Label.NAV_HOME to "홈"
        R.id.bottom_navigation_category -> AnalyticsConstant.Label.NAV_CATEGORY to "카테고리"
        R.id.bottom_navigation_article -> AnalyticsConstant.Label.NAV_BULLETIN to "게시판"
        R.id.bottom_navigation_profile -> AnalyticsConstant.Label.NAV_PROFILE to "프로필"
        else -> null
    }

    private val requestMainPermissionLauncher =
        registerForActivityResult(
            ActivityResultContracts.RequestMultiplePermissions()
        ) { permission ->
            var permissionGranted = true
            permission.entries.forEach {
                if (it.key in MAIN_REQUIRED_PERMISSION && it.value == false) {
                    permissionGranted = false
                }
            }

            if (!permissionGranted) {
                // handle permission granted
            } else {
                // handle permission denied
            }
        }

    override fun onCreate(savedInstanceState: Bundle?) {
        enableEdgeToEdgeWithLightStatusBar()
        super.onCreate(savedInstanceState)
        binding = ActivityNewMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        EventLogger.logScreenName("HomeActivity") // DA requirement

        ViewCompat.setOnApplyWindowInsetsListener(binding.bottomNavigationMain) { view, insets ->
            val navigationBars = insets.getInsets(WindowInsetsCompat.Type.navigationBars())

            view.updatePadding(
                bottom = navigationBars.bottom
            )

            WindowInsetsCompat.Builder(insets)
                .setInsets(WindowInsetsCompat.Type.navigationBars(), Insets.NONE)
                .build()
        }

        val navHostFragment = supportFragmentManager.findFragmentById(binding.navHostFragmentMain.id) as NavHostFragment
        navController = navHostFragment.navController

        binding.bottomNavigationMain.setupWithNavController(navController)

        // setOnItemReselectedListener를 따로 등록하면 재탭 시 기본 popUpTo/restoreState 동작이 사라지므로 등록하지 않는다.
        // 리스너가 없으면 재탭도 이 setOnItemSelectedListener로 그대로 들어와 로깅과 기본 동작이 모두 보존된다.
        binding.bottomNavigationMain.setOnItemSelectedListener { item ->
            navigationLogValue(item.itemId)?.let { (label, value) ->
                EventLogger.logCampusClickEvent(label, value)
            }
            NavigationUI.onNavDestinationSelected(item, navController)
        }

        val topLevelDestinations = setOf(
            R.id.bottom_navigation_home,
            R.id.bottom_navigation_category,
            R.id.bottom_navigation_article,
            R.id.bottom_navigation_profile
        )

        navController.addOnDestinationChangedListener { _, destination, _ ->
            binding.bottomNavigationMain.isVisible = destination.id in topLevelDestinations
        }

        init()
        handleIntent()
    }

    private fun init() {
        if (!checkMainPermission()) {
            requestMainPermissionLauncher.launch(
                MAIN_REQUIRED_PERMISSION
            )
        }
        viewModel.checkKeywordNotiContent()
        viewModel.getStoreCategories()
        viewModel.updateDeviceToken()
    }

    private fun initBanner() {
        lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.CREATED) {
                viewModel.isBannerRefusal.collectLatest {
                    if (it == false) {
                        val intent = Intent(this@MainActivity, BannerActivity::class.java)
                        startActivity(intent)
                    }
                }
            }
        }
    }

    private fun handleIntent() {
        val targetId = intent.getIntExtra(EXTRA_ID, -1)
        val targetBoardId = intent.getIntExtra(EXTRA_BOARD_ID, -1)
        val targetArticleId = intent.getIntExtra(EXTRA_ARTICLE_ID, -1)
        val targetChatId = intent.getIntExtra(EXTRA_CHAT_ROOM_ID, -1)
        val targetClubId = intent.getIntExtra(EXTRA_CLUB_ID, -1)
        val targetEventId = intent.getIntExtra(EXTRA_EVENT_ID, -1)
        val type = intent.getStringExtra(EXTRA_TYPE) ?: ""

        when (type) {
            SchemeType.SHOP.type,
            SchemeType.DINING.type,
            SchemeType.ARTICLE.type,
            SchemeType.CHAT.type,
            SchemeType.CLUB_RECRUIT.type,
            SchemeType.CLUB.type -> {
                navigator.navigateTo(
                    context = this,
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
            }

            else -> {
                // Banner shouldn't popup on other page
                initBanner()
            }
        }
    }

    private fun checkMainPermission() = MAIN_REQUIRED_PERMISSION.all {
        ContextCompat.checkSelfPermission(
            this, it
        ) == PackageManager.PERMISSION_GRANTED
    }

    companion object {
        private val MAIN_REQUIRED_PERMISSION =
            mutableListOf<String>().apply {
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
                    add(Manifest.permission.POST_NOTIFICATIONS)
                }
            }.toTypedArray()
    }
}
