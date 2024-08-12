package `in`.koreatech.koin.ui.article

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.navigation.fragment.findNavController
import com.google.android.material.tabs.TabLayout
import dagger.hilt.android.AndroidEntryPoint
import `in`.koreatech.koin.R
import `in`.koreatech.koin.core.util.dataBinding
import `in`.koreatech.koin.databinding.FragmentArticleListBinding
import `in`.koreatech.koin.ui.article.adapter.ArticleAdapter
import `in`.koreatech.koin.ui.article.state.ArticleState
import `in`.koreatech.koin.ui.article.viewmodel.ArticleViewModel
import kotlinx.coroutines.launch

@AndroidEntryPoint
class ArticleListFragment : Fragment() {

    private val binding by dataBinding<FragmentArticleListBinding>()
    private val navController by lazy { findNavController() }
    private val viewModel by activityViewModels<ArticleViewModel>()

    private val articleAdapter = ArticleAdapter(BoardType.ALL, onClick = ::onArticleClicked)

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        initTabSelectedListener()
        viewLifecycleOwner.lifecycleScope.run {
            this.launch {
                repeatOnLifecycle(Lifecycle.State.STARTED) {
                    viewModel.currentBoard.collect { board ->
                        viewModel.fetchArticles(board)
                    }
                }
            }
            this.launch {
                repeatOnLifecycle(Lifecycle.State.STARTED) {
                    viewModel.articles.collect { articles ->
                        articleAdapter.submitList(articles)
                    }
                }
            }
        }
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        articleAdapter.submitList(viewModel.articles.value)
    }

    private fun initTabSelectedListener() {
        binding.tabLayoutArticleBoard.addOnTabSelectedListener(object : TabLayout.OnTabSelectedListener {
            override fun onTabSelected(tab: TabLayout.Tab?) {
                tab?.let {
                    viewModel.setCurrentBoard(BoardType.entries[it.position])
                }
            }
            override fun onTabUnselected(tab: TabLayout.Tab?) {}
            override fun onTabReselected(tab: TabLayout.Tab?) {}
        })
    }

    private fun onArticleClicked(article: ArticleState) {
        navController.navigate(
            R.id.action_articleListFragment_to_articleDetailFragment,
            Bundle().apply {
                putParcelable("article", article)
            }
        )
    }

    companion object {

        @JvmStatic
        fun newInstance() =
            ArticleListFragment().apply {
                arguments = Bundle().apply {

                }
            }
    }
}