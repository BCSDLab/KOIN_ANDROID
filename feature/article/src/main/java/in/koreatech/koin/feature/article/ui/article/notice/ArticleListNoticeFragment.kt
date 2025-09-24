package `in`.koreatech.koin.feature.article.ui.article.notice

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.compose.ui.platform.ComposeView
import androidx.compose.ui.platform.ViewCompositionStrategy
import androidx.core.os.bundleOf
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import dagger.hilt.android.AndroidEntryPoint
import `in`.koreatech.koin.feature.article.R
import `in`.koreatech.koin.feature.article.enums.ArticleBoardType
import `in`.koreatech.koin.feature.article.ui.article.detail.ArticleDetailFragment.Companion.ARTICLE_ID
import `in`.koreatech.koin.feature.article.ui.article.detail.ArticleDetailFragment.Companion.NAVIGATED_BOARD_ID

@AndroidEntryPoint
class ArticleListNoticeFragment : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val navController = findNavController()
        return ComposeView(requireContext()).apply {
            setViewCompositionStrategy(ViewCompositionStrategy.DisposeOnViewTreeLifecycleDestroyed)
            setContent {
                NoticeListScreen(
                    board = ArticleBoardType.entries.find { board -> board.id == arguments?.getInt("boardId") }
                        ?: ArticleBoardType.ALL,
                    navigateToArticleDetail = { articleId, boardId ->
                        navController.navigate(
                            R.id.action_articleListFragment_to_articleDetailFragment,
                            bundleOf(
                                ARTICLE_ID to articleId,
                                NAVIGATED_BOARD_ID to boardId
                            )
                        )
                    },
                    navigateToKeywordSetting = {
                        navController.navigate(R.id.action_articleListFragment_to_articleKeywordFragment)
                    }
                )
            }
        }
    }
}
