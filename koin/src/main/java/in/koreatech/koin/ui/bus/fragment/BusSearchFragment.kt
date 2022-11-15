package `in`.koreatech.koin.ui.bus.fragment

import `in`.koreatech.koin.R
import `in`.koreatech.koin.core.fragment.DataBindingFragment
import `in`.koreatech.koin.core.progressdialog.IProgressDialog
import `in`.koreatech.koin.databinding.BusTimetableSearchFragmentBinding
import `in`.koreatech.koin.domain.model.bus.busNodeSelection
import `in`.koreatech.koin.domain.model.bus.spinnerSelection
import `in`.koreatech.koin.ui.bus.dialog.BusSearchResultDialog
import `in`.koreatech.koin.ui.bus.state.BusSearchResultItem
import `in`.koreatech.koin.ui.bus.state.toBusSearchResultItem
import `in`.koreatech.koin.ui.bus.viewmodel.BusSearchViewModel
import `in`.koreatech.koin.util.ext.observeLiveData
import `in`.koreatech.koin.util.ext.setOnItemSelectedListener
import `in`.koreatech.koin.util.ext.withLoading
import `in`.koreatech.koin.util.ext.withToastError
import android.app.DatePickerDialog
import android.os.Bundle
import android.view.View
import androidx.fragment.app.commit
import androidx.fragment.app.viewModels
import dagger.hilt.android.AndroidEntryPoint
import java.time.LocalDate
import java.time.LocalDateTime
import java.time.LocalTime
import java.time.format.DateTimeFormatter

@AndroidEntryPoint
class BusSearchFragment : DataBindingFragment<BusTimetableSearchFragmentBinding>() {
    override val layoutId: Int = R.layout.bus_timetable_search_fragment

    private val busSearchViewModel by viewModels<BusSearchViewModel>()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        initView()
        initViewModel()
    }

    private fun initView() = with(binding) {
        busSearchBusDepartureSpinner.setOnItemSelectedListener { _, _, position, _ ->
            busSearchViewModel.setDeparture(position.busNodeSelection)
        }

        busSearchBusArrivalSpinner.setOnItemSelectedListener { _, _, position, _ ->
            busSearchViewModel.setArrival(position.busNodeSelection)
        }

        busTimetableSearchDateImageButton.setOnClickListener {
            showDatePickerDialog()
        }

        busSearchTimePicker.setOnTimeChangedListener { _, hourOfDay, minute ->
            busSearchViewModel.setSelectedTime(LocalTime.of(hourOfDay, minute))
        }

        busTimetableSearchFragmentSearchButton.setOnClickListener {
            busSearchViewModel.search()
        }
    }

    private fun initViewModel() = with(busSearchViewModel) {
        (requireActivity() as? IProgressDialog)?.withLoading(viewLifecycleOwner, this)
        withToastError(this@BusSearchFragment, binding.root)

        observeLiveData(selectedDate) {
            binding.busTimetableSearchDateTextview.text = it.format(
                DateTimeFormatter.ofPattern("MM월 dd일 (E)")
            )

            binding.busTimetableSearchFragmentInformationTextview.text =
                LocalDateTime.of(selectedDate.value, selectedTime.value).format(
                    DateTimeFormatter.ofPattern("yyyy / MM / dd  a hh시 mm분")
                )
        }

        observeLiveData(selectedTime) {
            binding.busTimetableSearchFragmentInformationTextview.text =
                LocalDateTime.of(selectedDate.value, selectedTime.value).format(
                    DateTimeFormatter.ofPattern("yyyy / MM / dd  a hh시 mm분")
                )
        }

        observeLiveData(departure) {
            binding.busSearchBusDepartureSpinner.setSelection(it.spinnerSelection)
        }

        observeLiveData(arrival) {
            binding.busSearchBusArrivalSpinner.setSelection(it.spinnerSelection)
        }

        observeLiveData(busSearchResult) {
            showSearchResultDialog(it.map { it.toBusSearchResultItem() })
        }
    }

    private fun showDatePickerDialog() {
        val (year, month, dayOfMonth) = (busSearchViewModel.selectedDate.value
            ?: LocalDate.now()).let {
            Triple(it.year, it.monthValue - 1, it.dayOfMonth)
        }
        DatePickerDialog(requireContext()).apply {
            updateDate(year, month, dayOfMonth)
            setOnDateSetListener { _, year, month, dayOfMonth ->
                busSearchViewModel.setSelectedDate(
                    LocalDate.of(year, month + 1, dayOfMonth)
                )
            }
        }.show()
    }

    private fun showSearchResultDialog(list: List<BusSearchResultItem>) {
        with(requireActivity().supportFragmentManager) {
            val prev = findFragmentByTag(SEARCH_RESULT_DIALOG)
            if (prev != null) commit {
                remove(prev)
            }

            val busSearchResultDialog = BusSearchResultDialog()
            busSearchResultDialog.show(this, SEARCH_RESULT_DIALOG)
            busSearchResultDialog.submitList(list)
        }
    }

    companion object {
        const val SEARCH_RESULT_DIALOG = "SEARCH_RESULT_DIALOG"
    }
}