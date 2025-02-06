package `in`.koreatech.koin.ui.article.lostandfound

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.compose.ui.platform.ComposeView
import androidx.compose.ui.platform.ViewCompositionStrategy
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import dagger.hilt.android.AndroidEntryPoint
import `in`.koreatech.koin.R
import `in`.koreatech.koin.feature.lostandfound.enums.LostOrFoundType
import `in`.koreatech.koin.feature.lostandfound.ui.write.LostAndFoundWriteArticle
import `in`.koreatech.koin.ui.article.ArticleDetailFragment.Companion.NAVIGATED_BOARD_ID
import `in`.koreatech.koin.ui.article.lostandfound.ArticleLostAndFoundDetailFragment.Companion
import `in`.koreatech.koin.ui.article.lostandfound.ArticleLostAndFoundDetailFragment.Companion.ARTICLE_ID

@AndroidEntryPoint
class ArticleLostAndFoundWriteFragment : Fragment() {
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val navController = findNavController()

        return ComposeView(requireContext()).apply {
            setViewCompositionStrategy(ViewCompositionStrategy.DisposeOnViewTreeLifecycleDestroyed)
            setContent {
                LostAndFoundWriteArticle(
                    rawLostOrFoundType = requireArguments().getString(LOST_OR_FOUND_TYPE)
                ) { articleId ->
                    navController.popBackStack()
                    navController.navigate(
                        R.id.articleLostAndFoundDetailFragment,
                        Bundle().apply {
                            putInt(ARTICLE_ID, articleId)
                        }
                    )
                }
            }
        }
    }
    companion object {
        const val ARTICLE_ID = "article_id"
        const val LOST_OR_FOUND_TYPE = "lost_or_found_type"
    }
}
