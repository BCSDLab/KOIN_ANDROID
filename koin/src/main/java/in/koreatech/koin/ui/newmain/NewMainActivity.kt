package `in`.koreatech.koin.ui.newmain

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.core.view.updatePadding
import dagger.hilt.android.AndroidEntryPoint
import `in`.koreatech.koin.R
import `in`.koreatech.koin.core.designsystem.util.enableEdgeToEdgeWithLightStatusBar
import `in`.koreatech.koin.databinding.ActivityNewMainBinding
import `in`.koreatech.koin.ui.newmain.article.ArticleFragment
import `in`.koreatech.koin.ui.newmain.category.CategoryFragment
import `in`.koreatech.koin.ui.newmain.home.HomeFragment
import `in`.koreatech.koin.ui.newmain.profile.ProfileFragment

@AndroidEntryPoint
class NewMainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityNewMainBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        enableEdgeToEdgeWithLightStatusBar()
        super.onCreate(savedInstanceState)
        binding = ActivityNewMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        ViewCompat.setOnApplyWindowInsetsListener(binding.root) { view, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())

            view.updatePadding(
                left = systemBars.left,
                right = systemBars.right,
                top = systemBars.top,
                bottom = systemBars.bottom
            )

            WindowInsetsCompat.CONSUMED
        }

        supportFragmentManager.beginTransaction().replace(binding.frameLayoutMainContainer.id, HomeFragment()).commit()

        binding.bottomNavigationMain.setOnItemSelectedListener { item ->
            when (item.itemId) {
                R.id.bottom_navigation_home -> {
                    supportFragmentManager.beginTransaction().replace(binding.frameLayoutMainContainer.id, HomeFragment()).commit()
                    true
                }

                R.id.bottom_navigation_category -> {
                    supportFragmentManager.beginTransaction().replace(binding.frameLayoutMainContainer.id, CategoryFragment()).commit()
                    true
                }

                R.id.bottom_navigation_article -> {
                    supportFragmentManager.beginTransaction().replace(binding.frameLayoutMainContainer.id, ArticleFragment()).commit()
                    true
                }

                R.id.bottom_navigation_profile -> {
                    supportFragmentManager.beginTransaction().replace(binding.frameLayoutMainContainer.id, ProfileFragment()).commit()
                    true
                }

                else -> {
                    false
                }
            }
        }
    }
}
