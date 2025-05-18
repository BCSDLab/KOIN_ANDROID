package `in`.koreatech.koin.ui.term

import android.os.Bundle
import android.view.View
import androidx.activity.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.recyclerview.widget.LinearLayoutManager
import dagger.hilt.android.AndroidEntryPoint
import `in`.koreatech.koin.R
import `in`.koreatech.koin.core.activity.ActivityBase
import `in`.koreatech.koin.core.appbar.AppBarBase
import `in`.koreatech.koin.core.permission.checkNotificationPermission
import `in`.koreatech.koin.core.toast.ToastUtil
import `in`.koreatech.koin.databinding.ActivityTermBinding
import `in`.koreatech.koin.domain.model.notification.SubscribesType
import `in`.koreatech.koin.domain.model.term.Term
import `in`.koreatech.koin.ui.notification.viewmodel.NotificationUiState
import `in`.koreatech.koin.ui.notification.viewmodel.NotificationViewModel
import `in`.koreatech.koin.ui.term.TermViewModel.Companion.KEY_TERM
import `in`.koreatech.koin.ui.term.TermViewModel.Companion.TERM_KOIN
import `in`.koreatech.koin.ui.term.TermViewModel.Companion.TERM_MARKETING
import `in`.koreatech.koin.ui.term.TermViewModel.Companion.TERM_PRIVACY_POLICY
import `in`.koreatech.koin.ui.term.TermViewModel.Companion.TERM_UNKNOWN
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import timber.log.Timber

@AndroidEntryPoint
class TermActivity : ActivityBase(R.layout.activity_term) {
    private lateinit var binding: ActivityTermBinding
    override val screenTitle: String
        get() =
            when (term) {
                TERM_KOIN -> getString(R.string.setting_item_koin_terms)
                TERM_PRIVACY_POLICY -> getString(R.string.setting_item_privacy_policy)
                else -> TERM_UNKNOWN
            }

    private var term = TERM_UNKNOWN

    private val viewModel by viewModels<TermViewModel>()
    private val notificationViewModel: NotificationViewModel by viewModels()

    private val articleAdapter by lazy {
        TermArticleAdapter(
            onClickArticle = ::scrollContent
        )
    }

    private val contentAdapter by lazy {
        TermContentAdapter()
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityTermBinding.inflate(layoutInflater)
        setContentView(binding.root)

        loadTerm()
        initView()
        initObservers()
    }

    override fun onResume() {
        super.onResume()
        if (!checkNotificationPermission()) {
            permissionDenied()
        } else {
            permissionGranted()
        }
    }

    private fun permissionGranted() {
        notificationViewModel.getPermissionInfo()
        // TODO
    }

    private fun permissionDenied() {
        // TODO
    }

    private fun loadTerm() {
        viewModel.setTermType(intent.getStringExtra(KEY_TERM) ?: TERM_UNKNOWN)
        when (intent.getStringExtra(KEY_TERM)) {
            TERM_KOIN -> {
                viewModel.loadKoinTerm()
            }

            TERM_PRIVACY_POLICY -> {
                viewModel.loadPrivacyTerm()
            }

            TERM_MARKETING -> {
                viewModel.loadMarketingTerm()
            }

            else -> {
                ToastUtil.getInstance().makeShort("약관 타입을 명시해야 합니다.")
            }
        }
    }

    private fun initView() {
        with(binding) {
            appbarTermKoin.setOnClickListener {
                when (it.id) {
                    AppBarBase.getLeftButtonId() -> {
                        onBackPressedDispatcher.onBackPressed()
                    }
                }
            }
            fab.setOnClickListener {
                nsvTermKoin.smoothScrollTo(0, 0)
            }

            rvArticle.adapter = articleAdapter
            rvArticle.layoutManager = LinearLayoutManager(this@TermActivity)
            rvArticle.isNestedScrollingEnabled = false
            rvContent.adapter = contentAdapter
            rvContent.layoutManager = LinearLayoutManager(this@TermActivity)
            rvContent.isNestedScrollingEnabled = false

            notificationHeaderTermMarketingTermSwitch.setOnSwitchClickListener { isChecked ->
                if (isChecked) {
                    notificationViewModel.updateSubscription(SubscribesType.MARKETING)
                } else {
                    notificationViewModel.deleteSubscription(SubscribesType.MARKETING)
                }
            }
        }
    }

    private fun initObservers() {
        lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel.term.collect { termState ->
                    when (termState) {
                        is TermState.Init -> {}
                        is TermState.Success -> {
                            setTermContents(termState.term)
                        }

                        is TermState.Failure -> {
                            ToastUtil.getInstance().makeShort("약관을 불러오는 데 실패했습니다. 다시 시도해주세요")
                        }
                    }
                }
            }
        }
        lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel.termType.collectLatest {
                    binding.notificationHeaderTermMarketingTermSwitch.visibility = if (it == TERM_MARKETING) {
                        View.VISIBLE
                    } else {
                        View.GONE
                    }
                }
            }
        }
        lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.STARTED) {
                notificationViewModel.notificationUiState.collect { uiState ->
                    when (uiState) {
                        is NotificationUiState.Success -> {
                            uiState.notificationPermissionInfo.subscribes.forEach {
                                when (it.type) {
                                    SubscribesType.MARKETING -> {
                                        Timber.d("marketing: ${it.isPermit}")
                                        with(binding.notificationHeaderTermMarketingTermSwitch) {
                                            if (isChecked != it.isPermit) {
                                                fakeChecked = it.isPermit
                                                isChecked = it.isPermit
                                            }
                                        }
                                    }

                                    SubscribesType.NOTHING -> Unit
                                    else -> Unit
                                }
                            }
                        }

                        is NotificationUiState.Failed -> {}
                        is NotificationUiState.Nothing -> {}
                    }
                }
            }
        }
    }

    private fun scrollContent(position: Int) {
        val y =
            binding.rvContent.getChildAt(position).let {
                it.y + binding.rvContent.y
            }.toInt()
        binding.nsvTermKoin.smoothScrollTo(0, y)
    }

    private fun setTermContents(term: Term) {
        with(binding) {
            tvTermKoinTitle.text = term.header
            articleAdapter.submitList(term.articles.map { it.article })
            contentAdapter.submitList(term.articles)
        }
    }
}
