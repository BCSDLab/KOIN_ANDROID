package `in`.koreatech.koin.ui.business.registerstore.fragment

import `in`.koreatech.koin.R
import `in`.koreatech.koin.core.util.dataBinding
import `in`.koreatech.koin.databinding.RegisterMyStoreFragmentBinding
import `in`.koreatech.koin.databinding.RegisterStoreCategoryBinding
import `in`.koreatech.koin.domain.model.store.StoreCategory
import `in`.koreatech.koin.ui.business.registerstore.activity.RegisterStoreActivity
import `in`.koreatech.koin.ui.business.registerstore.viewmodel.RegisterStoreViewModel
import `in`.koreatech.koin.ui.store.viewmodel.StoreViewModel
import `in`.koreatech.koin.util.SnackbarUtil
import `in`.koreatech.koin.util.ext.textString
import android.os.Bundle
import android.view.View
import android.widget.TextView
import androidx.activity.viewModels
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController

class RegisterStoreCategoryFragment: Fragment(R.layout.register_store_category){
    private val binding by dataBinding<RegisterStoreCategoryBinding>()
    private val viewModel by activityViewModels<RegisterStoreViewModel>()
    private var storeCategory: String? = null

    override fun onViewCreated(view: View, savedInstanceState: Bundle?){
        super.onViewCreated(view, savedInstanceState)

        binding.backButtonInBasic.setOnClickListener {
            requireActivity().onBackPressedDispatcher.onBackPressed()
        }

        binding.storeCategoryChicken.setOnClickListener{
            handleCategorySelection(StoreCategory.Chicken)
            storeCategory = "치킨"
        }

        binding.storeCategoryPizza.setOnClickListener{
            handleCategorySelection(StoreCategory.Pizza)
            storeCategory = "피자"
        }

        binding.storeCategoryDosirak.setOnClickListener{
            handleCategorySelection(StoreCategory.Jeongsik)
            storeCategory = "도시락"
        }

        binding.storeCategoryPorkFeet.setOnClickListener{
            handleCategorySelection(StoreCategory.PorkFeet)
            storeCategory = "족발"
        }

        binding.storeCategoryChinese.setOnClickListener{
            handleCategorySelection(StoreCategory.Chinese)
            storeCategory = "중국집"
        }

        binding.storeCategoryNormal.setOnClickListener{
            handleCategorySelection(StoreCategory.NormalFood)
            storeCategory = "일반음식점"
        }

        binding.storeCategoryCafe.setOnClickListener{
            handleCategorySelection(StoreCategory.Cafe)
            storeCategory = "카페"
        }

        binding.storeCategoryHair.setOnClickListener{
            handleCategorySelection(StoreCategory.BeautySalon)
            storeCategory = "미용실"
        }

        binding.storeCategoryEtc.setOnClickListener{
            handleCategorySelection(StoreCategory.Etc)
            storeCategory = "기타"
        }

        binding.nextButton.setOnClickListener {
            if (
                storeCategory == null
            ) {
                SnackbarUtil.makeShortSnackbar(binding.root, getString(R.string.category_not_filled))
            }
            else {
                viewModel.setCategory(storeCategory)
                findNavController().navigate(R.id.register_category_fragment_to_register_basic_info_fragment)
            }
        }
    }

    private fun handleCategorySelection(category: StoreCategory?) {
        binding.storeCategoryChickenTextview.setCategorySelected(category == StoreCategory.Chicken)
        binding.storeCategoryPizzaTextview.setCategorySelected(category == StoreCategory.Pizza)
        binding.storeCategoryDosirakTextview.setCategorySelected(category == StoreCategory.Jeongsik)
        binding.storeCategoryPorkFeetTextview.setCategorySelected(category == StoreCategory.PorkFeet)
        binding.storeCategoryChineseTextview.setCategorySelected(category == StoreCategory.Chinese)
        binding.storeCategoryNormalTextview.setCategorySelected(category == StoreCategory.NormalFood)
        binding.storeCategoryCafeTextview.setCategorySelected(category == StoreCategory.Cafe)
        binding.storeCategoryHairTextview.setCategorySelected(category == StoreCategory.BeautySalon)
        binding.storeCategoryEtcTextview.setCategorySelected(category == StoreCategory.Etc)
    }

    private fun TextView.setCategorySelected(isSelected: Boolean) {
        setTextColor(
            ContextCompat.getColor(
                context,
                if (isSelected) R.color.colorAccent else R.color.black
            )
        )
    }
}