package `in`.koreatech.koin.ui.article

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.DrawableRes
import androidx.core.content.ContextCompat
import androidx.core.view.children
import androidx.core.widget.addTextChangedListener
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import com.google.android.material.chip.Chip
import com.google.android.material.shape.ShapeAppearanceModel
import dagger.hilt.android.AndroidEntryPoint
import `in`.koreatech.koin.R
import `in`.koreatech.koin.databinding.FragmentArticleKeywordBinding
import `in`.koreatech.koin.ui.article.viewmodel.ArticleKeywordViewModel
import `in`.koreatech.koin.ui.article.viewmodel.KeywordAddUiState
import `in`.koreatech.koin.ui.article.viewmodel.KeywordInputUiState
import `in`.koreatech.koin.util.SnackbarUtil
import kotlinx.coroutines.launch

@AndroidEntryPoint
class ArticleKeywordFragment : Fragment() {

    private var _binding: FragmentArticleKeywordBinding? = null
    private val binding get() = _binding!!

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
            binding.textInputKeyword.addTextChangedListener {
                viewModel.onKeywordInputChanged(it.toString())
            }
            setMyKeywords()
            setAddButtonState()
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
                        onMyKeywordsChanged(it)
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
                            createKeywordChip(keyword, R.drawable.ic_close_round, viewModel::deleteKeyword)
                        )
                }
            }
        }
    }

    private fun removeMyKeywordView(keywords: List<String>) {
        binding.chipGroupMyKeyword.children.forEach { chip ->
            if (keywords.none { it == (chip as Chip).text }) {
                binding.chipGroupMyKeyword.removeView(chip)
            }
        }
    }

    private fun onMyKeywordsChanged(keywords: List<String>) {
        binding.run {
            textViewMyKeywordCount.text = keywords.size.toString()
            addMyKeywordView(keywords)
            removeMyKeywordView(keywords)
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
            isCloseIconVisible = true
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
                            binding.textInputKeyword.text = null
                        }
                        KeywordAddUiState.Error -> {
                            SnackbarUtil.makeShortSnackbar(
                                binding.root,
                                getString(R.string.error_network_unknown),
                            )
                        }
                    }
                }
            }
        }
    }

    private fun setAddButtonState() {
        viewLifecycleOwner.lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel.keywordInputUiState.collect { state ->
                    binding.buttonAddKeyword.apply {
                        background.setTint(when (state) {
                            is KeywordInputUiState.Empty -> ContextCompat.getColor(requireContext(), R.color.gray16)
                            is KeywordInputUiState.Valid -> ContextCompat.getColor(requireContext(), R.color.colorPrimary)
                        })
                        setTextColor(when (state) {
                            is KeywordInputUiState.Empty -> ContextCompat.getColor(requireContext(), R.color.gray14)
                            is KeywordInputUiState.Valid -> ContextCompat.getColor(requireContext(), R.color.white)
                        })
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