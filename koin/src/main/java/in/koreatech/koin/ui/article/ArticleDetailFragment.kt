package `in`.koreatech.koin.ui.article

import android.annotation.SuppressLint
import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import dagger.hilt.android.AndroidEntryPoint
import `in`.koreatech.koin.R
import `in`.koreatech.koin.core.util.dataBinding
import `in`.koreatech.koin.databinding.FragmentArticleDetailBinding
import `in`.koreatech.koin.domain.util.DateFormatUtil
import `in`.koreatech.koin.domain.util.TimeUtil
import `in`.koreatech.koin.ui.article.state.ArticleHeaderState
import `in`.koreatech.koin.ui.article.viewmodel.ArticleDetailViewModel
import `in`.koreatech.koin.util.ext.getParcelableExtraCompat
import kotlinx.coroutines.launch
import javax.inject.Inject

@AndroidEntryPoint
class ArticleDetailFragment : Fragment(R.layout.fragment_article_detail) {

    @Inject
    lateinit var articleDetailViewModelFactory: ArticleDetailViewModel.Factory

    private val binding: FragmentArticleDetailBinding by dataBinding()
    private val viewModel: ArticleDetailViewModel by viewModels {
        ArticleDetailViewModel.provideFactory(
            articleDetailViewModelFactory,
            requireArguments().getParcelableExtraCompat<ArticleHeaderState>("article") ?: throw IllegalArgumentException("ArticleState is required"),
        )
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initHeader()
        setContent()
    }

    @SuppressLint("SetTextI18n")
    private fun initHeader() {
        binding.articleHeader.apply {
            textViewArticleBoardName.text = getString(viewModel.articleHeader.boardName)
            textViewArticleTitle.text = viewModel.articleHeader.title
            textViewArticleAuthor.text = viewModel.articleHeader.author
            textViewArticleDate.text = DateFormatUtil.getSimpleMonthAndDay(viewModel.articleHeader.createdAt) +
                    " " + DateFormatUtil.getDayOfWeek(TimeUtil.stringToDateYYYYMMDD(viewModel.articleHeader.createdAt))
            textViewArticleViewCount.text = viewModel.articleHeader.viewCount.toString()
        }
    }

    private fun setContent() {
        viewLifecycleOwner.lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel.htmlContent.collect { htmlElement ->
                    binding.htmlView.setHtml(htmlElement)
                }
            }
        }
    }
}