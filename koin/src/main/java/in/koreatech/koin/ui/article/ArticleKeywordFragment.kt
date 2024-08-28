package `in`.koreatech.koin.ui.article

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.DrawableRes
import androidx.core.view.children
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.navigation.fragment.findNavController
import com.google.android.material.chip.Chip
import com.google.android.material.shape.ShapeAppearanceModel
import dagger.hilt.android.AndroidEntryPoint
import `in`.koreatech.koin.R
import `in`.koreatech.koin.databinding.FragmentArticleKeywordBinding
import `in`.koreatech.koin.ui.article.viewmodel.ArticleKeywordViewModel
import `in`.koreatech.koin.ui.article.viewmodel.KeywordAddUiState
import `in`.koreatech.koin.util.SnackbarUtil
import kotlinx.coroutines.launch

@AndroidEntryPoint
class ArticleKeywordFragment : Fragment() {

    private var _binding: FragmentArticleKeywordBinding? = null
    private val binding get() = _binding!!

    private val navController by lazy { findNavController() }
    private val viewModel by viewModels<ArticleKeywordViewModel>()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        if(_binding == null) {
            _binding = FragmentArticleKeywordBinding.inflate(inflater, container, false)
            binding.textViewMaxKeywordCount.text = ArticleKeywordViewModel.MAX_KEYWORD_COUNT.toString()
            binding.buttonAddKeyword.setOnClickListener {
                viewModel.addKeyword(binding.textInputKeyword.text.toString())
            }
            setMyKeywords()
            setSuggestedKeywords()
            handleKeywordAddResult()
        }
        return binding.root
    }

    private fun setMyKeywords() {
        viewLifecycleOwner.lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel.myKeywords.collect {
                    binding.run {
                        textViewMyKeywordCount.text = it.size.toString()
                        addMyKeywordView(it)
                    }
                }
            }
        }
    }

    private fun addMyKeywordView(keywords: List<String>) {
        binding.run {
            if (chipGroupMyKeyword.childCount < keywords.size) {
                keywords.forEach { keyword ->
                    if (chipGroupMyKeyword.children.none { (it as Chip).text == keyword })
                        chipGroupMyKeyword.addView(
                            createKeywordChip(keyword, R.drawable.ic_close_round, viewModel::removeKeyword)
                        )
                }
            }
        }
    }

    private fun removeMyKeywordView(keyword: String) {
        binding.run {

        }
    }

    private fun setSuggestedKeywords() {
        viewLifecycleOwner.lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel.suggestedKeywords.collect {
                    binding.run {
                        it.forEach { keyword ->
                            chipGroupSuggestionKeywords.addView(
                                createKeywordChip(keyword, R.drawable.ic_add_round, viewModel::addKeyword)
                            )
                        }
                    }
                }
            }
        }
    }

    private fun createKeywordChip(keyword: String, @DrawableRes icon: Int, onCloseIconClicked: (String) -> Unit): Chip {
        return Chip(requireContext()).apply {
            id = View.generateViewId()
            shapeAppearanceModel = ShapeAppearanceModel.Builder()
                .setAllCornerSizes(60f)
                .build()
            isChecked = false
            isCheckable = false
            setCloseIconResource(icon)
            setOnCloseIconClickListener {
                onCloseIconClicked(keyword)
            }
            text = keyword
        }
    }

    private fun handleKeywordAddResult() {
        viewLifecycleOwner.lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel.keywordAddUiState.collect { state ->
                    when (state) {
                        KeywordAddUiState.Nothing -> {}
                        KeywordAddUiState.RequireInput -> {
                            SnackbarUtil.makeShortSnackbar(
                                binding.root,
                                getString(R.string.keyword_add_require_input),
                            )
                        }
                        KeywordAddUiState.LimitExceeded -> {
                            SnackbarUtil.makeShortSnackbar(
                                binding.root,
                                getString(R.string.keyword_add_limit_exceeded),
                            )
                        }
                        KeywordAddUiState.AlreadyExist -> {
                            SnackbarUtil.makeShortSnackbar(
                                binding.root,
                                getString(R.string.keyword_add_already_exist),
                            )
                        }
                        KeywordAddUiState.BlankNotAllowed -> {
                            SnackbarUtil.makeShortSnackbar(
                                binding.root,
                                getString(R.string.keyword_add_blank_not_allowed),
                            )
                        }
                        KeywordAddUiState.Loading -> {
                        }
                        KeywordAddUiState.Success -> {
                        }
                    }
                }
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}