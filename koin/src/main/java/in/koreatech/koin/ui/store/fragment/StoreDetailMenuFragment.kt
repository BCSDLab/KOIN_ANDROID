package `in`.koreatech.koin.ui.store.fragment

import android.graphics.Color
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.recyclerview.widget.LinearLayoutManager
import `in`.koreatech.koin.R
import `in`.koreatech.koin.databinding.FragmentStoreDetailMenuBinding
import `in`.koreatech.koin.domain.model.store.ShopMenus
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

                val list = mutableListOf<ShopMenus>()
                viewModel.storeMenu.value?.let {
                    category.menus?.forEachIndexed { menuIndex, shopMenus ->
                        if (shopMenus.isSingle && shopMenus.singlePrice != null) {
                            shopMenus.singlePrice
                            list.add(shopMenus)
                        } else if (shopMenus.optionPrices?.isNotEmpty() == true) {//옵션

                            shopMenus.optionPrices?.forEachIndexed { optionIndex, shopMenuOptions ->
                                list.add(
                                    ShopMenus(
                                        id = shopMenus.id,
                                        name = shopMenus.name+"-"+shopMenuOptions.option,
                                        isHidden = shopMenus.isHidden,
                                        isSingle = true,
                                        singlePrice = shopMenuOptions.price,
                                        null,
                                        description = shopMenus.description,
                                        imageUrls = shopMenus.imageUrls
                                    )
                                )
                            }
                        }
                        storeMenuAdapter[index].submitList(
                            list
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
                        if(binding.storeDetailMainMenuTextView.text == category.name){
                            binding.storeDetailMainMenuButton.setCardBackgroundColor(ContextCompat.getColor(requireContext(), R.color.colorPrimary))
                            binding.storeDetailMainMenuTextView.setTextColor(Color.WHITE)
                        }
                        else if(binding.storeDetailSetMenuTextView.text == category.name){
                            binding.storeDetailSetMenuButton.setCardBackgroundColor(ContextCompat.getColor(requireContext(), R.color.colorPrimary))
                            binding.storeDetailSetMenuTextView.setTextColor(Color.WHITE)
                        }
                        else if (binding.storeDetailSideMenuTextView.text == category.name){
                            binding.storeDetailSideMenuButton.setCardBackgroundColor(ContextCompat.getColor(requireContext(), R.color.colorPrimary))
                            binding.storeDetailSideMenuTextView.setTextColor(Color.WHITE)
                        }
                        else{
                            binding.storeDetailRecommendMenuButton.setCardBackgroundColor(ContextCompat.getColor(requireContext(), R.color.colorPrimary))
                            binding.storeDetailRecommendMenuTextView.setTextColor(Color.WHITE)
                        }


                        storeMenuAdapter[index].setCategory(category.name)
                    }
                }
            }
        }

    }
}