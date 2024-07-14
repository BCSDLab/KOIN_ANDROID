package `in`.koreatech.koin.ui.store.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import `in`.koreatech.koin.databinding.FragementStoreDetailReviewBinding
import `in`.koreatech.koin.ui.store.adapter.StoreDetailEventRecyclerAdapter
import `in`.koreatech.koin.ui.store.viewmodel.StoreDetailViewModel
import `in`.koreatech.koin.util.ext.observeLiveData

class StoreDetailReviewFragment : Fragment() {
    private var _binding: FragementStoreDetailReviewBinding? = null
    private val binding: FragementStoreDetailReviewBinding get() = _binding!!
    private val viewModel by activityViewModels<StoreDetailViewModel>()
    private val storeDetailEventAdapter = StoreDetailEventRecyclerAdapter()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        return FragementStoreDetailReviewBinding.inflate(inflater, container, false).also {
            _binding = it
        }.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initViews()
        initViewModel()
    }

    private fun initViews() {


    }

    private fun initViewModel() {
        observeLiveData(viewModel.storeEvent) {
            storeDetailEventAdapter.submitList(it)
        }

    }
}