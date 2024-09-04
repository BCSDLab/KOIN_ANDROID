package `in`.koreatech.koin.ui.article

import android.graphics.Canvas
import android.graphics.Paint
import android.os.Bundle
import android.text.TextUtils
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
import `in`.koreatech.koin.ui.article.ArticleDetailFragment.Companion.NAVIGATED_BOARD_ID
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
    private lateinit var pageChips: ArrayList<Chip>

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
            pageChips = arrayListOf(
                binding.chipPage1,
                binding.chipPage2,
                binding.chipPage3,
                binding.chipPage4,
                binding.chipPage5
            )
            binding.textViewNextPage.setOnClickListener {
                viewModel.setCurrentPage(viewModel.currentPage.value + 1)
            }
            binding.textViewPreviousPage.setOnClickListener {
                viewModel.setCurrentPage(viewModel.currentPage.value - 1)
            }
            handleKeywordChips()
            collectData()
        }
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
            bundleOf(ARTICLE_ID to article.id, NAVIGATED_BOARD_ID to viewModel.currentBoard.value.id)
        )
    }

    private fun handleKeywordChips() {
        binding.imageViewToKeywordAddPage.setOnClickListener {
            navigateToKeywordFragment()
        }
        binding.chipSeeAll.setOnClickListener {
            viewModel.selectKeyword("")
        }
        viewLifecycleOwner.lifecycleScope.run {
            launch {
                repeatOnLifecycle(Lifecycle.State.STARTED) {
                    viewModel.myKeywords.collect { keywords ->
                        removeKeywordChip(keywords)

                        if (keywords.isEmpty()) {
                            binding.chipGroupMyKeywords.addView(
                                createChip(getString(R.string.add_new_keyword), false, ::navigateToKeywordFragment)
                            )
                        } else {
                            addKeywordChip(keywords)
                        }
                    }
                }
            }
            launch {
                repeatOnLifecycle(Lifecycle.State.STARTED) {
                    viewModel.selectedKeyword.collect { keyword ->
                        var isKeywordSelected = false
                        binding.chipGroupMyKeywords.children.forEach {
                            if ("#$keyword" == (it as Chip).text.toString()) {
                                it.isChecked = true
                                isKeywordSelected = true
                                return@forEach
                            }
                        }
                        if (isKeywordSelected.not()) {      // 원래 선택된 상태였던 키워드가 삭제된 경우
                            viewModel.selectKeyword("")
                            binding.chipSeeAll.isChecked = true
                        }
                    }
                }
            }
        }
    }

    private fun removeKeywordChip(keywords: List<String>) {
        binding.chipGroupMyKeywords.children.forEachIndexed { i, chip ->
            if (i != 0)
                if (keywords.contains((chip as Chip).text.toString().substring(1)).not())
                    binding.chipGroupMyKeywords.removeView(chip)
        }
    }

    private fun addKeywordChip(keywords: List<String>) {
        keywords.forEach { keyword ->
            if (binding.chipGroupMyKeywords.children.any { (it as Chip).text == TextUtils.concat("#", keyword) }.not())
                binding.chipGroupMyKeywords.addView(
                    createChip(TextUtils.concat("#", keyword).toString(), true,
                        onChipClicked = {
                            viewModel.selectKeyword(keyword)
                        }
                    )
                )
        }
    }

    private fun navigateToKeywordFragment() {
        navController.navigate(R.id.action_articleListFragment_to_articleKeywordFragment)
    }

    private fun createChip(text: String, isCheckable: Boolean, onChipClicked: () -> Unit): Chip {
        val chip = layoutInflater.inflate(R.layout.chip_layout, binding.chipGroupMyKeywords, false) as Chip
        return chip.apply {
            id = View.generateViewId()
            this.isCheckable = isCheckable
            isCloseIconVisible = false
            this.text = text
            setOnClickListener { onChipClicked() }
        }
    }

    private fun collectData() {
        (requireActivity() as IProgressDialog).withLoading(viewLifecycleOwner, viewModel)
        viewLifecycleOwner.lifecycleScope.run {
            this.launch {
                repeatOnLifecycle(Lifecycle.State.STARTED) {
                    viewModel.currentBoard.collect { board ->
                        binding.tabLayoutArticleBoard.getTabAt(BoardType.entries.indexOf(board))?.select()
                    }
                }
            }
            this.launch {
                repeatOnLifecycle(Lifecycle.State.STARTED) {
                    viewModel.articlePagination.collectLatest {
                        articleAdapter.submitList(it.articles)
                        binding.nestedScrollViewArticleList.scrollTo(0, 0)
                        if (it.articles.isEmpty()) {
                            binding.textViewEmptyArticles.visibility = View.VISIBLE
                        } else {
                            binding.textViewEmptyArticles.visibility = View.GONE
                        }
                    }
                }
            }
            this.launch {
                repeatOnLifecycle(Lifecycle.State.STARTED) {
                    viewModel.currentPage.collect { page ->
                        setPagingTextButtonVisibility(page)
                        changePageChipSelectedState(page)
                    }
                }
            }
            this.launch {
                repeatOnLifecycle(Lifecycle.State.STARTED) {
                    viewModel.pageNumbers.collectLatest { pageNumbers ->
                        setPagingTextButtonVisibility(viewModel.currentPage.value)
                        changePageChipDataState(pageNumbers)
                        changePageChipSelectedState(viewModel.currentPage.value)
                    }
                }
            }
        }
    }

    private fun changePageChipSelectedState(page: Int) {
        pageChips.forEachIndexed { i, chip ->
            chip.isChecked = page == viewModel.pageNumbers.value[i]
        }
    }

    private fun changePageChipDataState(pageNumbers: IntArray) {
        pageChips.forEachIndexed { i, chip ->
            chip.post {
                if (pageNumbers[i] != 0) {
                    chip.text = pageNumbers[i].toString()
                    chip.visibility = View.VISIBLE
                } else {
                    chip.visibility = View.GONE
                }
            }
        }
    }

    private fun setPagingTextButtonVisibility(currentPage: Int) {
        if (currentPage >= viewModel.articlePagination.value.totalPage) {
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
        _binding = null
    }
}