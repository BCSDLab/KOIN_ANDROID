package `in`.koreatech.koin.ui.bus.fragment

import `in`.koreatech.koin.R
import `in`.koreatech.koin.core.fragment.DataBindingFragment
import `in`.koreatech.koin.core.progressdialog.IProgressDialog
import `in`.koreatech.koin.databinding.LayoutCityBusTimetableBinding
import `in`.koreatech.koin.databinding.LayoutExpressBusTimetableBinding
import `in`.koreatech.koin.databinding.LayoutShuttleBusTimetableBinding
import `in`.koreatech.koin.ui.bus.adpater.timetable.CityBusTimetableAdapter
import `in`.koreatech.koin.ui.bus.adpater.timetable.ExpressBusTimetableAdapter
import `in`.koreatech.koin.ui.bus.adpater.timetable.ShuttleBusTimetableAdapter
import `in`.koreatech.koin.ui.bus.state.toCityBusTimetableUiItem
import `in`.koreatech.koin.ui.bus.state.toExpressBusTimetableUiItem
import `in`.koreatech.koin.ui.bus.state.toShuttleBusTimetableUiItem
import `in`.koreatech.koin.ui.bus.viewmodel.CityBusTimetableViewModel
import `in`.koreatech.koin.ui.bus.viewmodel.ExpressBusTimetableViewModel
import `in`.koreatech.koin.ui.bus.viewmodel.ShuttleBusTimetableViewModel
import `in`.koreatech.koin.util.SnackbarUtil
import `in`.koreatech.koin.util.ext.observeLiveData
import `in`.koreatech.koin.util.ext.setOnItemSelectedListener
import `in`.koreatech.koin.util.ext.withLoading
import android.os.Bundle
import android.view.View
import android.widget.ArrayAdapter
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import dagger.hilt.android.AndroidEntryPoint

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
        }
    }

    private fun initViewModel() = with(cityBusTimetableViewModel) {
        (requireActivity() as? IProgressDialog)?.withLoading(viewLifecycleOwner, this)

        observeLiveData(errorMessage) {
            SnackbarUtil.makeShortSnackbar(view, it)
        }

        observeLiveData(busTimetables) { list ->
            cityBusTimetableAdapter.submitList(
                list.map { it.toCityBusTimetableUiItem() }
            )
            binding.recyclerView.smoothScrollToPosition(0)
        }
    }
}