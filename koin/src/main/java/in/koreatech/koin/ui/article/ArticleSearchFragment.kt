package `in`.koreatech.koin.ui.article

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.EditorInfo
import androidx.activity.addCallback
import androidx.core.os.bundleOf
import androidx.core.view.children
import androidx.core.widget.addTextChangedListener
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.navigation.fragment.findNavController
import com.google.android.material.chip.Chip
import dagger.hilt.android.AndroidEntryPoint
import `in`.koreatech.koin.R
import `in`.koreatech.koin.core.progressdialog.IProgressDialog
import `in`.koreatech.koin.databinding.FragmentArticleSearchBinding
import `in`.koreatech.koin.ui.article.ArticleDetailFragment.Companion.ARTICLE_ID
import `in`.koreatech.koin.ui.article.ArticleDetailFragment.Companion.NAVIGATED_BOARD_ID
import `in`.koreatech.koin.ui.article.adapter.ArticleAdapter
import `in`.koreatech.koin.ui.article.adapter.RecentSearchedHistoryAdapter
import `in`.koreatech.koin.ui.article.state.ArticleHeaderState
import `in`.koreatech.koin.ui.article.viewmodel.ArticleSearchViewModel
import `in`.koreatech.koin.ui.article.viewmodel.SearchUiState
import `in`.koreatech.koin.util.SnackbarUtil
import `in`.koreatech.koin.util.ext.withLoading
import kotlinx.coroutines.launch

@AndroidEntryPoint
class ArticleSearchFragment : Fragment() {

    private var _binding: FragmentArticleSearchBinding? = null
    private val binding get() = _binding!!

    private val viewModel: ArticleSearchViewModel by viewModels()
    private val navController by lazy { findNavController() }

    private val recentSearchedHistoryAdapter: RecentSearchedHistoryAdapter by lazy {
        RecentSearchedHistoryAdapter(
            onSearchHistoryClicked = ::onRecentSearchHistoryClicked,
            onDeleteClicked = ::onRecentSearchHistoryDeleteClicked
        )
    }

    private val searchResultAdapter: ArticleAdapter by lazy {
        ArticleAdapter(onClick = ::onArticleClicked)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        if(_binding == null) {
            _binding = FragmentArticleSearchBinding.inflate(inflater, container, false)
            (requireActivity() as IProgressDialog).withLoading(viewLifecycleOwner, viewModel)
            binding.textInputSearch.setOnEditorActionListener { _, actionId, _ ->
                if (actionId == EditorInfo.IME_ACTION_SEARCH) {
                    viewModel.search()
                    true
                } else false
            }
            initMostSearchedKeywordChips()
            binding.textInputSearch.addTextChangedListener {
                onSearchInputChanged(it.toString())
            }
            binding.imageSearch.setOnClickListener {
                viewModel.search()
            }
            binding.textViewRecentSearchedKeywordClear.setOnClickListener {
                viewModel.clearSearchHistory()
            }

            initSearchHistoryView()
            initSearchResultView()
        }
        return binding.root
    }

    private fun initSearchHistoryView() {
        binding.recyclerViewRecentSearchedKeyword.adapter = recentSearchedHistoryAdapter
        binding.recyclerViewRecentSearchedKeyword.itemAnimator = null
        viewLifecycleOwner.lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel.searchHistory.collect {
                    recentSearchedHistoryAdapter.submitList(it)
                }
            }
        }
    }

    private fun initSearchResultView() {
        requireActivity().onBackPressedDispatcher.addCallback(viewLifecycleOwner) {
            if (binding.frameLayoutSearchResult.visibility == View.VISIBLE) {
                binding.frameLayoutSearchResult.visibility = View.GONE
            } else {
                isEnabled = false
                navController.popBackStack()
            }
        }

        binding.recyclerViewSearchResult.adapter = searchResultAdapter
        binding.recyclerViewSearchResult.itemAnimator = null
        viewLifecycleOwner.lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel.searchResultUiState.collect {
                    when (it) {
                        is SearchUiState.Idle -> Unit
                        is SearchUiState.RequireInput -> SnackbarUtil.makeShortSnackbar(
                            binding.root,
                            getString(R.string.search_input_required)
                        )

                        is SearchUiState.Loading -> Unit
                        is SearchUiState.Success -> {
                            binding.frameLayoutSearchResult.visibility = View.VISIBLE
                            binding.textViewSearchResultEmpty.visibility = View.GONE
                            searchResultAdapter.submitList(it.articlePagination.articles)
                        }

                        is SearchUiState.Empty -> {
                            binding.frameLayoutSearchResult.visibility = View.VISIBLE
                            searchResultAdapter.submitList(emptyList())
                            binding.textViewSearchResultEmpty.visibility = View.VISIBLE
                        }

                        is SearchUiState.Error -> SnackbarUtil.makeShortSnackbar(
                            binding.root,
                            getString(R.string.error_network_unknown)
                        )
                    }
                }
            }
        }
    }

    private fun initMostSearchedKeywordChips() {
        viewLifecycleOwner.lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel.mostSearchedKeywords.collect {
                    binding.chipGroupMostSearchedKeyword.children.forEachIndexed { i, view ->
                        val chipText = it.getOrNull(i)
                        (view as Chip).text = chipText
                        view.setOnClickListener { _ ->
                            onSearchQueryClicked(chipText ?: "")
                        }
                    }
                }
            }
        }
    }

    private fun onSearchInputChanged(query: String) {
        if (query.isEmpty())
            binding.imageSearch.setColorFilter(R.color.neutral_500)
        else
            binding.imageSearch.setColorFilter(R.color.gray14)
        viewModel.onSearchInputChanged(query)
    }

    private fun onRecentSearchHistoryClicked(query: String) {
        onSearchQueryClicked(query)
    }

    private fun onSearchQueryClicked(query: String) {
        binding.textInputSearch.setText(query)
        binding.textInputSearch.setSelection(query.length)
        viewModel.search()
    }

    private fun onRecentSearchHistoryDeleteClicked(query: String) {
        viewModel.deleteSearchHistory(query)
    }

    private fun onArticleClicked(article: ArticleHeaderState) {
        navController.navigate(
            R.id.action_articleSearchFragment_to_articleDetailFragment,
            bundleOf(ARTICLE_ID to article.id, NAVIGATED_BOARD_ID to BoardType.ALL.id)
        )
    }
}