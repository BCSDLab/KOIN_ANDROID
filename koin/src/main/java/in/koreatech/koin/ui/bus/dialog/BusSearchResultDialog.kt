package `in`.koreatech.koin.ui.bus.dialog

import `in`.koreatech.koin.databinding.BusTimetableSearchResultDialogBinding
import `in`.koreatech.koin.ui.bus.adpater.search.BusSearchResultAdapter
import `in`.koreatech.koin.ui.bus.state.BusSearchResultItem
import `in`.koreatech.koin.util.ext.windowHeight
import `in`.koreatech.koin.util.ext.windowWidth
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.WindowManager
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.fragment.app.DialogFragment
import androidx.recyclerview.widget.LinearLayoutManager

class BusSearchResultDialog : DialogFragment() {

    private var _binding: BusTimetableSearchResultDialogBinding? = null
    val binding get() = _binding!!

    private val busSearchResultAdapter = BusSearchResultAdapter()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = BusTimetableSearchResultDialogBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onResume() {
        super.onResume()
        val params: ViewGroup.LayoutParams? = dialog?.window?.attributes

        params?.width = (windowWidth * 0.9).toInt()
        dialog?.window?.attributes = params as WindowManager.LayoutParams
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.recyclerView.apply {
            layoutManager = LinearLayoutManager(requireContext())
            adapter = busSearchResultAdapter
        }
        binding.buttonClose.setOnClickListener { dismiss() }
    }

    fun submitList(list: List<BusSearchResultItem>) {
        busSearchResultAdapter.submitList(list)
    }

    override fun onDestroyView() {
        _binding = null
        super.onDestroyView()
    }
}