package `in`.koreatech.koin.ui.newmain.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.core.view.doOnAttach
import androidx.core.view.updatePadding
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.navigation.NavController
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.fragment.findNavController
import dagger.hilt.android.AndroidEntryPoint
import `in`.koreatech.koin.R
import `in`.koreatech.koin.core.analytics.AnalyticsConstant
import `in`.koreatech.koin.core.analytics.EventAction
import `in`.koreatech.koin.core.analytics.EventLogger
import `in`.koreatech.koin.core.appbar.ToolbarMenu
import `in`.koreatech.koin.databinding.FragmentArticleHostBinding
import `in`.koreatech.koin.feature.article.R as ArticleR
import `in`.koreatech.koin.feature.article.model.ArticleToolbarState
import `in`.koreatech.koin.ui.newmain.ArticleBackgroundController
import `in`.koreatech.koin.ui.newmain.ArticleHostViewModel
import kotlinx.coroutines.launch

@AndroidEntryPoint
class ArticleFragment : Fragment() {

    private var _binding: FragmentArticleHostBinding? = null
    private val binding get() = _binding!!

    private val viewModel by viewModels<ArticleHostViewModel>()

    override fun onResume() {
        super.onResume()
        (activity as? ArticleBackgroundController)?.setArticleBackground()
    }

    override fun onPause() {
        super.onPause()
        (activity as? ArticleBackgroundController)?.clearArticleBackground()
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

        val originalTopPadding = binding.topbarArticleList.paddingTop
        ViewCompat.setOnApplyWindowInsetsListener(binding.topbarArticleList) { view, insets ->
            val statusBarHeight = insets.getInsets(WindowInsetsCompat.Type.systemBars()).top
            view.updatePadding(top = originalTopPadding + statusBarHeight)
            WindowInsetsCompat.CONSUMED
        }

        binding.btnNotificationArticle.setOnClickListener {
            findNavController().navigate(R.id.action_article_to_notification)
        }

        viewLifecycleOwner.lifecycleScope.launch {
            viewLifecycleOwner.repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel.hasUnreadNotification.collect { hasUnread ->
                    binding.btnNotificationArticle.setImageResource(
                        if (hasUnread) {
                            ArticleR.drawable.ic_koin_notification_dot
                        } else {
                            ArticleR.drawable.ic_koin_notification
                        }
                    )
                }
            }
        }

        val navHostFragment =
            childFragmentManager.findFragmentById(R.id.nav_host_article_fragment) as NavHostFragment
        val navController = navHostFragment.navController

        navController.addOnDestinationChangedListener { _, dest, _ ->
            val isArticleList = dest.id == ArticleR.id.articleListFragment
            binding.topbarArticleList.visibility = if (isArticleList) View.VISIBLE else View.GONE
            binding.toolbarArticle.visibility = if (isArticleList) View.GONE else View.VISIBLE

            if (!isArticleList) {
                when (dest.id) {
                    ArticleR.id.articleDetailFragment -> setToolbar(ArticleToolbarState.ARTICLE_DETAIL, navController)
                    ArticleR.id.articleSearchFragment -> setToolbar(ArticleToolbarState.ARTICLE_SEARCH, navController)
                    ArticleR.id.articleKeywordFragment -> setToolbar(ArticleToolbarState.ARTICLE_KEYWORD, navController)
                }
            }
        }
    }

    private fun setToolbar(state: ArticleToolbarState, navController: NavController) {
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