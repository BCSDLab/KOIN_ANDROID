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
import `in`.koreatech.koin.feature.lostandfound.ui.lostandfound.LostAndFoundList
import `in`.koreatech.koin.feature.lostandfound.ui.write.LostAndFoundWriteArticleViewModel.Companion.LOST_OR_FOUND_TYPE
import `in`.koreatech.koin.feature.user.ui.signin.SignInActivity
import `in`.koreatech.koin.ui.article.ArticleActivity.Companion.BUNDLE_ARTICLE_EXTRA_KEY
import `in`.koreatech.koin.ui.article.ArticleActivity.Companion.NAV_ARTICLE
import `in`.koreatech.koin.ui.article.ArticleActivity.Companion.START_BOARD
import `in`.koreatech.koin.ui.article.ArticleBoardType

@AndroidEntryPoint
class ArticleListLostAndFoundFragment : Fragment() {
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val navController = findNavController()
        return ComposeView(requireContext()).apply {
            setViewCompositionStrategy(ViewCompositionStrategy.DisposeOnViewTreeLifecycleDestroyed)
            setContent {
                LostAndFoundList(
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
                    navigateToKeywordFragment = {
                        navController.navigate(
                            R.id.action_articleListFragment_to_articleKeywordFragment
                        )
                    },
                    navigateToLoginActivity = {
                        Intent(requireContext(), SignInActivity::class.java).apply {
                            putExtra(
                                BUNDLE_ARTICLE_EXTRA_KEY,
                                bundleOf(
                                    NAV_ARTICLE to true,
                                    START_BOARD to ArticleBoardType.LOSTANDFOUND.id
                                )
                            )
                            flags = Intent.FLAG_ACTIVITY_CLEAR_TOP
                            requireActivity().finish()
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
