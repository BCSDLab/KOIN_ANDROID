package `in`.koreatech.koin.ui.bus.fragment

import `in`.koreatech.koin.R
import `in`.koreatech.koin.core.activity.ActivityBase
import `in`.koreatech.koin.core.fragment.DataBindingFragment
import `in`.koreatech.koin.databinding.BusMainFragmentBinding
import `in`.koreatech.koin.domain.model.bus.BusNode
import `in`.koreatech.koin.domain.model.bus.busNodeSelection
import `in`.koreatech.koin.domain.model.bus.spinnerSelection
import `in`.koreatech.koin.domain.model.bus.timer.BusArrivalInfo
import `in`.koreatech.koin.ui.bus.adpater.BusRemainTimeAdapter
import `in`.koreatech.koin.ui.bus.state.toCityBusRemainTimeUiState
import `in`.koreatech.koin.ui.bus.state.toExpressBusRemainTimeUiState
import `in`.koreatech.koin.ui.bus.state.toShuttleBusRemainTimeUiState
import `in`.koreatech.koin.ui.bus.viewmodel.BusMainFragmentViewModel
import `in`.koreatech.koin.util.ext.observeLiveData
import `in`.koreatech.koin.util.ext.setOnItemSelectedListener
import `in`.koreatech.koin.util.ext.withLoading
import `in`.koreatech.koin.util.ext.withToastError
import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import dagger.hilt.android.AndroidEntryPoint
import `in`.koreatech.koin.core.analytics.EventAction
import `in`.koreatech.koin.core.analytics.EventLogger
import `in`.koreatech.koin.core.constant.AnalyticsConstant
import `in`.koreatech.koin.core.util.dataBinding

@AndroidEntryPoint
class BusMainFragment : Fragment(R.layout.bus_main_fragment) {
    private val binding by dataBinding<BusMainFragmentBinding>()

    private val viewModel by viewModels<BusMainFragmentViewModel>()

    private val busRemainTimeAdapter = BusRemainTimeAdapter()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        initView()
        initViewModel()
    }

    private fun initView() = with(binding) {
        busDepartureSpinner.setSelection(BusNode.Koreatech.spinnerSelection)
        busArrivalSpinner.setSelection(BusNode.Terminal.spinnerSelection)
        busDepartureSpinner.setOnTouchListener { _, _ ->
            viewModel.isUserSelection = true
            busDepartureSpinner.performClick()
        }
        busDepartureSpinner.setOnItemSelectedListener { _, _, position, _ ->
            viewModel.setDeparture(position.busNodeSelection)
            if(viewModel.isUserSelection) {
                EventLogger.logClickEvent(
                    EventAction.CAMPUS,
                    AnalyticsConstant.Label.BUS_DEPARTURE,
                    resources.getStringArray(R.array.bus_place)[position]
                )
                viewModel.isUserSelection = false
            }
        }

        busArrivalSpinner.setOnTouchListener { _, _ ->
            viewModel.isUserSelection = true
            busArrivalSpinner.performClick()
        }
        busArrivalSpinner.setOnItemSelectedListener { _, _, position, _ ->
            viewModel.setArrival(position.busNodeSelection)
            if(viewModel.isUserSelection) {
                EventLogger.logClickEvent(
                    EventAction.CAMPUS,
                    AnalyticsConstant.Label.BUS_ARRIVAL,
                    resources.getStringArray(R.array.bus_place)[position]
                )
                viewModel.isUserSelection = false
            }
        }
        recyclerView.apply {
            layoutManager = LinearLayoutManager(requireContext())
            adapter = busRemainTimeAdapter
            itemAnimator = null
        }
    }

    private fun initViewModel() = with(viewModel) {
        (activity as ActivityBase).withLoading(viewLifecycleOwner, this)
        withToastError(this@BusMainFragment, binding.root)

        observeLiveData(departure) {
            binding.busDepartureSpinner.setSelection(it.spinnerSelection)
        }
        observeLiveData(arrival) {
            binding.busArrivalSpinner.setSelection(it.spinnerSelection)
        }

        observeLiveData(busTimer) { list ->
            val uiState = list.mapNotNull {
                when (it) {
                    is BusArrivalInfo.CityBusArrivalInfo -> it.toCityBusRemainTimeUiState(
                        requireContext(),
                        departure.value ?: BusNode.Koreatech,
                        arrival.value ?: BusNode.Terminal
                    )
                    is BusArrivalInfo.ExpressBusArrivalInfo -> it.toExpressBusRemainTimeUiState(
                        requireContext(),
                        departure.value ?: BusNode.Koreatech,
                        arrival.value ?: BusNode.Terminal
                    )
                    is BusArrivalInfo.ShuttleBusArrivalInfo -> it.toShuttleBusRemainTimeUiState(
                        requireContext(),
                        departure.value ?: BusNode.Koreatech,
                        arrival.value ?: BusNode.Terminal
                    )
                    else -> null
                }
            }
            busRemainTimeAdapter.submitList(uiState)
        }
    }
}