package `in`.koreatech.koin.ui.article

import android.graphics.Canvas
import android.graphics.Paint
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
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
import com.google.android.material.chip.Chip
import com.google.android.material.tabs.TabLayout
import dagger.hilt.android.AndroidEntryPoint
import `in`.koreatech.koin.R
import `in`.koreatech.koin.core.progressdialog.IProgressDialog
import `in`.koreatech.koin.databinding.FragmentArticleListBinding
import `in`.koreatech.koin.ui.article.ArticleDetailFragment.Companion.ARTICLE_ID
import `in`.koreatech.koin.ui.article.adapter.ArticleAdapter
import `in`.koreatech.koin.ui.article.state.ArticleHeaderState
import `in`.koreatech.koin.ui.article.viewmodel.ArticleListViewModel
import `in`.koreatech.koin.util.ext.withLoading
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

@AndroidEntryPoint
class ArticleListFragment : Fragment() {

    private var _binding: FragmentArticleListBinding? = null
    private val binding get() = _binding!!
    private val navController by lazy { findNavController() }

    private val viewModel by viewModels<ArticleListViewModel>()

    private val onTabSelectedListener =
        object : TabLayout.OnTabSelectedListener {
            override fun onTabSelected(tab: TabLayout.Tab?) {
                tab?.let {
                    viewModel.setCurrentBoard(BoardType.entries[it.position])
                }
            }
            override fun onTabUnselected(tab: TabLayout.Tab?) {}
            override fun onTabReselected(tab: TabLayout.Tab?) {}
        }

    private val articleAdapter = ArticleAdapter(onClick = ::onArticleClicked)
    private val pageChips: ArrayList<Chip> by lazy {
        arrayListOf(
            binding.chipPage1,
            binding.chipPage2,
            binding.chipPage3,
            binding.chipPage4,
            binding.chipPage5
        )
    }
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        if (_binding == null) {
            _binding = FragmentArticleListBinding.inflate(inflater, container, false)
            initArticleRecyclerView()
            initPageButtonSelectedListener()
            addCategoryTabs()
            binding.textViewNextPage.setOnClickListener {
                viewModel.setCurrentPage(viewModel.currentPage.value + 1)
            }
            binding.textViewPreviousPage.setOnClickListener {
                viewModel.setCurrentPage(viewModel.currentPage.value - 1)
            }
            binding.imageViewToKeywordAddPage.setOnClickListener {
                navController.navigate(R.id.action_articleListFragment_to_articleKeywordFragment)
            }
        }
        collectData()
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.tabLayoutArticleBoard.addOnTabSelectedListener(onTabSelectedListener)
    }

    private fun initPageButtonSelectedListener() {
        binding.chipGroupArticlePage.setOnCheckedStateChangeListener { _, checkedIds ->
            binding.root.findViewById<Chip>(checkedIds.first()).let {
                viewModel.setCurrentPage(viewModel.pageNumbers.value[pageChips.indexOf(it)])
            }
        }
    }

    private fun initArticleRecyclerView() {
        binding.recyclerViewArticleList.adapter = articleAdapter
        binding.recyclerViewArticleList.addItemDecoration(object: DividerItemDecoration(requireContext(), VERTICAL) {
            override fun onDrawOver(c: Canvas, parent: RecyclerView, state: RecyclerView.State) {
                drawArticleDivider(c, parent)
            }
        })
    }

    private fun addCategoryTabs() {
        BoardType.entries.forEach {
            binding.tabLayoutArticleBoard.addTab(binding.tabLayoutArticleBoard.newTab().apply {
                id = View.generateViewId()
                text = getString(it.simpleKoreanName)
            })
        }
    }

    private fun onArticleClicked(article: ArticleHeaderState) {
        navController.navigate(
            R.id.action_articleListFragment_to_articleDetailFragment,
            bundleOf(ARTICLE_ID to article.id)
        )
    }

    private fun collectData() {
        (requireActivity() as IProgressDialog).withLoading(viewLifecycleOwner, viewModel)
        viewLifecycleOwner.lifecycleScope.run {
            this.launch {
                repeatOnLifecycle(Lifecycle.State.STARTED) {
                    viewModel.articlePagination.collectLatest {
                        articleAdapter.submitList(it.articles)
                        binding.nestedScrollViewArticleList.scrollTo(0, 0)
                    }
                }
            }
            this.launch {
                repeatOnLifecycle(Lifecycle.State.STARTED) {
                    viewModel.currentPage.collect { page ->
                        viewModel.calculatePageNumber()
                        setPagingTextButtonVisibility(page)
                        changeChipSelectedState(page)
                    }
                }
            }
            this.launch {
                repeatOnLifecycle(Lifecycle.State.STARTED) {
                    viewModel.pageNumbers.collect { pageNumbers ->
                        changeChipDataState(pageNumbers)
                    }
                }
            }
        }
    }

    private fun changeChipSelectedState(page: Int) {
        pageChips.forEachIndexed { i, chip ->
            chip.isChecked = page == viewModel.pageNumbers.value[i]
        }
    }

    private fun changeChipDataState(pageNumbers: IntArray) {
        pageChips.forEachIndexed { i, chip ->
            if (pageNumbers[i] != 0) {
                chip.text = pageNumbers[i].toString()
                chip.visibility = View.VISIBLE
            } else {
                chip.visibility = View.GONE
            }
        }
    }

    private fun setPagingTextButtonVisibility(currentPage: Int) {
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

    override fun onDestroyView() {
        super.onDestroyView()
        binding.tabLayoutArticleBoard.removeOnTabSelectedListener(onTabSelectedListener)
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }
}