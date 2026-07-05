package `in`.koreatech.koin.ui.newmain.fragment

import android.graphics.Color
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.ViewCompat
import androidx.core.view.doOnAttach
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.NavHostFragment
import dagger.hilt.android.AndroidEntryPoint
import `in`.koreatech.koin.R
import `in`.koreatech.koin.core.analytics.AnalyticsConstant
import `in`.koreatech.koin.core.analytics.EventAction
import `in`.koreatech.koin.core.analytics.EventLogger
import `in`.koreatech.koin.core.appbar.ToolbarMenu
import `in`.koreatech.koin.databinding.FragmentArticleHostBinding
import `in`.koreatech.koin.feature.article.R as ArticleR
import `in`.koreatech.koin.feature.article.model.ArticleToolbarState

@AndroidEntryPoint
class ArticleFragment : Fragment() {

    private var _binding: FragmentArticleHostBinding? = null
    private val binding get() = _binding!!

    override fun onResume() {
        super.onResume()
        requireActivity().findViewById<View>(R.id.nav_host_fragment_main)
            ?.setBackgroundColor(Color.WHITE)
    }

    override fun onPause() {
        super.onPause()
        requireActivity().findViewById<View>(R.id.nav_host_fragment_main)
            ?.background = null
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentArticleHostBinding.inflate(inflater, container, false)
        binding.root.doOnAttach { ViewCompat.requestApplyInsets(it) }
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val navHostFragment =
            childFragmentManager.findFragmentById(R.id.nav_host_article_fragment) as NavHostFragment
        val navController = navHostFragment.navController

        navController.addOnDestinationChangedListener { _, dest, _ ->
            when (dest.id) {
                ArticleR.id.articleListFragment -> setToolbar(ArticleToolbarState.ARTICLE_LIST, navController)
                ArticleR.id.articleDetailFragment -> setToolbar(ArticleToolbarState.ARTICLE_DETAIL, navController)
                ArticleR.id.articleSearchFragment -> setToolbar(ArticleToolbarState.ARTICLE_SEARCH, navController)
                ArticleR.id.articleKeywordFragment -> setToolbar(ArticleToolbarState.ARTICLE_KEYWORD, navController)
            }
        }
    }

    private fun setToolbar(state: ArticleToolbarState, navController: androidx.navigation.NavController) {
        binding.toolbarArticle.apply {
            setOnNavigationIconClickListener { navController.navigateUp() }
            setTitle(getString(state.title))
            setMenus(
                ToolbarMenu(
                    menuRes = state.menuRes,
                    onClick = { itemId ->
                        when (itemId) {
                            ArticleR.id.action_search_article -> {
                                EventLogger.logClickEvent(
                                    EventAction.CAMPUS,
                                    AnalyticsConstant.Label.NOTICE_SEARCH,
                                    getString(ArticleR.string.search)
                                )
                                navController.navigate(ArticleR.id.action_articleListFragment_to_articleSearchFragment)
                            }
                        }
                    }
                )
            )
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
