package `in`.koreatech.koin.feature.article.ui.article.list

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
import `in`.koreatech.koin.core.designsystem.theme.KoinTheme
import `in`.koreatech.koin.core.navigation.Navigator
import `in`.koreatech.koin.feature.article.ArticleActivity.Companion.START_BOARD
import `in`.koreatech.koin.feature.article.R
import `in`.koreatech.koin.feature.article.enums.ArticleBoardType
import `in`.koreatech.koin.feature.article.ui.article.detail.ArticleDetailFragment.Companion.ARTICLE_ID
import `in`.koreatech.koin.feature.article.ui.article.detail.ArticleDetailFragment.Companion.NAVIGATED_BOARD_ID
import `in`.koreatech.koin.feature.article.ui.lostandfound.write.LostAndFoundWriteArticleViewModel.Companion.LOST_OR_FOUND_TYPE
import javax.inject.Inject

@AndroidEntryPoint
class ArticleListFragment : Fragment() {

    @Inject
    lateinit var navigator: Navigator

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val navController = findNavController()
        return ComposeView(requireContext()).apply {
            setViewCompositionStrategy(ViewCompositionStrategy.DisposeOnViewTreeLifecycleDestroyed)
            setContent {
                KoinTheme {
                    ArticleScreen(
                        board = ArticleBoardType.fromId(arguments?.getInt(START_BOARD) ?: ArticleBoardType.ALL.id),
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
                        },
                        navigateToWriteFoundItem = {
                            when (it) {
                                "LOST" ->
                                    navController.navigate(
                                        R.id.articleLostAndFoundWriteLostFragment,
                                        bundleOf(LOST_OR_FOUND_TYPE to "LOST")
                                    )

                                "FOUND" ->
                                    navController.navigate(
                                        R.id.articleLostAndFoundWriteFoundFragment,
                                        bundleOf(LOST_OR_FOUND_TYPE to "FOUND")
                                    )
                            }
                        },
                        navigateToLostAndFoundDetail = { articleId ->
                            navController.navigate(
                                R.id.articleLostAndFoundDetailFragment,
                                bundleOf(ARTICLE_ID to articleId)
                            )
                        },
                        navigateToLoginActivity = {
                            navigator.navigateToSignIn(
                                requireContext(),
                                "koin://article/activity?fragment=article_lost_and_found"
                            ).let(::startActivity)
                        }
                    )
                }
            }
        }
    }
}
