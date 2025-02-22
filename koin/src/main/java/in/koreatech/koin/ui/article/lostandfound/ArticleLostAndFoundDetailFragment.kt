package `in`.koreatech.koin.ui.article.lostandfound

import android.content.Intent
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
import `in`.koreatech.koin.R
import `in`.koreatech.koin.core.analytics.AnalyticsConstant
import `in`.koreatech.koin.core.analytics.EventAction
import `in`.koreatech.koin.core.analytics.EventLogger
import `in`.koreatech.koin.feature.lostandfound.ui.detail.LostAndFoundDetail
import `in`.koreatech.koin.ui.article.ArticleDetailFragment.Companion.NAVIGATED_BOARD_ID

@AndroidEntryPoint
class ArticleLostAndFoundDetailFragment : Fragment() {
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val navController = findNavController()

        return ComposeView(requireContext()).apply {
            setViewCompositionStrategy(ViewCompositionStrategy.DisposeOnViewTreeLifecycleDestroyed)
            setContent {
                LostAndFoundDetail(
                    navigateToArticleList = {
                        navController.popBackStack(R.id.articleListFragment, false)
                    },
                    navigateToHotArticle = { hotArticleData ->
                        EventLogger.logClickEvent(
                            EventAction.CAMPUS,
                            AnalyticsConstant.Label.POPULAR_NOTICE,
                            hotArticleData.articleTitle
                        )
                        navController.navigate(
                            R.id.articleLostAndFoundDetailFragment_to_articleDetailFragment,
                            Bundle().apply {
                                putInt(ARTICLE_ID, hotArticleData.articleId)
                                putInt(NAVIGATED_BOARD_ID, hotArticleData.board.id)
                            }
                        )
                    },
                    navigateToReport = { articleId ->
                        Intent(requireContext(), LostAndFoundReportActivity::class.java).apply {
                            putExtra(
                                ARTICLE_ID,
                                articleId
                            )
                        }.let(::startActivity)
                    }
                )
            }
        }
    }

    companion object {
        const val ARTICLE_ID = "article_id"
    }
}
