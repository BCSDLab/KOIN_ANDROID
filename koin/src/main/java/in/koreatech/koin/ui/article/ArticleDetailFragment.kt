package `in`.koreatech.koin.ui.article

import android.app.DownloadManager
import android.graphics.Rect
import android.net.Uri
import android.os.Bundle
import android.os.Environment
import android.text.TextUtils
import android.view.View
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.RecyclerView
import dagger.hilt.android.AndroidEntryPoint
import `in`.koreatech.koin.R
import `in`.koreatech.koin.core.analytics.EventAction
import `in`.koreatech.koin.core.analytics.EventLogger
import `in`.koreatech.koin.core.constant.AnalyticsConstant
import `in`.koreatech.koin.core.dialog.ImageZoomableDialog
import `in`.koreatech.koin.core.download.FileDownloadManager
import `in`.koreatech.koin.core.progressdialog.IProgressDialog
import `in`.koreatech.koin.core.toast.ToastUtil
import `in`.koreatech.koin.core.util.dataBinding
import `in`.koreatech.koin.core.webview.loadKoreatechHtml
import `in`.koreatech.koin.core.webview.setOnImageClickListener
import `in`.koreatech.koin.databinding.FragmentArticleDetailBinding
import `in`.koreatech.koin.domain.util.DateFormatUtil
import `in`.koreatech.koin.domain.util.TimeUtil
import `in`.koreatech.koin.ui.article.adapter.AttachmentAdapter
import `in`.koreatech.koin.ui.article.adapter.HotArticleAdapter
import `in`.koreatech.koin.ui.article.state.ArticleHeaderState
import `in`.koreatech.koin.ui.article.state.ArticleState
import `in`.koreatech.koin.ui.article.state.AttachmentState
import `in`.koreatech.koin.ui.article.viewmodel.ArticleDetailViewModel
import `in`.koreatech.koin.util.ext.withLoading
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import javax.inject.Inject

@AndroidEntryPoint
class ArticleDetailFragment : Fragment(R.layout.fragment_article_detail) {

    @Inject
    lateinit var articleDetailViewModelFactory: ArticleDetailViewModel.Factory
    private val navController by lazy { findNavController() }

    private val binding: FragmentArticleDetailBinding by dataBinding()

    private val attachmentAdapter = AttachmentAdapter(::onAttachmentClick, ::onAttachmentLongClick)
    private val hotArticleAdapter = HotArticleAdapter(::onHotArticleClick)

    private val attachmentDownloadManager: FileDownloadManager by lazy {
        FileDownloadManager(viewLifecycleOwner, requireContext()).apply {
            setOnDownloadSuccessListener {
                ToastUtil.getInstance().makeShort(getString(R.string.download_complete))
            }
        }
    }

    private val viewModel: ArticleDetailViewModel by viewModels {
        ArticleDetailViewModel.provideFactory(
            articleDetailViewModelFactory,
            requireArguments().getInt(ARTICLE_ID),
            requireArguments().getInt(NAVIGATED_BOARD_ID)
        )
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        (requireActivity() as IProgressDialog).withLoading(viewLifecycleOwner, viewModel)
        binding.htmlView.setOnPreDrawListener { viewModel.setIsLoading(true) }
        binding.htmlView.setOnPostDrawListener { viewModel.setIsLoading(false) }
        initArticle()
        initAttachmentAdapter()
        initHotArticles()
        initButtonClickListeners()
    }

