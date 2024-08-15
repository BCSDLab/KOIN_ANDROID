package `in`.koreatech.koin.ui.article

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import dagger.hilt.android.AndroidEntryPoint
import `in`.koreatech.koin.R
import `in`.koreatech.koin.core.util.dataBinding
import `in`.koreatech.koin.databinding.FragmentArticleDetailBinding
import `in`.koreatech.koin.domain.util.DateFormatUtil
import `in`.koreatech.koin.domain.util.TimeUtil
import `in`.koreatech.koin.ui.article.state.ArticleState
import `in`.koreatech.koin.ui.article.viewmodel.ArticleDetailViewModel
import `in`.koreatech.koin.util.ext.getParcelableExtraCompat
import javax.inject.Inject

@AndroidEntryPoint
class ArticleDetailFragment : Fragment(R.layout.fragment_article_detail) {

    @Inject
    lateinit var articleDetailViewModelFactory: ArticleDetailViewModel.Factory

    private val binding: FragmentArticleDetailBinding by dataBinding()
    private val viewModel: ArticleDetailViewModel by viewModels {
        ArticleDetailViewModel.provideFactory(
            articleDetailViewModelFactory,
            requireArguments().getParcelableExtraCompat<ArticleState>("article") ?: throw IllegalArgumentException("ArticleState is required")
        )
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initHeader()
    }

    private fun initHeader() {
        binding.articleHeader.apply {
            textViewArticleBoardName.text = getString(viewModel.article.boardName)
            textViewArticleTitle.text = viewModel.article.title
            textViewArticleAuthor.text = viewModel.article.author
            textViewArticleDate.text = DateFormatUtil.getSimpleMonthAndDay(viewModel.article.createdAt) +
                    " " + DateFormatUtil.getDayOfWeek(TimeUtil.stringToDateYYYYMMDD(viewModel.article.createdAt))
            textViewArticleViewCount.text = viewModel.article.viewCount.toString()
        }
    }
}