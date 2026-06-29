package `in`.koreatech.koin.ui.newmain

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.core.graphics.Insets
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.core.view.isVisible
import androidx.core.view.updatePadding
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.setupWithNavController
import dagger.hilt.android.AndroidEntryPoint
import `in`.koreatech.koin.R
import `in`.koreatech.koin.core.designsystem.util.enableEdgeToEdgeWithLightStatusBar
import `in`.koreatech.koin.databinding.ActivityNewMainBinding

@AndroidEntryPoint
class NewMainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityNewMainBinding
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
