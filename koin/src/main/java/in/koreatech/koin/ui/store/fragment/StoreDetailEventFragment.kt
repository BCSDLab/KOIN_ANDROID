package `in`.koreatech.koin.ui.store.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import `in`.koreatech.koin.databinding.FragmentStoreDetailEventBinding
import `in`.koreatech.koin.ui.store.adapter.StoreDetailEventRecyclerAdapter
import `in`.koreatech.koin.ui.store.viewmodel.StoreDetailViewModel
import `in`.koreatech.koin.util.ext.observeLiveData

class StoreDetailEventFragment : Fragment() {
    private var _binding: FragmentStoreDetailEventBinding? = null
    private val binding: FragmentStoreDetailEventBinding get() = _binding!!
    private val viewModel by activityViewModels<StoreDetailViewModel>()
    private val storeDetailMenuAdapter = StoreDetailEventRecyclerAdapter()

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
    }

    private fun initViews() {

        binding.storeDetailEventRecyclerview.apply {
            adapter = storeDetailMenuAdapter
        }
        viewModel.storeEvent.value?.let {
            storeDetailMenuAdapter.submitList(it)
        }
    }


    private fun initViewModel() {
        observeLiveData(viewModel.storeEvent) {
            storeDetailMenuAdapter.submitList(it)
        }

    }
}