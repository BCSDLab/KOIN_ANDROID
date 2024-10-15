package `in`.koreatech.koin.ui.store.fragment

import android.annotation.SuppressLint
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ScrollView
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import `in`.koreatech.koin.R
import `in`.koreatech.koin.core.analytics.EventAction
import `in`.koreatech.koin.core.analytics.EventLogger
import `in`.koreatech.koin.core.constant.AnalyticsConstant
import `in`.koreatech.koin.databinding.FragmentStoreDetailMenuBinding
import `in`.koreatech.koin.domain.model.store.ShopMenus
import `in`.koreatech.koin.domain.model.store.StoreDetailScrollType
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

    private var selectedCategory: String? = null

    private val storeDetailActivityContract =
        registerForActivityResult(StoreDetailActivityContract()) {
        }

    private val storeRandomRecyclerAdapter = StoreRecyclerAdapter().apply {
        setOnItemClickListener {

            storeDetailActivityContract.launch(Triple(it.uid,null,false))
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
        initEventScrollCallback()
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
                                    setUnselectedCategoryButtonStyle(selectedCategory)
                                    selectedCategory = "추천 메뉴"
                                    setSelectedCategoryButtonStyle(selectedCategory)
                                }
                            }
                            "메인 메뉴" -> {
                                binding.storeDetailMainRecyclerview.visibility = View.VISIBLE
                                storeMainMenuAdapter.submitList(list)
                                binding.storeDetailMainMenuButton.setOnClickListener {
                                    binding.storeDetailMenuNestedScrollView.smoothScrollTo(0, binding.storeDetailMainRecyclerview.top)
                                    setUnselectedCategoryButtonStyle(selectedCategory)
                                    selectedCategory = "메인 메뉴"
                                    setSelectedCategoryButtonStyle(selectedCategory)
                                }
                            }
                            "세트 메뉴" -> {
                                binding.storeDetailSetRecyclerview.visibility = View.VISIBLE
                                storeSetMenuAdapter.submitList(list)
                                binding.storeDetailSetMenuButton.setOnClickListener {
                                    binding.storeDetailMenuNestedScrollView.smoothScrollTo(0, binding.storeDetailSetRecyclerview.top)
                                    setUnselectedCategoryButtonStyle(selectedCategory)
                                    selectedCategory = "세트 메뉴"
                                    setSelectedCategoryButtonStyle(selectedCategory)
                                }
                            }
                            "사이드 메뉴" -> {
                                binding.storeDetailSideRecyclerview.visibility = View.VISIBLE
                                storeSideMenuAdapter.submitList(list)
                                binding.storeDetailSideMenuButton.setOnClickListener {
                                    binding.storeDetailMenuNestedScrollView.smoothScrollTo(0, binding.storeDetailSideRecyclerview.top)
                                    setUnselectedCategoryButtonStyle(selectedCategory)
                                    selectedCategory = "사이드 메뉴"
                                    setSelectedCategoryButtonStyle(selectedCategory)
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

            observeLiveData(viewModel.scrollUp){
                if(it == StoreDetailScrollType.MENU){
                    binding.storeDetailMenuNestedScrollView.fullScroll(ScrollView.FOCUS_UP)
                    viewModel.scrollReset()
                }
            }
        }
    }
    private fun setSelectedCategoryButtonStyle(name: String?) {
        when(name) {
            "추천 메뉴" -> {
                binding.storeDetailRecommendMenuButton.strokeWidth = 0
                binding.storeDetailRecommendMenuTextView.setTextColor(ContextCompat.getColor(requireContext(), R.color.white))
                binding.storeDetailRecommendMenuButton.setCardBackgroundColor(ContextCompat.getColor(requireContext(), R.color.colorPrimary))
            }
            "메인 메뉴" -> {
                binding.storeDetailMainMenuButton.strokeWidth = 0
                binding.storeDetailMainMenuTextView.setTextColor(ContextCompat.getColor(requireContext(), R.color.white))
                binding.storeDetailMainMenuButton.setCardBackgroundColor(ContextCompat.getColor(requireContext(), R.color.colorPrimary))
            }
            "세트 메뉴" -> {
                binding.storeDetailSetMenuButton.strokeWidth = 0
                binding.storeDetailSetMenuTextView.setTextColor(ContextCompat.getColor(requireContext(), R.color.white))
                binding.storeDetailSetMenuButton.setCardBackgroundColor(ContextCompat.getColor(requireContext(), R.color.colorPrimary))
            }
            "사이드 메뉴" -> {
                binding.storeDetailSideMenuButton.strokeWidth = 0
                binding.storeDetailSideMenuTextView.setTextColor(ContextCompat.getColor(requireContext(), R.color.white))
                binding.storeDetailSideMenuButton.setCardBackgroundColor(ContextCompat.getColor(requireContext(), R.color.colorPrimary))
            }
        }
    }
    private fun setUnselectedCategoryButtonStyle(name: String?) {
        when(name) {
            null -> return
            "추천 메뉴" -> {
                binding.storeDetailRecommendMenuButton.strokeWidth = 1
                binding.storeDetailRecommendMenuTextView.setTextColor(ContextCompat.getColor(requireContext(), R.color.gray10))
                binding.storeDetailRecommendMenuButton.setCardBackgroundColor(ContextCompat.getColor(requireContext(), R.color.white))
            }
            "메인 메뉴" -> {
                binding.storeDetailMainMenuButton.strokeWidth = 1
                binding.storeDetailMainMenuTextView.setTextColor(ContextCompat.getColor(requireContext(), R.color.gray10))
                binding.storeDetailMainMenuButton.setCardBackgroundColor(ContextCompat.getColor(requireContext(), R.color.white))
            }
            "세트 메뉴" -> {
                binding.storeDetailSetMenuButton.strokeWidth = 1
                binding.storeDetailSetMenuTextView.setTextColor(ContextCompat.getColor(requireContext(), R.color.gray10))
                binding.storeDetailSetMenuButton.setCardBackgroundColor(ContextCompat.getColor(requireContext(), R.color.white))
            }
            "사이드 메뉴" -> {
                binding.storeDetailSideMenuButton.strokeWidth = 1
                binding.storeDetailSideMenuTextView.setTextColor(ContextCompat.getColor(requireContext(), R.color.gray10))
                binding.storeDetailSideMenuButton.setCardBackgroundColor(ContextCompat.getColor(requireContext(), R.color.white))
            }
        }

    }


    private fun initEventScrollCallback() {
        binding.storeDetailMenuNestedScrollView.setOnScrollChangeListener { _, _, scrollY, _, oldScrollY ->
            val contentHeight = binding.storeDetailMenuNestedScrollView.getChildAt(0).measuredHeight
            val scrollViewHeight = binding.storeDetailMenuNestedScrollView.height
            val totalScrollRange = contentHeight - scrollViewHeight
            val seventyPercentScroll = (totalScrollRange * 0.7).toInt()

            if (seventyPercentScroll in (oldScrollY + 1)..scrollY) {
                EventLogger.logScrollEvent(
                    EventAction.BUSINESS,
                    AnalyticsConstant.Label.SHOP_DETAIL_VIEW,
                    viewModel.store.value?.name ?: "Unknown"
                )
            }
        }
    }
}