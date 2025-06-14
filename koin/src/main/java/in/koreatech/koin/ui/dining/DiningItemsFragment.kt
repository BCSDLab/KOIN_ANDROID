package `in`.koreatech.koin.ui.dining

import android.os.Bundle
import android.view.Gravity
import android.view.View
import android.view.ViewGroup.MarginLayoutParams
import android.widget.FrameLayout
import android.widget.ImageView
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.core.view.updatePadding
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.recyclerview.widget.ListAdapter
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
import `in`.koreatech.koin.core.abtest.ExperimentGroup
import `in`.koreatech.koin.core.analytics.AnalyticsConstant
import `in`.koreatech.koin.core.analytics.EventAction
import `in`.koreatech.koin.core.analytics.EventLogger
import `in`.koreatech.koin.core.onboarding.OnboardingManager
import `in`.koreatech.koin.core.onboarding.OnboardingType
import `in`.koreatech.koin.core.util.dataBinding
import `in`.koreatech.koin.databinding.FragmentDiningItemsBinding
import `in`.koreatech.koin.domain.model.dining.Dining
import `in`.koreatech.koin.domain.util.DiningUtil
import `in`.koreatech.koin.domain.util.TimeUtil
import `in`.koreatech.koin.domain.util.ext.arrange
import `in`.koreatech.koin.ui.dining.adapter.DiningAdapter
import `in`.koreatech.koin.ui.dining.adapter.DiningOriginalAdapter
import `in`.koreatech.koin.ui.dining.viewmodel.DiningViewModel
import javax.inject.Inject
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.launch

@AndroidEntryPoint
class DiningItemsFragment : Fragment(R.layout.fragment_dining_items) {
    private val binding by dataBinding<FragmentDiningItemsBinding>()
    private val viewModel by activityViewModels<DiningViewModel>()
    private val type by lazy { arguments?.getString(TYPE) }

    @Inject
    lateinit var onboardingManager: OnboardingManager
    private lateinit var diningAdapter: ListAdapter<Dining, RecyclerView.ViewHolder>

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        ViewCompat.setOnApplyWindowInsetsListener(binding.nestedScrollViewDiningItems) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.updatePadding(
                bottom = systemBars.bottom
            )
            WindowInsetsCompat.CONSUMED
        }

        viewLifecycleOwner.lifecycleScope.launch {
            viewLifecycleOwner.repeatOnLifecycle(Lifecycle.State.STARTED) {
                combine(
                    viewModel.abTestExperimentGroup,
                    viewModel.dining
                ) { experimentGroup, diningList ->
                    when (experimentGroup) {
                        ExperimentGroup.SHARE_ORIGINAL -> {
                            diningAdapter = DiningOriginalAdapter(::shareDining)
                            (binding.recyclerViewDiningType.layoutParams as MarginLayoutParams).setMargins(
                                0,
                                0,
                                0,
                                40
                            )
                        }
                        ExperimentGroup.SHARE_NEW -> {
                            diningAdapter = DiningAdapter(::shareDining)
                            (binding.recyclerViewDiningType.layoutParams as MarginLayoutParams).setMargins(
                                24,
                                20,
                                24,
                                40
                            )
                        }
                    }

                    binding.recyclerViewDiningType.apply {
                        adapter = diningAdapter
                    }

                    val filteredDiningList =
                        diningList
                            .filter { dining -> dining.type == type }
                            .arrange()
                            .filter { dining -> dining.menu.isNotEmpty() && dining.menu.first() != "미운영" }

                    diningAdapter.submitList(filteredDiningList) {
                        if (filteredDiningList.isNotEmpty() && diningAdapter is DiningAdapter) onListItemAttached()
                    }
                }.launchIn(this)
            }
        }
    }

    private fun onListItemAttached() {
        with(onboardingManager) {
            viewLifecycleOwner.showOnboardingIfNeeded(
                OnboardingType.DINING_SHARE
            ) {
                lifecycleScope.launch {
                    delay(200)
                    binding.recyclerViewDiningType.layoutManager?.findViewByPosition(0)?.let {
                        val bottomOffset = it.bottom
                        binding.frameLayoutDiningItems.addView(
                            ImageView(requireContext()).apply {
                                layoutParams =
                                    FrameLayout.LayoutParams(
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
        val executionParams =
            mapOf(
                "date" to dining.date,
                "type" to dining.type,
                "place" to dining.place
            )
        val link =
            Link(
                androidExecutionParams = executionParams,
                iosExecutionParams = executionParams
            )
        return FeedTemplate(
            content =
            Content(
                title = "ㅤ",
                imageUrl = dining.imageUrl,
                link = link
            ),
            itemContent =
            ItemContent(
                profileText = "${
                    if (TimeUtil.isToday(dining.date)) {
                        "오늘"
                    } else if (TimeUtil.isTomorrow(dining.date)) {
                        "내일"
                    } else {
                        TimeUtil.formatDateToKorean(dining.date)
                    }
                } ${DiningUtil.getKoreanName(dining.type)} 식단",
                items =
                listOf(
                    ItemInfo(
                        item = dining.place,
                        itemOp = dining.menu.joinToString(", ")
                    )
                )
            ),
            buttons =
            listOf(
                Button("코인에서 식단 전체보기", link)
            )
        )
    }

    companion object {
        private const val TYPE = "type"

        fun newInstance(type: String) = DiningItemsFragment().apply {
            arguments =
                Bundle().apply {
                    putString(TYPE, type)
                }
        }
    }
}
