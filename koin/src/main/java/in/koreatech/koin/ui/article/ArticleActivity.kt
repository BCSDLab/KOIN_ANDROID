package `in`.koreatech.koin.ui.article

import android.content.Intent
import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.core.os.bundleOf
import androidx.navigation.NavController
import androidx.navigation.fragment.NavHostFragment
import dagger.hilt.android.AndroidEntryPoint
import `in`.koreatech.koin.R
import `in`.koreatech.koin.core.activity.ActivityBase
import `in`.koreatech.koin.core.analytics.AnalyticsConstant
import `in`.koreatech.koin.core.analytics.EventAction
import `in`.koreatech.koin.core.analytics.EventLogger
import `in`.koreatech.koin.core.appbar.ToolbarMenu
import `in`.koreatech.koin.core.navigation.utils.EXTRA_BOARD_ID
import `in`.koreatech.koin.core.navigation.utils.EXTRA_ID
import `in`.koreatech.koin.core.util.dataBinding
import `in`.koreatech.koin.databinding.ActivityArticleBinding
import `in`.koreatech.koin.ui.article.ArticleDetailFragment.Companion.ARTICLE_ID
import `in`.koreatech.koin.ui.article.ArticleDetailFragment.Companion.NAVIGATED_BOARD_ID
import `in`.koreatech.koin.util.ext.whiteStatusBar
import timber.log.Timber

@AndroidEntryPoint
class ArticleActivity : ActivityBase() {
    private val binding by dataBinding<ActivityArticleBinding>()
    private lateinit var navController: NavController
    override val screenTitle: String = "공지사항"

    override fun onCreate(savedInstanceState: Bundle?) {
        enableEdgeToEdge()
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_article)

        window.whiteStatusBar()

        val navHostFragment =
            supportFragmentManager.findFragmentById(
                R.id.nav_host_article_fragment
            ) as NavHostFragment
        navController = navHostFragment.navController

        navController.addOnDestinationChangedListener { _, dest, _ ->
            when (dest.id) {
                R.id.articleListFragment -> setToolbar(ArticleToolbarState.ARTICLE_LIST)
                R.id.articleDetailFragment -> setToolbar(ArticleToolbarState.ARTICLE_DETAIL)
                R.id.articleSearchFragment -> setToolbar(ArticleToolbarState.ARTICLE_SEARCH)
                R.id.articleKeywordFragment -> setToolbar(ArticleToolbarState.ARTICLE_KEYWORD)
                R.id.articleLostAndFoundWriteLostFragment -> setToolbar(
                    ArticleToolbarState.ARTICLE_LOSTANDFOUND_LOST_ITEM
                )
                R.id.articleLostAndFoundWriteFoundFragment -> setToolbar(
                    ArticleToolbarState.ARTICLE_LOSTANDFOUND_FOUND_ITEM
                )
                R.id.articleLostAndFoundDetailFragment -> setToolbar(
                    ArticleToolbarState.ARTICLE_DETAIL
                )
            }
        }

        navigateToDetailFragment()
    }

    override fun onNewIntent(intent: Intent?) {
        navigateToArticleDetail(intent)
        super.onNewIntent(intent)
    }

    private fun setNavigationGraph(startBoard: Int = ArticleBoardType.ALL.id) {
        navController.setGraph(R.navigation.nav_graph_article, bundleOf(START_BOARD to startBoard))
    }

    // 지정된 프래그먼트로 이동 (extra로 전달받은 경우에만)
    private fun navigateToDetailFragment() {
        val uri = intent.data
        val link = uri?.getQueryParameter("fragment") // 내부에서만 사용하는 딥링크

        navigateToArticleDetail(intent)

        when (link) {
            "article_keyword" -> {
                setNavigationGraph()
                navController.popBackStack()
                navController.navigate(
                    R.id.articleKeywordFragment
                ) // See ArticleKeywordFragment, LoginActivity
            }
            "article_detail" -> {
                setNavigationGraph()
                val articleId = uri.getQueryParameter("article_id")?.toIntOrNull() ?: 0
                val boardId = uri.getQueryParameter("board_id")?.toIntOrNull() ?: 0
                navController.popBackStack()
                navController.navigate(
                    R.id.articleDetailFragment,
                    bundleOf(
                        ARTICLE_ID to articleId,
                        NAVIGATED_BOARD_ID to boardId
                    )
                )
            }
            "article_lost_and_found" -> {
                setNavigationGraph(ArticleBoardType.LOSTANDFOUND.id)
            }
            null -> {
                val bundle = intent.getBundleExtra(BUNDLE_ARTICLE_EXTRA_KEY)
                bundle?.getInt(START_BOARD)?.let {
                    setNavigationGraph(it)
                } ?: setNavigationGraph()
            }
        }
    }

    // 키워드 알림 전용
    private fun navigateToArticleDetail(mIntent: Intent?) {
        mIntent?.getIntExtra(EXTRA_ID, -1).let {
            Timber.d("article id : $it")
            if (it == -1) return
            val boardId = mIntent?.getIntExtra(EXTRA_BOARD_ID, ArticleBoardType.ALL.id)
            if (boardId == ArticleBoardType.LOSTANDFOUND.id) {
                setNavigationGraph(ArticleBoardType.LOSTANDFOUND.id)

                navController.navigate(
                    R.id.articleLostAndFoundDetailFragment,
                    bundleOf(
                        ARTICLE_ID to it
                    )
                )
            } else {
                setNavigationGraph()

                navController.navigate(
                    R.id.articleDetailFragment,
                    bundleOf(
                        ARTICLE_ID to it,
                        NAVIGATED_BOARD_ID to ArticleBoardType.ALL.id
                    )
                )
            }
        }
    }

    private fun setToolbar(state: ArticleToolbarState) {
        binding.toolbarArticleList.apply {
            setOnNavigationIconClickListener { onBackPressedCallback.handleOnBackPressed() }
            setTitle(getString(state.title))
            setMenus(
                ToolbarMenu(
                    menuRes = state.menuRes,
                    onClick = { itemId ->
                        when (itemId) {
                            R.id.action_search_article -> {
                                EventLogger.logClickEvent(
                                    EventAction.CAMPUS,
                                    AnalyticsConstant.Label.NOTICE_SEARCH,
                                    getString(R.string.search)
                                )
                                navController.navigate(
                                    R.id.action_articleListFragment_to_articleSearchFragment
                                )
                            }
                        }
                    }
                )
            )
        }
    }

    companion object {
        const val NAVIGATE_ACTION = "navigate_action"
        const val NAV_ARTICLE = "article"
        const val START_BOARD = "start_board"
        const val BUNDLE_ARTICLE_EXTRA_KEY = "BUNDLE_EXTRA_KEY"
    }
}
