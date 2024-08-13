package `in`.koreatech.koin.ui.article

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.navigation.fragment.findNavController
import com.google.android.material.tabs.TabLayout
import dagger.hilt.android.AndroidEntryPoint
import `in`.koreatech.koin.R
import `in`.koreatech.koin.databinding.FragmentArticleListBinding
import `in`.koreatech.koin.ui.article.adapter.ArticleAdapter
import `in`.koreatech.koin.ui.article.state.ArticleState
import `in`.koreatech.koin.ui.article.viewmodel.ArticleViewModel
import kotlinx.coroutines.launch

@AndroidEntryPoint
class ArticleListFragment : Fragment() {

    private lateinit var binding: FragmentArticleListBinding
    private val navController by lazy { findNavController() }
    private val viewModel by activityViewModels<ArticleViewModel>()

    private val articleAdapter = ArticleAdapter(BoardType.ALL, onClick = ::onArticleClicked)

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentArticleListBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initTabSelectedListener()
        collectData()
    }

    private fun initTabSelectedListener() {
        binding.tabLayoutArticleBoard.addOnTabSelectedListener(object : TabLayout.OnTabSelectedListener {
            override fun onTabSelected(tab: TabLayout.Tab?) {
                tab?.let {
                    viewModel.setCurrentBoard(BoardType.entries[it.position])
                }
            }
            override fun onTabUnselected(tab: TabLayout.Tab?) {}
            override fun onTabReselected(tab: TabLayout.Tab?) {}
        })
    }

    private fun onArticleClicked(article: ArticleState) {
        navController.navigate(
            R.id.action_articleListFragment_to_articleDetailFragment,
            Bundle().apply {
                putParcelable("article", article)
            }
        )
    }

    private fun collectData() {
        viewLifecycleOwner.lifecycleScope.run {
            this.launch {
                repeatOnLifecycle(Lifecycle.State.STARTED) {
                    viewModel.currentBoard.collect { board ->
                        viewModel.fetchArticles(board, 1)
                    }
                }
            }
            this.launch {
                repeatOnLifecycle(Lifecycle.State.STARTED) {
                    viewModel.articlePagination.collect {
                        articleAdapter.submitList(it.articles)
                    }
                }
            }
            this.launch {
                repeatOnLifecycle(Lifecycle.State.STARTED) {
                    viewModel.currentPage.collect { page ->
                        setPagingButtonVisibility(page)
                    }
                }
            }
        }
    }

    private fun setPagingButtonVisibility(currentPage: Int) {
        if (currentPage == viewModel.articlePagination.value.totalPage) {
            binding.textViewNextPage.visibility = View.GONE
        } else {
            binding.textViewNextPage.visibility = View.VISIBLE
        }
        if (currentPage == 1) {
            binding.textViewPreviousPage.visibility = View.GONE
        } else {
            binding.textViewPreviousPage.visibility = View.VISIBLE
        }
    }

    companion object {

        @JvmStatic
        fun newInstance() =
            ArticleListFragment().apply {
                arguments = Bundle().apply {

                }
            }
    }
}