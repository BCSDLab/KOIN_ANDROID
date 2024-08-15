package `in`.koreatech.koin.ui.article

import android.graphics.Canvas
import android.graphics.Paint
import android.os.Bundle
import android.view.View
import androidx.core.content.ContextCompat
import androidx.core.os.bundleOf
import androidx.core.view.children
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.tabs.TabLayout
import dagger.hilt.android.AndroidEntryPoint
import `in`.koreatech.koin.R
import `in`.koreatech.koin.core.util.dataBinding
import `in`.koreatech.koin.databinding.FragmentArticleListBinding
import `in`.koreatech.koin.ui.article.adapter.ArticleAdapter
import `in`.koreatech.koin.ui.article.state.ArticleState
import `in`.koreatech.koin.ui.article.viewmodel.ArticleListViewModel
import kotlinx.coroutines.launch

@AndroidEntryPoint
class ArticleListFragment : Fragment(R.layout.fragment_article_list) {

    private val binding by dataBinding<FragmentArticleListBinding>()
    private val navController by lazy { findNavController() }
    private val viewModel by viewModels<ArticleListViewModel>()

    private val articleAdapter = ArticleAdapter(onClick = ::onArticleClicked)

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        addCategoryTabs()
        initArticleRecyclerView()
        initTabSelectedListener()
        collectData()

    }

    private fun initArticleRecyclerView() {
        binding.recyclerViewArticleList.adapter = articleAdapter
        binding.recyclerViewArticleList.addItemDecoration(object: DividerItemDecoration(requireContext(), VERTICAL) {
            override fun onDrawOver(c: Canvas, parent: RecyclerView, state: RecyclerView.State) {
                drawArticleDivider(c, parent)
            }
        })
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

    private fun addCategoryTabs() {
        BoardType.entries.forEach {
            binding.tabLayoutArticleBoard.addTab(binding.tabLayoutArticleBoard.newTab().apply {
                text = getString(it.simpleKoreanName)
            })
        }
    }

    private fun onArticleClicked(article: ArticleState) {
        navController.navigate(
            R.id.action_articleListFragment_to_articleDetailFragment,
            bundleOf("article" to article)
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

    private fun drawArticleDivider(c: Canvas, parent: RecyclerView) {
        val paint = Paint()
        paint.color = ContextCompat.getColor(requireContext(), R.color.divider)
        val height = 1f

        val left = parent.paddingLeft.toFloat()
        val right = parent.width - parent.paddingRight.toFloat()
        parent.children.forEach { child ->
            val params = child.layoutParams as RecyclerView.LayoutParams
            val top = child.bottom + params.bottomMargin.toFloat()
            val bottom = top + height
            c.drawRect(left, top, right, bottom, paint)
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