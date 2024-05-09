package `in`.koreatech.koin.ui.store.fragment

import android.annotation.SuppressLint
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

    private val storeRecommendMenuAdapter = StoreDetailMenuRecyclerAdapter()
    private val storeMainMenuAdapter = StoreDetailMenuRecyclerAdapter()
    private val storeSetMenuAdapter = StoreDetailMenuRecyclerAdapter()
    private val storeSideMenuAdapter = StoreDetailMenuRecyclerAdapter()

    private val storeDetailActivityContract =
        registerForActivityResult(StoreDetailActivityContract()) {
        }

    private val storeRandomRecyclerAdapter = StoreRecyclerAdapter().apply {
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
        binding.storeDetailRecommendRecyclerview.adapter = storeRecommendMenuAdapter
        binding.storeDetailMainRecyclerview.adapter = storeMainMenuAdapter
        binding.storeDetailSetRecyclerview.adapter = storeSetMenuAdapter
        binding.storeDetailSideRecyclerview.adapter = storeSideMenuAdapter
        binding.storeRandomRecyclerView.adapter = storeRandomRecyclerAdapter

    }
    
    @SuppressLint("ResourceAsColor")
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
                        when(category.name){
                            "추천 메뉴" -> {
                                binding.storeDetailRecommendRecyclerview.visibility = View.VISIBLE
                                storeRecommendMenuAdapter.submitList(list)
                                binding.storeDetailRecommendMenuButton.setOnClickListener {
                                    binding.storeDetailMenuNestedScrollView.smoothScrollTo(0, binding.storeDetailRecommendRecyclerview.top)
                                }
                            }
                            "메인 메뉴" -> {
                                binding.storeDetailMainRecyclerview.visibility = View.VISIBLE
                                storeMainMenuAdapter.submitList(list)
                                binding.storeDetailMainMenuButton.setOnClickListener {
                                    binding.storeDetailMenuNestedScrollView.smoothScrollTo(0, binding.storeDetailMainRecyclerview.top)
                                }
                            }
                            "세트 메뉴" -> {
                                binding.storeDetailSetRecyclerview.visibility = View.VISIBLE
                                storeSetMenuAdapter.submitList(list)
                                binding.storeDetailSetMenuButton.setOnClickListener {
                                    binding.storeDetailMenuNestedScrollView.smoothScrollTo(0, binding.storeDetailSetRecyclerview.top)
                                }
                            }
                            "사이드 메뉴" -> {
                                binding.storeDetailSideRecyclerview.visibility = View.VISIBLE
                                storeSideMenuAdapter.submitList(list)
                                binding.storeDetailSideMenuButton.setOnClickListener {
                                    binding.storeDetailMenuNestedScrollView.smoothScrollTo(0, binding.storeDetailSideRecyclerview.top)
                                }
                            }
                        }
                    }
                }
            }
            observeLiveData(viewModel.recommendStores) {
                if (it != null) {
                    storeRandomRecyclerAdapter.submitList(it)
                }
            }
            observeLiveData(viewModel.categories) {
                if (it.menuCategories != null) {
                    viewModel.categories.value?.menuCategories?.forEachIndexed { index, category ->
                        when(category.name) {
                            binding.storeDetailMainMenuTextView.text -> {

                                binding.storeDetailMainMenuButton.strokeColor = ContextCompat.getColor(requireContext(), R.color.gray15)
                                binding.storeDetailMainMenuTextView.setTextColor(ContextCompat.getColor(requireContext(), R.color.gray10))
                            }
                            binding.storeDetailSetMenuTextView.text -> {
                                binding.storeDetailSetMenuButton.strokeColor = ContextCompat.getColor(requireContext(), R.color.gray15)
                                binding.storeDetailSetMenuTextView.setTextColor(ContextCompat.getColor(requireContext(), R.color.gray10))
                            }
                            binding.storeDetailSideMenuTextView.text -> {
                                binding.storeDetailSideMenuButton.strokeColor = ContextCompat.getColor(requireContext(), R.color.gray15)
                                binding.storeDetailSideMenuTextView.setTextColor(ContextCompat.getColor(requireContext(), R.color.gray10))
                            }
                            else -> {
                                binding.storeDetailRecommendMenuButton.strokeColor = ContextCompat.getColor(requireContext(), R.color.gray15)
                                binding.storeDetailRecommendMenuTextView.setTextColor(ContextCompat.getColor(requireContext(), R.color.gray10))
                            }
                        }

                        when(category.name){
                            "추천 메뉴" -> storeRecommendMenuAdapter.setCategory(category.name)
                            "메인 메뉴" -> storeMainMenuAdapter.setCategory(category.name)
                            "세트 메뉴" -> storeSetMenuAdapter.setCategory(category.name)
                            "사이드 메뉴" -> storeSideMenuAdapter.setCategory(category.name)
                        }
                    }
                }
            }
        }

    }
}