package `in`.koreatech.koin.ui.store.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.recyclerview.widget.LinearLayoutManager
import `in`.koreatech.koin.databinding.FragmentStoreDetailMenuBinding
import `in`.koreatech.koin.ui.store.adapter.StoreDetailMenuRecyclerAdapter
import `in`.koreatech.koin.ui.store.adapter.StoreRecyclerAdapter
import `in`.koreatech.koin.ui.store.contract.StoreDetailActivityContract
import `in`.koreatech.koin.ui.store.viewmodel.StoreDetailViewModel
import `in`.koreatech.koin.util.ext.observeLiveData

class StoreDetailMenuFragment : Fragment() {
    private var _binding: FragmentStoreDetailMenuBinding? = null
    private val binding: FragmentStoreDetailMenuBinding get() = _binding!!
    private val viewModel by activityViewModels<StoreDetailViewModel>()

    private val storeMenuAdapter: MutableList<StoreDetailMenuRecyclerAdapter> =
        List(4) { StoreDetailMenuRecyclerAdapter() }.toMutableList()

    private val storeDetailActivityContract =
        registerForActivityResult(StoreDetailActivityContract()) {
        }

    private val storeRecyclerAdapter = StoreRecyclerAdapter().apply {
        setOnItemClickListener {
            storeDetailActivityContract.launch(it.uid)
            requireActivity().finish()
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        return FragmentStoreDetailMenuBinding.inflate(inflater, container, false).also {
            _binding = it
        }.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initViews()
        initViewModel()
    }

    private fun initViews() {
        repeat(4) {
            storeMenuAdapter.add(StoreDetailMenuRecyclerAdapter())
        }
        binding.storeDetailRecommendRecyclerview.adapter = storeMenuAdapter[0]
        binding.storeDetailMainRecyclerview.adapter = storeMenuAdapter[1]
        binding.storeDetailSetRecyclerview.adapter = storeMenuAdapter[2]
        binding.storeDetailSideRecyclerview.adapter = storeMenuAdapter[3]
        binding.storeRandomRecyclerView.adapter = storeRecyclerAdapter
    }
    
    private fun initViewModel() {

        observeLiveData(viewModel.storeMenu) {
            viewModel.categories.value?.menuCategories?.forEachIndexed { index, category ->
                viewModel.storeMenu.value?.let {
                    storeMenuAdapter[index].submitList(
                        it
                    )
                }
            }
        }
        observeLiveData(viewModel.recommendStores) {
            if (it != null) {
                storeRecyclerAdapter.submitList(it)
            }
        }
        observeLiveData(viewModel.categories) {
            if (it.menuCategories != null) {
                viewModel.categories.value?.menuCategories?.forEachIndexed { index, category ->
                    storeMenuAdapter[index].setCategory(category.name)
                }
            }
        }
    }

}