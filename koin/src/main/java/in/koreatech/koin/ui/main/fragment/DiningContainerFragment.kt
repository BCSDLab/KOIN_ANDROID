package `in`.koreatech.koin.ui.main.fragment

import android.os.Bundle
import android.view.View
import androidx.core.content.ContextCompat
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import dagger.hilt.android.AndroidEntryPoint
import `in`.koreatech.koin.R
import `in`.koreatech.koin.core.analytics.EventAction
import `in`.koreatech.koin.core.analytics.EventLogger
import `in`.koreatech.koin.core.constant.AnalyticsConstant
import `in`.koreatech.koin.core.util.dataBinding
import `in`.koreatech.koin.data.util.localized
import `in`.koreatech.koin.databinding.FragmentDiningContainerBinding
import `in`.koreatech.koin.domain.model.dining.Dining
import `in`.koreatech.koin.domain.model.dining.DiningType
import `in`.koreatech.koin.domain.util.DiningUtil
import `in`.koreatech.koin.domain.util.ext.arrange
import `in`.koreatech.koin.domain.util.ext.typeFilter
import `in`.koreatech.koin.ui.main.activity.MainActivity
import `in`.koreatech.koin.ui.main.viewmodel.MainActivityViewModel
import `in`.koreatech.koin.util.ext.observeLiveData

@AndroidEntryPoint
class DiningContainerFragment : Fragment(R.layout.fragment_dining_container) {
    private val binding by dataBinding<FragmentDiningContainerBinding>()
    private val viewModel by activityViewModels<MainActivityViewModel>()
    private val place by lazy { arguments?.getString(PLACE) }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        initView()
        initViewModel()
    }

    private fun initView() = with(binding) {
        diningContainer.setOnClickListener {
            if (activity is MainActivity) {
                val mainActivity = activity as MainActivity
                mainActivity.callDrawerItem(R.id.navi_item_dining)
            }
            val type = DiningUtil.getCurrentType()
            EventLogger.logClickEvent(
                EventAction.CAMPUS,
                AnalyticsConstant.Label.MAIN_MENU_MOVEDETAILVIEW,
                if (type == DiningType.NextBreakfast) "내일 " else "오늘 " + requireContext().getString(R.string.navigation_item_dining)
            )
        }
    }

    private fun initViewModel() = with(viewModel) {
        observeLiveData(diningData) {
            updateDining(it, selectedPosition.value ?: 0)
        }

        observeLiveData(selectedPosition) { position ->
            diningData.value?.let { list ->
                updateDining(list, position)
            }
        }

        observeLiveData(selectedType) {
            binding.textViewDiningTime.text = it.localized(requireActivity())
        }
    }

    fun updateDining(originalList: List<Dining>, position: Int) {
        val diningType = DiningUtil.getCurrentType()
        val diningArranged = originalList
            .typeFilter(diningType)
            .arrange()

        updateMenu(originalList, diningArranged, position)
        updateStatus(position, diningArranged)
    }

    private fun updateMenu(originalList: List<Dining>, arrangedList: List<Dining>, position: Int) {
        if (originalList.isEmpty() || arrangedList[position].menu.isEmpty()) {
            binding.viewEmptyDining.emptyDiningListFrameLayout.isVisible = true
            return
        }
        binding.viewEmptyDining.emptyDiningListFrameLayout.isVisible = false

        val menus = listOf(binding.textViewDiningContainerMenuLeft, binding.textViewDiningContainerMenuRight)
        val limit = arrangedList[position].menu.size.coerceAtMost(5)
        menus.forEachIndexed { index, textView ->
            textView.text =  when (index) {
                0 -> arrangedList[position].menu.subList(0, limit).joinToString("\n")
                else -> arrangedList[position].menu.subList(limit, arrangedList[position].menu.size).joinToString("\n")
            }
        }
    }

    private fun updateStatus(position: Int, arrangedList: List<Dining>) {
        val isSoldOut = arrangedList[position].soldoutAt.isNotEmpty()
        val isChanged = arrangedList[position].changedAt.isNotEmpty()
        with(binding.textViewDiningStatus) {
            when {
                isSoldOut -> {
                    text = context.getString(R.string.dining_soldout)
                    setTextColor(ContextCompat.getColor(context, R.color.dining_soldout_text))
                    background =
                        ContextCompat.getDrawable(context, R.drawable.dining_soldout_fill_radius_4)
                }

                isChanged -> {
                    text = context.getString(R.string.dining_changed)
                    setTextColor(ContextCompat.getColor(context, R.color.dining_changed_text))
                    background =
                        ContextCompat.getDrawable(context, R.drawable.dining_changed_fill_radius_4)
                }

                else -> {
                    visibility = View.INVISIBLE
                }
            }
        }
    }

    companion object {
        private const val PLACE = "place"
        fun newInstance(place: String) =
            DiningContainerFragment().apply {
                arguments = Bundle().apply {
                    putString(PLACE, place)
                }
            }
    }
}