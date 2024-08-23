package `in`.koreatech.koin.ui.article

import android.os.Bundle
import android.text.TextUtils
import android.view.View
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.navigation.fragment.findNavController
import dagger.hilt.android.AndroidEntryPoint
import `in`.koreatech.koin.R
import `in`.koreatech.koin.core.util.dataBinding
import `in`.koreatech.koin.databinding.FragmentArticleDetailBinding
import `in`.koreatech.koin.domain.util.DateFormatUtil
import `in`.koreatech.koin.domain.util.TimeUtil
import `in`.koreatech.koin.ui.article.adapter.HotArticleAdapter
import `in`.koreatech.koin.ui.article.state.ArticleHeaderState
import `in`.koreatech.koin.ui.article.viewmodel.ArticleDetailViewModel
import `in`.koreatech.koin.util.ext.getParcelableExtraCompat
import kotlinx.coroutines.launch
import javax.inject.Inject

@AndroidEntryPoint
class ArticleDetailFragment : Fragment(R.layout.fragment_article_detail) {

    @Inject
    lateinit var articleDetailViewModelFactory: ArticleDetailViewModel.Factory
    private val navController by lazy { findNavController() }

    private val binding: FragmentArticleDetailBinding by dataBinding()
    private val hotArticleAdapter = HotArticleAdapter(::onHotArticleClick)
    private val viewModel: ArticleDetailViewModel by viewModels {
        ArticleDetailViewModel.provideFactory(
            articleDetailViewModelFactory,
            requireArguments().getParcelableExtraCompat<ArticleHeaderState>(ARTICLE_HEADER) ?: throw IllegalArgumentException("ArticleState is required"),
        )
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initHeader()
        setContent()
        initHotArticles()
        initButtonClickListeners()
    }

    private fun initHeader() {
        binding.articleHeader.apply {
            textViewArticleBoardName.text = getString(viewModel.articleHeader.boardName)
            textViewArticleTitle.text = viewModel.articleHeader.title
            textViewArticleAuthor.text = viewModel.articleHeader.author
            textViewArticleDate.text = TextUtils.concat(DateFormatUtil.getSimpleMonthAndDay(viewModel.articleHeader.createdAt),
                    " ", DateFormatUtil.getDayOfWeek(TimeUtil.stringToDateYYYYMMDD(viewModel.articleHeader.createdAt)))
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

    private fun initHotArticles() {
        binding.recyclerViewHotArticles.adapter = hotArticleAdapter
        binding.recyclerViewHotArticles.isNestedScrollingEnabled = false
        viewLifecycleOwner.lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel.hotArticles.collect {
                    hotArticleAdapter.submitList(it)
                }
            }
        }
    }

    private fun initButtonClickListeners() {
        binding.buttonToList.setOnClickListener {
            navController.popBackStack(R.id.articleListFragment, false)
        }
    }

    private fun onHotArticleClick(article: ArticleHeaderState) {
        navController.navigate(
            R.id.action_articleDetailFragment_to_articleDetailFragment,
            Bundle().apply {
                putParcelable(ARTICLE_HEADER, article)
            }
        )
    }

    companion object {
        const val ARTICLE_HEADER = "article_header"
    }
}