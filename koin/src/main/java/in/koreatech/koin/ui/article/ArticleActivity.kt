package `in`.koreatech.koin.ui.article

import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.navigation.NavController
import androidx.navigation.findNavController
import androidx.navigation.fragment.NavHostFragment
import dagger.hilt.android.AndroidEntryPoint
import `in`.koreatech.koin.R
import `in`.koreatech.koin.core.activity.ActivityBase
import `in`.koreatech.koin.core.util.dataBinding
import `in`.koreatech.koin.databinding.ActivityArticleBinding

@AndroidEntryPoint
class ArticleActivity : ActivityBase() {

    private val binding by dataBinding<ActivityArticleBinding>()
    private lateinit var navController: NavController
    override val screenTitle: String = "공지사항"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_article)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        val navHostFragment = supportFragmentManager.findFragmentById(R.id.nav_host_article_fragment) as NavHostFragment
        navController = navHostFragment.navController

        navController.addOnDestinationChangedListener { _, dest, _ ->
            when (dest.id) {
                R.id.articleListFragment -> setToolbar(ArticleToolbarState.ARTICLE_LIST)
                R.id.articleDetailFragment -> setToolbar(ArticleToolbarState.ARTICLE_DETAIL)
                R.id.articleSearchFragment -> setToolbar(ArticleToolbarState.ARTICLE_SEARCH)
                R.id.articleKeywordFragment -> setToolbar(ArticleToolbarState.ARTICLE_KEYWORD)
            }
        }
    }

    private fun setToolbar(state: ArticleToolbarState) {
        binding.toolbarArticleList.apply {
            title = getString(state.title)
            menu.clear()
            state.menuRes?.let { inflateMenu(it) }
            setOnMenuItemClickListener { item ->
                when (item.itemId) {
                    R.id.action_search_article -> {
                        navController.navigate(R.id.action_articleListFragment_to_articleSearchFragment)
                        true
                    }
                    else -> false
                }
            }
        }
    }
}
