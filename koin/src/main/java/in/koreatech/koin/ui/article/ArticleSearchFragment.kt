package `in`.koreatech.koin.ui.article

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.children
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import com.google.android.material.chip.Chip
import dagger.hilt.android.AndroidEntryPoint
import `in`.koreatech.koin.databinding.FragmentArticleSearchBinding
import `in`.koreatech.koin.ui.article.viewmodel.ArticleSearchViewModel
import kotlinx.coroutines.launch

@AndroidEntryPoint
class ArticleSearchFragment : Fragment() {

    private var _binding: FragmentArticleSearchBinding? = null
    private val binding get() = _binding!!

    private val viewModel: ArticleSearchViewModel by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentArticleSearchBinding.inflate(inflater, container, false).also {
            initMostSearchedKeywordChips()
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
}