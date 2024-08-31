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
import `in`.koreatech.koin.core.progressdialog.IProgressDialog
import `in`.koreatech.koin.core.util.dataBinding
import `in`.koreatech.koin.databinding.FragmentArticleDetailBinding
import `in`.koreatech.koin.domain.util.DateFormatUtil
import `in`.koreatech.koin.domain.util.TimeUtil
import `in`.koreatech.koin.ui.article.adapter.HotArticleAdapter
import `in`.koreatech.koin.ui.article.state.ArticleHeaderState
import `in`.koreatech.koin.ui.article.state.ArticleState
import `in`.koreatech.koin.ui.article.viewmodel.ArticleDetailViewModel
import `in`.koreatech.koin.util.ext.withLoading
import kotlinx.coroutines.flow.collectLatest
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
            requireArguments().getInt(ARTICLE_ID),
            requireArguments().getInt(NAVIGATED_BOARD_ID)
        )
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        (requireActivity() as IProgressDialog).withLoading(viewLifecycleOwner, viewModel)
        binding.htmlView.setOnPreDrawListener { viewModel.setIsLoading(true) }
        binding.htmlView.setOnPostDrawListener { viewModel.setIsLoading(false) }
        initArticle()
        initHotArticles()
        initButtonClickListeners()
    }

    private fun initArticle() {
        viewLifecycleOwner.lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel.article.collectLatest {
                    setHeader(it)
                    setContent(it)
                    setNavigateArticleButtonVisibility(it)
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
        binding.buttonToPrevArticle.setOnClickListener {
            navController.navigate(
                R.id.action_articleDetailFragment_to_articleDetailFragment,
                Bundle().apply {
                    putInt(ARTICLE_ID, viewModel.article.value.prevArticleId!!)
                    putInt(NAVIGATED_BOARD_ID, viewModel.navigatedBoardId)
                }
            )
        }
        binding.buttonToNextArticle.setOnClickListener {
            navController.navigate(
                R.id.action_articleDetailFragment_to_articleDetailFragment,
                Bundle().apply {
                    putInt(ARTICLE_ID, viewModel.article.value.nextArticleId!!)
                    putInt(NAVIGATED_BOARD_ID, viewModel.navigatedBoardId)
                }
            )
        }
    }

    private fun setHeader(article: ArticleState) {
        binding.articleHeader.apply {
            article.header.boardName?.let {
                textViewArticleBoardName.text = getString(it)
            }
            textViewArticleTitle.text = article.header.title
            textViewArticleAuthor.text = article.header.author
            try {
                textViewArticleDate.text = TextUtils.concat(
                    DateFormatUtil.getSimpleMonthAndDay(article.header.registeredAt),
                    " ",
                    DateFormatUtil.getDayOfWeek(TimeUtil.stringToDateYYYYMMDD(article.header.registeredAt))
                )
            } catch(e: Exception) { }
            textViewArticleViewCount.text = article.header.viewCount.toString()
        }
    }

    private fun setContent(article: ArticleState) {
        binding.htmlView.setHtml(article.content)
    }

    private fun setNavigateArticleButtonVisibility(article: ArticleState) {
        binding.buttonToPrevArticle.visibility = if (article.prevArticleId == null) View.INVISIBLE else View.VISIBLE
        binding.buttonToNextArticle.visibility = if (article.nextArticleId == null) View.INVISIBLE else View.VISIBLE
    }

    private fun onHotArticleClick(article: ArticleHeaderState) {
        navController.navigate(
            R.id.action_articleDetailFragment_to_articleDetailFragment,
            Bundle().apply {
                putInt(ARTICLE_ID, article.id)
            }
        )
    }

    companion object {
        const val ARTICLE_ID = "article_header"
        const val NAVIGATED_BOARD_ID = "navigated_board_id"
    }
}