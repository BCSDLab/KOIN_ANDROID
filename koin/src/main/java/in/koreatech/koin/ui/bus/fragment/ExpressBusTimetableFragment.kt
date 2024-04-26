package `in`.koreatech.koin.ui.bus.fragment

import `in`.koreatech.koin.R
import `in`.koreatech.koin.core.fragment.DataBindingFragment
import `in`.koreatech.koin.core.progressdialog.IProgressDialog
import `in`.koreatech.koin.databinding.LayoutExpressBusTimetableBinding
import `in`.koreatech.koin.databinding.LayoutShuttleBusTimetableBinding
import `in`.koreatech.koin.ui.bus.adpater.timetable.ExpressBusTimetableAdapter
import `in`.koreatech.koin.ui.bus.adpater.timetable.ShuttleBusTimetableAdapter
import `in`.koreatech.koin.ui.bus.state.toExpressBusTimetableUiItem
import `in`.koreatech.koin.ui.bus.state.toShuttleBusTimetableUiItem
import `in`.koreatech.koin.ui.bus.viewmodel.ExpressBusTimetableViewModel
import `in`.koreatech.koin.ui.bus.viewmodel.ShuttleBusTimetableViewModel
import `in`.koreatech.koin.util.SnackbarUtil
import `in`.koreatech.koin.util.ext.observeLiveData
import `in`.koreatech.koin.util.ext.setOnItemSelectedListener
import `in`.koreatech.koin.util.ext.withLoading
import `in`.koreatech.koin.util.ext.withToastError
import android.os.Bundle
import android.view.View
import android.widget.ArrayAdapter
import androidx.core.view.isVisible
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import dagger.hilt.android.AndroidEntryPoint
import `in`.koreatech.koin.core.analytics.EventLogger
import `in`.koreatech.koin.core.constant.AnalyticsConstant

@AndroidEntryPoint
class ExpressBusTimetableFragment : DataBindingFragment<LayoutExpressBusTimetableBinding>() {
    override val layoutId: Int = R.layout.layout_express_bus_timetable

    private val expressBusTimetableViewModel by viewModels<ExpressBusTimetableViewModel>()
    private val expressBusTimetableAdapter = ExpressBusTimetableAdapter()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        initView()
        initViewModel()
    }

    private fun initView() = with(binding) {
        recyclerView.apply {
            layoutManager = LinearLayoutManager(requireContext())
            adapter = expressBusTimetableAdapter
            itemAnimator = null
        }

        busTimetableCoursesSpinner.setOnItemSelectedListener { _, _, position, _ ->
            expressBusTimetableViewModel.setCoursePosition(position)
            EventLogger.logClickEvent(
                AnalyticsConstant.Domain.CAMPUS,
                AnalyticsConstant.Label.BUS_TIMETABLE_EXPRESS,
                busTimetableCoursesSpinner.selectedItem.toString()
            )
        }
    }

    private fun initViewModel() = with(expressBusTimetableViewModel) {
        (requireActivity() as? IProgressDialog)?.withLoading(viewLifecycleOwner, this)

        withToastError(this@ExpressBusTimetableFragment, binding.root)

        observeLiveData(busCoursesString) { courses ->
            if(courses.isNullOrEmpty()) {
                binding.busTimetableCoursesSpinner.isVisible = false
            } else {
                binding.busTimetableCoursesSpinner.isVisible = true
                binding.busTimetableCoursesSpinner.adapter =
                    ArrayAdapter(
                        requireContext(),
                        android.R.layout.simple_spinner_dropdown_item,
                        courses
                    )
            }
        }

        observeLiveData(selectedCoursesPosition) {
            binding.busTimetableCoursesSpinner.setSelection(it, true)
        }

        observeLiveData(busTimetables) { list ->
            expressBusTimetableAdapter.submitList(
                list.map { it.toExpressBusTimetableUiItem(requireContext()) }
            )
            binding.recyclerView.smoothScrollToPosition(0)
        }

        observeLiveData(updatedAt) {
            expressBusTimetableAdapter.setUpdatedAt(it)
        }
    }
}