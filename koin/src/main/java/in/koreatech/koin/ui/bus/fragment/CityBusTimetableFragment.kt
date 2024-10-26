package `in`.koreatech.koin.ui.bus.fragment

import android.os.Bundle
import android.view.View
import android.widget.ArrayAdapter
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import dagger.hilt.android.AndroidEntryPoint
import `in`.koreatech.koin.R
import `in`.koreatech.koin.core.analytics.EventAction
import `in`.koreatech.koin.core.analytics.EventLogger
import `in`.koreatech.koin.core.constant.AnalyticsConstant
import `in`.koreatech.koin.core.fragment.DataBindingFragment
import `in`.koreatech.koin.core.progressdialog.IProgressDialog
import `in`.koreatech.koin.databinding.LayoutCityBusTimetableBinding
import `in`.koreatech.koin.domain.model.bus.city.CityBusGeneralDestination
import `in`.koreatech.koin.domain.model.bus.city.CityBusNumber
import `in`.koreatech.koin.domain.model.bus.city.toggleSelection
import `in`.koreatech.koin.ui.bus.adpater.timetable.CityBusTimetableAdapter
import `in`.koreatech.koin.ui.bus.state.toCityBusTimetableUiItemList
import `in`.koreatech.koin.ui.bus.viewmodel.CityBusTimetableViewModel
import `in`.koreatech.koin.util.ext.observeLiveData
import `in`.koreatech.koin.util.ext.setOnItemSelectedListener
import `in`.koreatech.koin.util.ext.withLoading
import `in`.koreatech.koin.util.ext.withToastError

@AndroidEntryPoint
class CityBusTimetableFragment : DataBindingFragment<LayoutCityBusTimetableBinding>() {
    override val layoutId: Int = R.layout.layout_city_bus_timetable

    private val cityBusTimetableViewModel by viewModels<CityBusTimetableViewModel>()
    private val cityBusTimetableAdapter = CityBusTimetableAdapter()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        initView()
        initViewModel()
    }

    private fun initView() = with(binding) {
        recyclerView.apply {
            layoutManager = LinearLayoutManager(requireContext())
            adapter = cityBusTimetableAdapter
            itemAnimator = null
        }
        busTimetableCityCourseToggle.setOnCheckedChangeListener { _, isChecked ->
            val selection =
                if (isChecked) CityBusGeneralDestination.Terminal.toggleSelection else CityBusGeneralDestination.Beongchon.toggleSelection
            cityBusTimetableViewModel.setDestination(selection)
        }
        busTimetableCityBusNumberSpinner.adapter =
            ArrayAdapter(
                requireContext(),
                android.R.layout.simple_spinner_dropdown_item,
                CityBusNumber.entries.map { "${it.number}번" }
            )
        busTimetableCityBusNumberSpinner.setOnItemSelectedListener { _, _, position, _ ->
            cityBusTimetableViewModel.setBusNumber(position)
            EventLogger.logClickEvent(
                EventAction.CAMPUS,
                AnalyticsConstant.Label.BUS_TIMETABLE_AREA,
                busTimetableCityBusNumberSpinner.selectedItem.toString()
            )
        }
    }

    private fun initViewModel() = with(cityBusTimetableViewModel) {
        (requireActivity() as? IProgressDialog)?.withLoading(viewLifecycleOwner, this)

        withToastError(this@CityBusTimetableFragment, binding.root)

        observeLiveData(busDepartTimes) { list ->
            cityBusTimetableAdapter.submitList(list.toCityBusTimetableUiItemList())
            binding.recyclerView.smoothScrollToPosition(0)
        }

        observeLiveData(updatedAt) {
            cityBusTimetableAdapter.setUpdatedAt(it)
        }
    }
}