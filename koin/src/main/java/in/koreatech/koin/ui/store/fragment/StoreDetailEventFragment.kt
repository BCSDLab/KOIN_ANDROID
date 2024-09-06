package `in`.koreatech.koin.ui.store.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ScrollView
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import `in`.koreatech.koin.core.analytics.EventLogger
import `in`.koreatech.koin.core.constant.AnalyticsConstant
import `in`.koreatech.koin.databinding.FragmentStoreDetailEventBinding
import `in`.koreatech.koin.domain.model.store.StoreDetailScrollType
import `in`.koreatech.koin.ui.store.adapter.StoreDetailEventRecyclerAdapter
import `in`.koreatech.koin.ui.store.viewmodel.StoreDetailViewModel
import `in`.koreatech.koin.util.ext.observeLiveData

class StoreDetailEventFragment : Fragment() {
    private var _binding: FragmentStoreDetailEventBinding? = null
    private val binding: FragmentStoreDetailEventBinding get() = _binding!!
    private val viewModel by activityViewModels<StoreDetailViewModel>()
    private val storeDetailEventAdapter = StoreDetailEventRecyclerAdapter()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        return FragmentStoreDetailEventBinding.inflate(inflater, container, false).also {
            _binding = it
        }.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initViews()
        initViewModel()
        initEventScrollCallback()
    }

    private fun initViews() {

        binding.storeDetailEventRecyclerview.apply {
            adapter = storeDetailEventAdapter
        }
        viewModel.storeEvent.value?.let {
            if (it.isNotEmpty()) {
                binding.storeDetailEventRecyclerview.visibility = View.VISIBLE
                binding.storeDetailNoEventImageView.visibility = View.GONE
                binding.storeDetailNoEventTextView.visibility = View.GONE
                storeDetailEventAdapter.submitList(it)
            } else {
                binding.storeDetailEventRecyclerview.visibility = View.GONE
                binding.storeDetailNoEventImageView.visibility = View.VISIBLE
                binding.storeDetailNoEventTextView.visibility = View.VISIBLE
            }
        }

    }

    private fun initViewModel() {
        observeLiveData(viewModel.storeEvent) {
            storeDetailEventAdapter.submitList(it)
        }

        observeLiveData(viewModel.scrollUp){
            if(it == StoreDetailScrollType.EVENT){
                binding.storeEventScrollView.fullScroll(ScrollView.FOCUS_UP)
                viewModel.scrollReset()
            }
        }

    }

    private fun initEventScrollCallback() {
        binding.storeEventScrollView.setOnScrollChangeListener { _, _, scrollY, _, oldScrollY ->
            val contentHeight = binding.storeEventScrollView.getChildAt(0).measuredHeight
            val scrollViewHeight = binding.storeEventScrollView.height
            val totalScrollRange = contentHeight - scrollViewHeight
            val seventyPercentScroll = (totalScrollRange * 0.7).toInt()

            if (seventyPercentScroll in (oldScrollY + 1)..scrollY) {
                EventLogger.logScrollEvent(
                    AnalyticsConstant.Domain.BUSINESS,
                    AnalyticsConstant.Label.SHOP_DETAIL_VIEW_EVENT,
                    viewModel.store.value?.name ?: "Unknown"
                )
            }
        }
    }
}