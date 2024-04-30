package `in`.koreatech.koin.ui.bus.fragment

import android.os.Bundle
import android.view.View
import android.widget.ArrayAdapter
import androidx.core.view.isVisible
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import dagger.hilt.android.AndroidEntryPoint
import `in`.koreatech.koin.R
import `in`.koreatech.koin.core.analytics.EventLogger
import `in`.koreatech.koin.core.constant.AnalyticsConstant
import `in`.koreatech.koin.core.fragment.DataBindingFragment
import `in`.koreatech.koin.core.progressdialog.IProgressDialog
import `in`.koreatech.koin.databinding.LayoutShuttleBusTimetableBinding
import `in`.koreatech.koin.ui.bus.adpater.timetable.ShuttleBusTimetableAdapter
import `in`.koreatech.koin.ui.bus.state.toShuttleBusTimetableUiItem
import `in`.koreatech.koin.ui.bus.viewmodel.ShuttleBusTimetableViewModel
import `in`.koreatech.koin.util.ext.observeLiveData
import `in`.koreatech.koin.util.ext.setOnItemSelectedListener
import `in`.koreatech.koin.util.ext.withLoading
import `in`.koreatech.koin.util.ext.withToastError

@AndroidEntryPoint
class ShuttleBusTimetableFragment : DataBindingFragment<LayoutShuttleBusTimetableBinding>() {
    override val layoutId: Int = R.layout.layout_shuttle_bus_timetable

    private val shuttleBusTimetableViewModel by viewModels<ShuttleBusTimetableViewModel>()
    private val shuttleBusTimetableAdapter = ShuttleBusTimetableAdapter()
    private var isCourseInitialization = true
    private var isRouteInitialization = true

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        initView()
        initViewModel()
    }

    private fun initView() = with(binding) {
        recyclerView.apply {
            layoutManager = LinearLayoutManager(requireContext())
            adapter = shuttleBusTimetableAdapter
            itemAnimator = null
        }

        busTimetableCoursesSpinner.setOnItemSelectedListener { _, _, position, _ ->
            shuttleBusTimetableViewModel.setCoursePosition(position)
            if (isCourseInitialization) {
                isCourseInitialization = false
                return@setOnItemSelectedListener
            }
            EventLogger.logClickEvent(
                AnalyticsConstant.Domain.CAMPUS,
                AnalyticsConstant.Label.BUS_TIMETABLE_AREA,
                busTimetableCoursesSpinner.selectedItem.toString()
            )
        }

        busTimetableRoutesSpinner.setOnItemSelectedListener { _, _, position, _ ->
            shuttleBusTimetableViewModel.setRoutePosition(position)
            if (isRouteInitialization) {
                isRouteInitialization = false
                return@setOnItemSelectedListener
            }
            EventLogger.logClickEvent(
                AnalyticsConstant.Domain.CAMPUS,
                AnalyticsConstant.Label.BUS_TIMETABLE_TIME,
                busTimetableRoutesSpinner.selectedItem.toString()
            )
        }
    }

    private fun initViewModel() = with(shuttleBusTimetableViewModel) {
        (requireActivity() as? IProgressDialog)?.withLoading(viewLifecycleOwner, this)

        withToastError(this@ShuttleBusTimetableFragment, binding.root)

        observeLiveData(busCoursesString) { courses ->
            if (courses.isNullOrEmpty()) {
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

        observeLiveData(selectedRoutesPosition) {
            binding.busTimetableRoutesSpinner.setSelection(it, true)
        }

        observeLiveData(updatedAt) {
            shuttleBusTimetableAdapter.setUpdatedAt(it)
        }

        observeLiveData(busRoutes) {
            if (it.isNullOrEmpty()) {
                binding.busTimetableRoutesSpinner.isVisible = false
            } else {
                binding.busTimetableRoutesSpinner.isVisible = true
                binding.busTimetableRoutesSpinner.adapter =
                    ArrayAdapter(
                        requireContext(),
                        android.R.layout.simple_spinner_dropdown_item,
                        it
                    )
            }
        }

        observeLiveData(selectedRoutesPosition) {
            shuttleBusTimetableAdapter.submitList(
                busTimetables.value?.get(
                    selectedRoutesPosition.value ?: 0
                )?.map { it.toShuttleBusTimetableUiItem() }
            )
        }
    }
}