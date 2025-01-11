package `in`.koreatech.koin.ui.article

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import com.google.android.material.tabs.TabLayout
import dagger.hilt.android.AndroidEntryPoint
import `in`.koreatech.koin.R
import `in`.koreatech.koin.core.analytics.AnalyticsConstant
import `in`.koreatech.koin.core.analytics.EventAction
import `in`.koreatech.koin.core.analytics.EventLogger
import `in`.koreatech.koin.core.progressdialog.IProgressDialog
import `in`.koreatech.koin.databinding.FragmentArticleListBinding
import `in`.koreatech.koin.ui.article.ArticleActivity.Companion.START_BOARD
import `in`.koreatech.koin.ui.article.viewmodel.ArticleListViewModel
import `in`.koreatech.koin.util.ext.withLoading
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch


@AndroidEntryPoint
class ArticleListFragment : Fragment() {

    private var _binding: FragmentArticleListBinding? = null
    private val binding get() = _binding!!

    private val onTabSelectedListener =
        object : TabLayout.OnTabSelectedListener {
            override fun onTabSelected(tab: TabLayout.Tab?) {
                tab?.let {
                    EventLogger.logClickEvent(
                        EventAction.CAMPUS,
                        AnalyticsConstant.Label.NOTICE_TAB,
                        it.text.toString()
                    )
                    viewModel.setCurrentBoard(ArticleBoardType.entries[it.position])
                }
            }

            override fun onTabUnselected(tab: TabLayout.Tab?) {}
            override fun onTabReselected(tab: TabLayout.Tab?) {}
        }

    private val viewModel by viewModels<ArticleListViewModel>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setStartBoard()
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        if (_binding == null) {
            _binding = FragmentArticleListBinding.inflate(inflater, container, false)
            addCategoryTabs()
            collectData()
            observeBoard()
        }
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.tabLayoutArticleBoard.addOnTabSelectedListener(onTabSelectedListener)
    }

    private fun setStartBoard() {
        val startBoard = arguments?.getInt(START_BOARD) ?: ArticleBoardType.ALL.id
        viewModel.setCurrentBoard(ArticleBoardType.fromId(startBoard))
    }

    private fun observeBoard() {
        lifecycleScope.launch {
            viewModel.currentBoard.collectLatest { board ->
                when (board) {
                    ArticleBoardType.ALL,
                    ArticleBoardType.NORMAL,
                    ArticleBoardType.SCHOLARSHIP,
                    ArticleBoardType.SCHOOL,
                    ArticleBoardType.RECRUIT,
                    ArticleBoardType.IPP,
                    ArticleBoardType.STUDENT,
                    ArticleBoardType.KOIN -> {
                        val articleListNoticeFragment = ArticleListNoticeFragment()
                        val bundle = Bundle()
                        bundle.putInt("boardId", board.id)
                        articleListNoticeFragment.arguments = bundle
                        childFragmentManager.beginTransaction()
                            .replace(R.id.frame_layout_article_list, articleListNoticeFragment)
                            .commit()
                    }
                    ArticleBoardType.LOSTANDFOUND -> {
                        //TODO: Add LostAndFoundFragment
                    }
                }
            }
        }
    }

    private fun addCategoryTabs() {
        ArticleBoardType.entries.forEach {
            binding.tabLayoutArticleBoard.addTab(binding.tabLayoutArticleBoard.newTab().apply {
                id = View.generateViewId()
                text = getString(it.simpleKoreanName)
            })
        }
    }

    private fun collectData() {
        (requireActivity() as IProgressDialog).withLoading(viewLifecycleOwner, viewModel)
        viewLifecycleOwner.lifecycleScope.run {
            this.launch {
                repeatOnLifecycle(Lifecycle.State.STARTED) {
                    viewModel.currentBoard.collect { board ->
                        binding.tabLayoutArticleBoard.getTabAt(
                            ArticleBoardType.entries.indexOf(
                                board
                            )
                        )?.select()
                    }
                }
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        binding.tabLayoutArticleBoard.removeOnTabSelectedListener(onTabSelectedListener)
        _binding = null
    }
}