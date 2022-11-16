package `in`.koreatech.koin.ui.bus.fragment

import `in`.koreatech.koin.R
import `in`.koreatech.koin.core.fragment.DataBindingFragment
import `in`.koreatech.koin.core.progressdialog.IProgressDialog
import `in`.koreatech.koin.databinding.LayoutShuttleBusTimetableBinding
import `in`.koreatech.koin.ui.bus.adpater.timetable.ShuttleBusTimetableAdapter
import `in`.koreatech.koin.ui.bus.state.toShuttleBusTimetableUiItem
import `in`.koreatech.koin.ui.bus.viewmodel.ShuttleBusTimetableViewModel
import `in`.koreatech.koin.util.SnackbarUtil
import `in`.koreatech.koin.util.ext.observeLiveData
import `in`.koreatech.koin.util.ext.setOnItemSelectedListener
import `in`.koreatech.koin.util.ext.withLoading
import `in`.koreatech.koin.util.ext.withToastError
import android.os.Bundle
import android.view.View
import android.widget.ArrayAdapter
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class ShuttleBusTimetableFragment : DataBindingFragment<LayoutShuttleBusTimetableBinding>() {
    override val layoutId: Int = R.layout.layout_shuttle_bus_timetable

    private val shuttleBusTimetableViewModel by viewModels<ShuttleBusTimetableViewModel>()
    private val shuttleBusTimetableAdapter = ShuttleBusTimetableAdapter()

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
        }

        busTimetableRoutesSpinner.setOnItemSelectedListener { _, _, position, _ ->
            shuttleBusTimetableViewModel.setRoutePosition(position)
        }
    }

    private fun initViewModel() = with(shuttleBusTimetableViewModel) {
        (requireActivity() as? IProgressDialog)?.withLoading(viewLifecycleOwner, this)

        withToastError(this@ShuttleBusTimetableFragment, binding.root)

        observeLiveData(busCoursesString) { courses ->
            binding.busTimetableCoursesSpinner.adapter =
                ArrayAdapter(
                    requireContext(),
                    android.R.layout.simple_spinner_dropdown_item,
                    courses
                )
        }

        observeLiveData(busRoutes) { routes ->
            binding.busTimetableRoutesSpinner.adapter =
                ArrayAdapter(
                    requireContext(),
                    android.R.layout.simple_spinner_dropdown_item,
                    routes
                )
        }

        observeLiveData(selectedCoursesPosition) {
            binding.busTimetableCoursesSpinner.setSelection(it, true)
        }

        observeLiveData(selectedRoutesPosition) {
            binding.busTimetableRoutesSpinner.setSelection(it, true)
        }

        observeLiveData(busTimetables) { list ->
            shuttleBusTimetableAdapter.submitList(
                list.map { it.toShuttleBusTimetableUiItem() }
            )
            binding.recyclerView.smoothScrollToPosition(0)
        }
    }
}