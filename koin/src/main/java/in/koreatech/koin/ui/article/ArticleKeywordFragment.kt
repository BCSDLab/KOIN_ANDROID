package `in`.koreatech.koin.ui.article

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.EditorInfo
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
import dagger.hilt.android.AndroidEntryPoint
import `in`.koreatech.koin.R
import `in`.koreatech.koin.core.analytics.EventAction
import `in`.koreatech.koin.core.analytics.EventLogger
import `in`.koreatech.koin.core.constant.AnalyticsConstant
import `in`.koreatech.koin.core.dialog.AlertModalDialog
import `in`.koreatech.koin.core.dialog.AlertModalDialogData
import `in`.koreatech.koin.core.permission.checkNotificationPermission
import `in`.koreatech.koin.core.toast.ToastUtil
import `in`.koreatech.koin.databinding.FragmentArticleKeywordBinding
import `in`.koreatech.koin.domain.model.notification.SubscribesType
import `in`.koreatech.koin.ui.article.viewmodel.ArticleKeywordViewModel
import `in`.koreatech.koin.ui.article.viewmodel.KeywordAddUiState
import `in`.koreatech.koin.ui.article.viewmodel.KeywordInputUiState
import `in`.koreatech.koin.ui.notification.viewmodel.NotificationUiState
import `in`.koreatech.koin.ui.notification.viewmodel.NotificationViewModel
import `in`.koreatech.koin.util.SnackbarUtil
import kotlinx.coroutines.launch

@AndroidEntryPoint
class ArticleKeywordFragment : Fragment() {

    private var _binding: FragmentArticleKeywordBinding? = null
    private val binding get() = _binding!!

    private val viewModel by viewModels<ArticleKeywordViewModel>()
    private val notificationViewModel by viewModels<NotificationViewModel>()

