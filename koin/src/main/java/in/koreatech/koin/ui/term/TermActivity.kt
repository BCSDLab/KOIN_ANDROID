package `in`.koreatech.koin.ui.term

import android.os.Bundle
import androidx.activity.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.recyclerview.widget.LinearLayoutManager
import dagger.hilt.android.AndroidEntryPoint
import `in`.koreatech.koin.R
import `in`.koreatech.koin.core.activity.ActivityBase
import `in`.koreatech.koin.core.appbar.AppBarBase
import `in`.koreatech.koin.core.toast.ToastUtil
import `in`.koreatech.koin.databinding.ActivityTermBinding
import `in`.koreatech.koin.domain.model.term.Term
import kotlinx.coroutines.launch

@AndroidEntryPoint
class TermActivity : ActivityBase(R.layout.activity_term) {
    private lateinit var binding: ActivityTermBinding
    override val screenTitle: String
        get() = when (term) {
            TERM_KOIN -> getString(R.string.setting_item_koin_terms)
            TERM_PRIVACY_POLICY -> getString(R.string.setting_item_privacy_policy)
            else -> TERM_UNKNOWN
        }

    private var term = TERM_UNKNOWN

    private val viewModel by viewModels<TermViewModel>()

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

    private fun loadTerm() {
        when (intent.getStringExtra(KEY_TERM)) {
            TERM_KOIN -> {
                viewModel.loadKoinTerm()
            }

            TERM_PRIVACY_POLICY -> {
                viewModel.loadPrivacyTerm()
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
    }

    private fun scrollContent(position: Int) {
        val y = binding.rvContent.getChildAt(position).let {
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

    companion object {
        const val KEY_TERM = "key_term"
        const val TERM_KOIN = "term_koin"
        const val TERM_PRIVACY_POLICY = "term_privacy_policy"
        private const val TERM_UNKNOWN = "term_unknown"
    }
}