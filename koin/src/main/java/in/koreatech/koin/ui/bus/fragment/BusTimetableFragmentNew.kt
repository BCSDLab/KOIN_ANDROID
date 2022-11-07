package `in`.koreatech.koin.ui.bus.fragment

import `in`.koreatech.koin.R
import `in`.koreatech.koin.core.fragment.DataBindingFragment
import `in`.koreatech.koin.databinding.BusTimetableFragmentBinding
import `in`.koreatech.koin.domain.model.bus.BusType
import `in`.koreatech.koin.ui.bus.viewmodel.BusTimetableViewModel
import `in`.koreatech.koin.util.ext.observeLiveData
import android.os.Bundle
import android.view.View
import androidx.fragment.app.commit
import androidx.fragment.app.viewModels
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class BusTimetableFragmentNew : DataBindingFragment<BusTimetableFragmentBinding>() {
    override val layoutId: Int = R.layout.bus_timetable_fragment

    private val busTimetableViewModel by viewModels<BusTimetableViewModel>()

    private val shuttleBusTimetableFragment = ShuttleBusTimetableFragment()
    private val expressBusTimetableFragment = ExpressBusTimetableFragment()
    private val cityBusTimetableFragment = CityBusTimetableFragment()


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        initView()
        initViewModel()
    }

    private fun initView() {
        busTimetableViewModel.setBusType(BusType.Shuttle)

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
            }
        }
    }

    private fun switchShuttleBusTimetable() {
        requireActivity().supportFragmentManager.commit {
            replace(R.id.bus_timetable_fragment, shuttleBusTimetableFragment)
        }
    }

    private fun switchExpressBusTimetable() {
        requireActivity().supportFragmentManager.commit {
            replace(R.id.bus_timetable_fragment, expressBusTimetableFragment)
        }
    }

    private fun switchCityBusTimetable() {
        requireActivity().supportFragmentManager.commit {
            replace(R.id.bus_timetable_fragment, cityBusTimetableFragment)
        }
    }
}