    private fun initArticle() {
        viewLifecycleOwner.lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel.article.collectLatest {
                    setHeader(it)
                    setContent(it)
                    setNavigateArticleButtonVisibility(it)

                    if (it.attachments.isEmpty())
                        binding.groupAttachment.visibility = View.GONE
                    else {
                        binding.groupAttachment.visibility = View.VISIBLE
                        attachmentAdapter.submitList(it.attachments)
                    }
                }
            }
        }
    }

    private fun initAttachmentAdapter() {
        binding.recyclerViewAttachments.adapter = attachmentAdapter
        binding.recyclerViewAttachments.addItemDecoration(object : RecyclerView.ItemDecoration(){
            override fun getItemOffsets(outRect: Rect, view: View, parent: RecyclerView, state: RecyclerView.State) {
                val offset = 25
                val count = state.itemCount

                val position = parent.getChildAdapterPosition(view)
                if (position != count - 1) {
                    outRect.bottom = offset
                }
            }
        })
    }

    private fun initHotArticles() {
        binding.recyclerViewHotArticles.adapter = hotArticleAdapter
        viewLifecycleOwner.lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel.hotArticles.collect {
                    hotArticleAdapter.submitList(it)
                }
            }
        }
    }

    private fun initButtonClickListeners() {
        binding.buttonToList.setOnClickListener {
            EventLogger.logClickEvent(
                EventAction.CAMPUS,
                AnalyticsConstant.Label.INVENTORY,
                getString(R.string.list)
            )
            navController.popBackStack(R.id.articleListFragment, false)
        }
        binding.buttonToPrevArticle.setOnClickListener {
            navController.navigate(
                R.id.action_articleDetailFragment_to_articleDetailFragment,
                Bundle().apply {
                    putInt(ARTICLE_ID, viewModel.article.value.prevArticleId!!)
                    putInt(NAVIGATED_BOARD_ID, viewModel.navigatedBoardId)
                }
            )
        }
        binding.buttonToNextArticle.setOnClickListener {
            navController.navigate(
                R.id.action_articleDetailFragment_to_articleDetailFragment,
                Bundle().apply {
                    putInt(ARTICLE_ID, viewModel.article.value.nextArticleId!!)
                    putInt(NAVIGATED_BOARD_ID, viewModel.navigatedBoardId)
                }
            )
        }
    }

    private fun setHeader(article: ArticleState) {
        binding.articleHeader.apply {
            textViewArticleBoardName.text = getString(article.header.board.koreanName)
            textViewArticleTitle.text = article.header.title
            textViewArticleAuthor.text = article.header.author
            try {
                textViewArticleDate.text = TextUtils.concat(
                    DateFormatUtil.getSimpleMonthAndDay(article.header.registeredAt),
                    " ",
                    DateFormatUtil.getDayOfWeek(TimeUtil.stringToDateYYYYMMDD(article.header.registeredAt))
                )
            } catch(_: Exception) { }
            textViewArticleViewCount.text = article.header.viewCount.toString()
        }
    }

    private fun setContent(article: ArticleState) {
        //binding.htmlView.setHtml(article.content)
        binding.webView.apply {
            setOnImageClickListener(requireActivity()) {
                val dialog = ImageZoomableDialog(requireActivity(), it).apply {
                    initialScale = 0.94f
                    cancelableOnTouchOutside = false
                }
                dialog.show()
            }
        }.loadKoreatechHtml(requireContext(), article.content)
    }

    private fun setNavigateArticleButtonVisibility(article: ArticleState) {
        // binding.buttonToPrevArticle.visibility = if (article.prevArticleId == null) View.INVISIBLE else View.VISIBLE
        // binding.buttonToNextArticle.visibility = if (article.nextArticleId == null) View.INVISIBLE else View.VISIBLE // TODO 잠시 배포에 포함 X
        binding.buttonToPrevArticle.visibility = View.INVISIBLE
        binding.buttonToNextArticle.visibility = View.INVISIBLE
    }

    private fun onAttachmentClick(attachment: AttachmentState) {
        ToastUtil.getInstance().makeShort(getString(R.string.start_download))
        attachmentDownloadManager.download(DownloadManager.Request(Uri.parse(attachment.url))
            .setDestinationInExternalPublicDir(Environment.DIRECTORY_DOWNLOADS, attachment.name)
            .setTitle(attachment.name)
            .setDescription(getString(R.string.downloading))
            .setNotificationVisibility(DownloadManager.Request.VISIBILITY_VISIBLE_NOTIFY_COMPLETED)
            .setAllowedOverMetered(true)
            .setAllowedOverRoaming(true))
    }

    private fun onAttachmentLongClick(attachment: AttachmentState) {
        ToastUtil.getInstance().makeShort(attachment.name)
    }

    private fun onHotArticleClick(article: ArticleHeaderState) {
        EventLogger.logClickEvent(
            EventAction.CAMPUS,
            AnalyticsConstant.Label.POPULAR_NOTICE,
            article.title
        )
        navController.navigate(
            R.id.action_articleDetailFragment_to_articleDetailFragment,
            Bundle().apply {
                putInt(ARTICLE_ID, article.id)
                putInt(NAVIGATED_BOARD_ID, viewModel.navigatedBoardId)
            }
        )
    }

    companion object {
        const val ARTICLE_ID = "article_id"
        const val NAVIGATED_BOARD_ID = "navigated_board_id"
    }
}