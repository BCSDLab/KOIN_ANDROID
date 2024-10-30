package `in`.koreatech.koin.ui.dining

import android.os.Bundle
import android.view.Gravity
import android.view.View
import android.widget.FrameLayout
import android.widget.ImageView
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.kakao.sdk.share.ShareClient
import com.kakao.sdk.template.model.Button
import com.kakao.sdk.template.model.Content
import com.kakao.sdk.template.model.FeedTemplate
import com.kakao.sdk.template.model.ItemContent
import com.kakao.sdk.template.model.ItemInfo
import com.kakao.sdk.template.model.Link
import dagger.hilt.android.AndroidEntryPoint
import `in`.koreatech.koin.R
import `in`.koreatech.koin.core.analytics.EventAction
import `in`.koreatech.koin.core.analytics.EventLogger
import `in`.koreatech.koin.core.constant.AnalyticsConstant
import `in`.koreatech.koin.core.onboarding.OnboardingManager
import `in`.koreatech.koin.core.onboarding.OnboardingType
import `in`.koreatech.koin.core.util.dataBinding
import `in`.koreatech.koin.databinding.FragmentDiningItemsBinding
import `in`.koreatech.koin.domain.model.dining.Dining
import `in`.koreatech.koin.domain.util.DiningUtil
import `in`.koreatech.koin.domain.util.TimeUtil
import `in`.koreatech.koin.domain.util.ext.arrange
import `in`.koreatech.koin.ui.dining.adapter.DiningAdapter
import `in`.koreatech.koin.ui.dining.viewmodel.DiningViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import javax.inject.Inject

@AndroidEntryPoint
class DiningItemsFragment : Fragment(R.layout.fragment_dining_items) {
    private val binding by dataBinding<FragmentDiningItemsBinding>()
    private val viewModel by activityViewModels<DiningViewModel>()
    private val type by lazy { arguments?.getString(TYPE) }

    @Inject
    lateinit var onboardingManager: OnboardingManager
    private val diningAdapter by lazy {
        DiningAdapter(onShareClick = ::shareDining)
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
                    diningAdapter.submitList(diningList.filter { dining -> dining.menu.isNotEmpty() && dining.menu.first() != "미운영" }) {
                        if(it.isNotEmpty())
                            onListItemAttached()
                    }
                }
            }
        }
    }

    private fun onListItemAttached() {
        with(onboardingManager) {
            viewLifecycleOwner.showOnboardingIfNeeded(
                OnboardingType.DINING_SHARE,
            ) {
                lifecycleScope.launch(Dispatchers.Default) {
                    delay(200)
                    withContext(Dispatchers.Main) {
                        binding.recyclerViewDiningType.layoutManager?.findViewByPosition(0)?.let {
                            val bottomOffset = it.bottom
                            binding.frameLayoutDiningItems.addView(
                                ImageView(requireContext()).apply {
                                    layoutParams = FrameLayout.LayoutParams(
                                        550,
                                        FrameLayout.LayoutParams.WRAP_CONTENT
                                    ).apply {
                                        gravity = Gravity.CENTER_HORIZONTAL
                                    }

                                    translationY = bottomOffset.toFloat() - 90f
                                    setOnClickListener {
                                        binding.frameLayoutDiningItems.removeView(this)
                                    }
                                    Glide.with(requireContext())
                                        .load(R.drawable.tooltip_share)
                                        .into(this)
                                }
                            )
                        }
                    }
                }
            }
        }
    }

    private fun shareDining(dining: Dining) {
        EventLogger.logClickEvent(
            EventAction.CAMPUS,
            AnalyticsConstant.Label.MENU_SHARE,
            "공유하기"
        )
        val messageTemplate = createFeedMessageTemplate(dining)

        if (ShareClient.instance.isKakaoTalkSharingAvailable(requireContext())) {
            ShareClient.instance.shareDefault(
                requireContext(),
                messageTemplate
            ) { sharingResult, error ->
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
                title = "ㅤ",
                imageUrl = dining.imageUrl,
                link = link
            ),
            itemContent = ItemContent(
                profileText = "${
                    if (TimeUtil.isToday(dining.date)) "오늘" else if (TimeUtil.isTomorrow(dining.date)) "내일" else
                        TimeUtil.formatDateToKorean(dining.date)
                } ${DiningUtil.getKoreanName(dining.type)} 식단",
                items = listOf(
                    ItemInfo(
                        item = dining.place,
                        itemOp = dining.menu.joinToString(", ")
                    )
                )
            ),
            buttons = listOf(
                Button("코인에서 식단 전체보기", link)
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