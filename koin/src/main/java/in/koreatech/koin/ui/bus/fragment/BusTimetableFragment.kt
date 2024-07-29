package `in`.koreatech.koin.ui.bus.fragment

import `in`.koreatech.koin.R
import `in`.koreatech.koin.core.fragment.DataBindingFragment
import `in`.koreatech.koin.databinding.BusTimetableFragmentBinding
import `in`.koreatech.koin.domain.model.bus.BusType
import `in`.koreatech.koin.ui.bus.adpater.timetable.pager.BusTimetableViewPager2Adapter
import `in`.koreatech.koin.ui.bus.viewmodel.BusTimetableViewModel
import `in`.koreatech.koin.util.ext.observeLiveData
import android.os.Bundle
import android.view.View
import androidx.fragment.app.commit
import androidx.fragment.app.viewModels
import dagger.hilt.android.AndroidEntryPoint
import `in`.koreatech.koin.core.analytics.EventLogger
import `in`.koreatech.koin.core.constant.AnalyticsConstant
import `in`.koreatech.koin.domain.model.bus.toBusType
import `in`.koreatech.koin.ui.main.fragment.DiningContainerFragment

@AndroidEntryPoint
class BusTimetableFragment : DataBindingFragment<BusTimetableFragmentBinding>() {
    override val layoutId: Int = R.layout.bus_timetable_fragment

    private val busTimetableViewModel by viewModels<BusTimetableViewModel>()

    private val busTimetableViewPager2Adapter by lazy { BusTimetableViewPager2Adapter(requireActivity()) }

    private val busType by lazy { arguments?.getString(TYPE)?.toBusType() ?: BusType.Shuttle }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        initView()
        initViewModel()
    }

    private fun initView() {
        busTimetableViewModel.setBusType(busType)

        binding.busTimetablePager.apply {
            offscreenPageLimit = 3
            isUserInputEnabled = false
            adapter = busTimetableViewPager2Adapter
        }

        binding.busTimetableBustypeShuttle.setOnClickListener {
            busTimetableViewModel.setBusType(BusType.Shuttle)
            EventLogger.logClickEvent(
                AnalyticsConstant.Domain.CAMPUS,
                AnalyticsConstant.Label.BUS_TIMETABLE,
                getString(R.string.bus_name_school_shuttle)
            )
        }
        binding.busTimetableBustypeDaesung.setOnClickListener {
            busTimetableViewModel.setBusType(BusType.Express)
            EventLogger.logClickEvent(
                AnalyticsConstant.Domain.CAMPUS,
                AnalyticsConstant.Label.BUS_TIMETABLE,
                getString(R.string.bus_name_express)
            )
        }
        binding.busTimetableBustypeCity.setOnClickListener {
            busTimetableViewModel.setBusType(BusType.City)
            EventLogger.logClickEvent(
                AnalyticsConstant.Domain.CAMPUS,
                AnalyticsConstant.Label.BUS_TIMETABLE,
                getString(R.string.bus_name_city)
            )
        }
    }

    private fun initViewModel() = with(busTimetableViewModel) {
        observeLiveData(showingBusType) {
            when(it) {
                BusType.City -> switchCityBusTimetable()
                BusType.Express -> switchExpressBusTimetable()
                BusType.Shuttle -> switchShuttleBusTimetable()
                else -> Unit
            }
        }
    }

    private fun switchShuttleBusTimetable() = with(binding) {
        busTimetablePager.setCurrentItem(0, true)
        busTimetableBustypeShuttle.alpha = 1f
        busTimetableBustypeDaesung.alpha = 0.5f
        busTimetableBustypeCity.alpha = 0.5f
    }

    private fun switchExpressBusTimetable() = with(binding) {
        busTimetablePager.setCurrentItem(1, true)
        busTimetableBustypeShuttle.alpha = 0.5f
        busTimetableBustypeDaesung.alpha = 1f
        busTimetableBustypeCity.alpha = 0.5f
    }

    private fun switchCityBusTimetable() = with(binding) {
        busTimetablePager.setCurrentItem(2, true)
        busTimetableBustypeShuttle.alpha = 0.5f
        busTimetableBustypeDaesung.alpha = 0.5f
        busTimetableBustypeCity.alpha = 1f
    }

    companion object {
        private const val TYPE = "type"
        fun newInstance(type: String) =
            BusTimetableFragment().apply {
                arguments = Bundle().apply {
                    putString(TYPE, type)
                }
            }
    }
}