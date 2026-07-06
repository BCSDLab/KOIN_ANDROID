package `in`.koreatech.koin.ui.newmain

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.core.graphics.Insets
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.core.view.isVisible
import androidx.core.view.updatePadding
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.NavigationUI
import androidx.navigation.ui.setupWithNavController
import dagger.hilt.android.AndroidEntryPoint
import `in`.koreatech.koin.R
import `in`.koreatech.koin.core.analytics.AnalyticsConstant
import `in`.koreatech.koin.core.analytics.EventLogger
import `in`.koreatech.koin.core.designsystem.util.enableEdgeToEdgeWithLightStatusBar
import `in`.koreatech.koin.databinding.ActivityNewMainBinding

@AndroidEntryPoint
class NewMainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityNewMainBinding

    private fun navigationLogValue(itemId: Int): Pair<String, String>? = when (itemId) {
        R.id.bottom_navigation_home -> AnalyticsConstant.Label.NAV_HOME to "홈"
        R.id.bottom_navigation_category -> AnalyticsConstant.Label.NAV_CATEGORY to "카테고리"
        R.id.bottom_navigation_article -> AnalyticsConstant.Label.NAV_BULLETIN to "게시판"
        R.id.bottom_navigation_profile -> AnalyticsConstant.Label.NAV_PROFILE to "프로필"
        else -> null
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        enableEdgeToEdgeWithLightStatusBar()
        super.onCreate(savedInstanceState)
        binding = ActivityNewMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

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
        val navController = navHostFragment.navController

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
    }
}
