package `in`.koreatech.koin.ui.article.lostandfound

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
import `in`.koreatech.koin.core.navigation.Navigator
import `in`.koreatech.koin.feature.lostandfound.ui.lostandfound.LostAndFoundList
import `in`.koreatech.koin.feature.lostandfound.ui.write.LostAndFoundWriteArticleViewModel.Companion.LOST_OR_FOUND_TYPE
import javax.inject.Inject

@AndroidEntryPoint
class ArticleListLostAndFoundFragment : Fragment() {

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
                        navigator.navigateToSignIn(
                            requireContext(),
                            "koin://article/activity?fragment=article_lost_and_found"
                        ).let(::startActivity)
                    }
                )
            }
        }
    }

    companion object {
        const val ARTICLE_ID = "article_id"
    }
}
