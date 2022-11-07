package `in`.koreatech.koin.ui.bus.fragment

import `in`.koreatech.koin.R
import `in`.koreatech.koin.core.activity.ActivityBase
import `in`.koreatech.koin.core.fragment.DataBindingFragment
import `in`.koreatech.koin.databinding.BusMainFragmentNewBinding
import `in`.koreatech.koin.domain.model.bus.timer.BusArrivalInfo
import `in`.koreatech.koin.domain.model.bus.BusNode
import `in`.koreatech.koin.ui.bus.adpater.BusRemainTimeAdapter
import `in`.koreatech.koin.ui.bus.state.toCityBusRemainTimeUiState
import `in`.koreatech.koin.ui.bus.state.toExpressBusRemainTimeUiState
import `in`.koreatech.koin.ui.bus.state.toShuttleBusRemainTimeUiState
import `in`.koreatech.koin.ui.bus.viewmodel.BusMainFragmentViewModel
import `in`.koreatech.koin.util.SnackbarUtil
import `in`.koreatech.koin.util.ext.observeLiveData
import `in`.koreatech.koin.util.ext.setOnItemSelectedListener
import `in`.koreatech.koin.util.ext.withLoading
import android.os.Bundle
import android.view.View
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.SimpleItemAnimator
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class BusMainFragmentNew : DataBindingFragment<BusMainFragmentNewBinding>() {
    override val layoutId: Int = R.layout.bus_main_fragment_new

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
        busDepartureSpinner.setOnItemSelectedListener { _, _, position, _ ->
            viewModel.setDeparture(position.busNodeSelection)
        }
        busArrivalSpinner.setOnItemSelectedListener { _, _, position, _ ->
            viewModel.setArrival(position.busNodeSelection)
        }
        recyclerView.apply {
            layoutManager = LinearLayoutManager(requireContext())
            adapter = busRemainTimeAdapter
            (itemAnimator as? SimpleItemAnimator)?.supportsChangeAnimations = false
        }
    }

    private fun initViewModel() = with(viewModel) {
        (activity as ActivityBase).withLoading(viewLifecycleOwner, this)

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

            observeLiveData(getBusTimerError) {
                SnackbarUtil.makeShortSnackbar(binding.root, it)
            }
        }
    }
}

val BusNode.spinnerSelection
    get() = when (this) {
        BusNode.Koreatech -> 0
        BusNode.Station -> 2
        BusNode.Terminal -> 1
    }

val Int.busNodeSelection
    get() = when (this) {
        0 -> BusNode.Koreatech
        2 -> BusNode.Station
        1 -> BusNode.Terminal
        else -> throw IllegalArgumentException("Not supported selection.")
    }