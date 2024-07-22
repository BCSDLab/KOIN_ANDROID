package `in`.koreatech.koin.ui.dining

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import com.kakao.sdk.share.ShareClient
import com.kakao.sdk.template.model.Button
import com.kakao.sdk.template.model.Content
import com.kakao.sdk.template.model.FeedTemplate
import com.kakao.sdk.template.model.Link
import dagger.hilt.android.AndroidEntryPoint
import `in`.koreatech.koin.R
import `in`.koreatech.koin.core.util.dataBinding
import `in`.koreatech.koin.databinding.FragmentDiningItemsBinding
import `in`.koreatech.koin.domain.model.dining.Dining
import `in`.koreatech.koin.domain.util.ext.arrange
import `in`.koreatech.koin.ui.dining.adapter.DiningAdapter
import `in`.koreatech.koin.ui.dining.viewmodel.DiningViewModel
import kotlinx.coroutines.async
import kotlinx.coroutines.launch

@AndroidEntryPoint
class DiningItemsFragment : Fragment(R.layout.fragment_dining_items) {
    private val binding by dataBinding<FragmentDiningItemsBinding>()
    private val viewModel by activityViewModels<DiningViewModel>()
    private val type by lazy { arguments?.getString(TYPE) }
    private val diningAdapter by lazy { DiningAdapter(
        onLikeClickResult = { dining ->
            viewLifecycleOwner.lifecycleScope.async {
                viewModel.toggleLikeDining(dining)
            }.await()
        },
        onShareClick = ::shareDining,
        coroutineScope = viewLifecycleOwner.lifecycleScope
    ) }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        viewModel.getUser()
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.recyclerViewDiningType.apply {
            adapter = diningAdapter
        }
        viewLifecycleOwner.lifecycleScope.launch {
            viewLifecycleOwner.repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel.dining.collect {
                    val diningList = it.filter { dining -> dining.type == type }.arrange()
                    diningAdapter.submitList( diningList.filter { dining -> dining.menu.isNotEmpty() && dining.menu.first() != "미운영" } )
                }
            }
        }
    }

    private fun shareDining(dining: Dining) {
        val messageTemplate = createFeedMessageTemplate(dining)

        if(ShareClient.instance.isKakaoTalkSharingAvailable(requireContext())) {
            ShareClient.instance.shareDefault(requireContext(), messageTemplate) { sharingResult, error ->
                error?.printStackTrace()
                sharingResult?.let {
                    requireContext().startActivity(it.intent)
                }
            }
        }
    }

    private fun createFeedMessageTemplate(dining: Dining): FeedTemplate {
        val executionParams = mapOf(
            "date" to dining.date,
            "type" to dining.type,
            "place" to dining.place
        )
        val link = Link(
            androidExecutionParams = executionParams,
            iosExecutionParams = executionParams
        )
        return FeedTemplate(
            content = Content(
                title = "오늘의 점심 메뉴",
                description = dining.menu.joinToString(", "),
                imageUrl = dining.imageUrl,
                link = link
            ),
            buttons = listOf(
                Button("다른 사진 보러가기", link)
            )
        )
    }

    companion object {
        private const val TYPE = "type"
        fun newInstance(type: String) = DiningItemsFragment().apply {
            arguments = Bundle().apply {
                putString(TYPE, type)
            }
        }
    }
}