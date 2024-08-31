package `in`.koreatech.koin.ui.article

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.children
import androidx.core.widget.addTextChangedListener
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import com.google.android.material.chip.Chip
import dagger.hilt.android.AndroidEntryPoint
import `in`.koreatech.koin.R
import `in`.koreatech.koin.databinding.FragmentArticleSearchBinding
import `in`.koreatech.koin.ui.article.adapter.RecentSearchedHistoryAdapter
import `in`.koreatech.koin.ui.article.viewmodel.ArticleSearchViewModel
import kotlinx.coroutines.launch

@AndroidEntryPoint
class ArticleSearchFragment : Fragment() {

    private var _binding: FragmentArticleSearchBinding? = null
    private val binding get() = _binding!!

    private val viewModel: ArticleSearchViewModel by viewModels()

    private val recentSearchedHistoryAdapter: RecentSearchedHistoryAdapter by lazy {
        RecentSearchedHistoryAdapter(
            onKeywordClicked = ::onRecentKeywordClicked,
            onDeleteClicked = ::onRecentKeywordDeleteClicked
        )
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        if(_binding == null) {
            _binding = FragmentArticleSearchBinding.inflate(inflater, container, false)
            initMostSearchedKeywordChips()
            binding.textInputSearch.addTextChangedListener {
                onSearchInputChanged(it.toString())
            }
            binding.imageSearch.setOnClickListener {
                viewModel.search()
            }

            binding.recyclerViewRecentSearchedKeyword.adapter = recentSearchedHistoryAdapter
            viewLifecycleOwner.lifecycleScope.launch {
                repeatOnLifecycle(Lifecycle.State.STARTED) {
                    viewModel.searchHistory.collect {
                        recentSearchedHistoryAdapter.submitList(it)
                    }
                }
            }
        }
        return binding.root
    }

    private fun initMostSearchedKeywordChips() {
        viewLifecycleOwner.lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel.mostSearchedKeywords.collect {
                    binding.chipGroupMostSearchedKeyword.children.forEachIndexed { i, view ->
                        (view as Chip).text = it.getOrNull(i)
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

    private fun onRecentKeywordClicked(keyword: String) {
        binding.textInputSearch.setText(keyword)
    }

    private fun onRecentKeywordDeleteClicked(keyword: String) {

    }
}