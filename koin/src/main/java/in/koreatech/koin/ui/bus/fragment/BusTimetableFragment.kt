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

@AndroidEntryPoint
class BusTimetableFragment : DataBindingFragment<BusTimetableFragmentBinding>() {
    override val layoutId: Int = R.layout.bus_timetable_fragment

    private val busTimetableViewModel by viewModels<BusTimetableViewModel>()

    private val busTimetableViewPager2Adapter by lazy { BusTimetableViewPager2Adapter(requireActivity()) }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        initView()
        initViewModel()
    }

    private fun initView() {
        busTimetableViewModel.setBusType(BusType.Shuttle)

        binding.busTimetablePager.apply {
            offscreenPageLimit = 3
            isUserInputEnabled = false
            adapter = busTimetableViewPager2Adapter
        }

        binding.busTimetableBustypeShuttle.setOnClickListener {
            busTimetableViewModel.setBusType(BusType.Shuttle)
        }
        binding.busTimetableBustypeDaesung.setOnClickListener {
            busTimetableViewModel.setBusType(BusType.Express)
        }
        binding.busTimetableBustypeCity.setOnClickListener {
            busTimetableViewModel.setBusType(BusType.City)
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
}