    private val loginModal: AlertModalDialog by lazy {
        AlertModalDialog(
            requireContext(),
            AlertModalDialogData(
                title = R.string.recommend_login_to_get_keyword_notification_title,
                message = R.string.recommend_login_to_get_keyword_notification_message,
                positiveButtonText = R.string.action_login
            ),
            onPositiveButtonClicked = {
                EventLogger.logClickEvent(
                    EventAction.CAMPUS,
                    AnalyticsConstant.Label.LOGIN_POPUP_KEYWORD,
                    getString(R.string.action_login)
                )
                val intent = Intent(Intent.ACTION_VIEW).apply {
                    data = Uri.parse("koin://login/login?link=koin://article/activity?fragment=article_keyword")
                }
                it.dismiss()
                startActivity(intent)
            }, onNegativeButtonClicked = {
                EventLogger.logClickEvent(
                    EventAction.CAMPUS,
                    AnalyticsConstant.Label.LOGIN_POPUP_KEYWORD,
                    getString(R.string.close)
                )
                it.dismiss()
            })
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        if (_binding == null) {
            _binding = FragmentArticleKeywordBinding.inflate(inflater, container, false)
            binding.textViewMaxKeywordCount.text =
                ArticleKeywordViewModel.MAX_KEYWORD_COUNT.toString()
            binding.buttonAddKeyword.setOnClickListener {
                viewModel.addKeyword(binding.textInputSearch.text.toString())
            }
            binding.textInputSearch.setOnEditorActionListener { _, id, _ ->
                if (id == EditorInfo.IME_ACTION_DONE) {
                    viewModel.addKeyword(binding.textInputSearch.text.toString())
                    true
                } else false
            }
            binding.textInputSearch.addTextChangedListener {
                viewModel.onKeywordInputChanged(it.toString())
            }
            initKeywordNotification()
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
                            createChip(
                                keyword,
                                R.drawable.ic_close_round,
                                binding.chipGroupMyKeyword,
                                viewModel::deleteKeyword
                            )
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
        binding.textViewMyKeywordCount.text = keywords.size.toString()
        addMyKeywordView(keywords)
        removeMyKeywordView(keywords)
    }

    private fun setSuggestedKeywords() {
        viewLifecycleOwner.lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel.suggestedKeywords.collect { suggests ->
                    suggests.forEach { keyword ->
                        binding.run {
                            if (chipGroupSuggestionKeywords.childCount >= ArticleKeywordViewModel.MAX_SUGGEST_KEYWORD_COUNT)
                                return@forEach

                            if (viewModel.myKeywords.value.contains(keyword).not()) {
                                chipGroupSuggestionKeywords.addView(
                                    createChip(
                                        keyword,
                                        R.drawable.ic_add_round,
                                        binding.chipGroupSuggestionKeywords
                                    ) {
                                        EventLogger.logClickEvent(
                                            EventAction.CAMPUS,
                                            AnalyticsConstant.Label.RECOMMENDED_KEYWORD,
                                            keyword
                                        )
                                        viewModel.addKeyword(it)
                                    }
                                )
                            }
                        }
                    }
                }
            }
        }
    }

    private fun createChip(
        text: String,
        @DrawableRes icon: Int,
        root: ViewGroup,
        onCloseIconClicked: (String) -> Unit
    ): Chip {
        val chip = layoutInflater.inflate(R.layout.chip_layout, root, false) as Chip
        return chip.apply {
            id = View.generateViewId()
            isChecked = false
            isCheckable = false
            isCloseIconVisible = true
            setCloseIconResource(icon)
            setOnCloseIconClickListener {
                onCloseIconClicked(text)
            }
            this.text = text
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

                        KeywordAddUiState.InvalidLength -> {
                            SnackbarUtil.makeShortSnackbar(
                                binding.root,
                                getString(R.string.keyword_add_invalid_length),
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

                        is KeywordAddUiState.Success -> {
                            try {
                                binding.chipGroupSuggestionKeywords.removeView(
                                    binding.chipGroupSuggestionKeywords.children.first { chip ->
                                        (chip as Chip).text == state.keyword
                                    }
                                )
                            } catch (_: Exception) {}
                            binding.textInputSearch.text = null
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
                        background.setTint(
                            when (state) {
                                is KeywordInputUiState.Empty -> ContextCompat.getColor(
                                    requireContext(),
                                    R.color.gray16
                                )

                                is KeywordInputUiState.Valid -> ContextCompat.getColor(
                                    requireContext(),
                                    R.color.colorPrimary
                                )
                            }
                        )
                        setTextColor(
                            when (state) {
                                is KeywordInputUiState.Empty -> ContextCompat.getColor(
                                    requireContext(),
                                    R.color.gray14
                                )

                                is KeywordInputUiState.Valid -> ContextCompat.getColor(
                                    requireContext(),
                                    R.color.white
                                )
                            }
                        )
                    }
                }
            }
        }
    }

    private fun initKeywordNotification() {
        notificationViewModel.getPermissionInfo()

        binding.notificationKeyword.setOnSwitchClickListener { isChecked ->
            if (requireContext().checkNotificationPermission().not()) {
                ToastUtil.getInstance().makeShort(R.string.request_notification_permission)
                binding.notificationKeyword.isChecked = false
                return@setOnSwitchClickListener
            }

            if (viewModel.user.value.isAnonymous) {
                binding.notificationKeyword.isChecked = false
                loginModal.show()
                return@setOnSwitchClickListener
            }
            if (isChecked) {
                EventLogger.logClickEvent(
                    EventAction.CAMPUS,
                    AnalyticsConstant.Label.KEYWORD_NOTIFICATION,
                    "on"
                )
                notificationViewModel.updateSubscription(SubscribesType.ARTICLE_KEYWORD)
            }
            else {
                EventLogger.logClickEvent(
                    EventAction.CAMPUS,
                    AnalyticsConstant.Label.KEYWORD_NOTIFICATION,
                    "off"
                )
                notificationViewModel.deleteSubscription(SubscribesType.ARTICLE_KEYWORD)
            }
        }

        viewLifecycleOwner.lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.STARTED) {
                notificationViewModel.notificationUiState.collect { uiState ->
                    if (uiState is NotificationUiState.Success) {
                        uiState.notificationPermissionInfo.subscribes.forEach {
                            if (it.type == SubscribesType.ARTICLE_KEYWORD) {
                                binding.notificationKeyword.run {
                                    if (isChecked != it.isPermit) {
                                        fakeChecked = it.isPermit
                                        isChecked = it.isPermit
                                    }
                                }
                            }
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