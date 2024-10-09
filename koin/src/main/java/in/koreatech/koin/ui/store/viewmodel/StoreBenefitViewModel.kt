package `in`.koreatech.koin.ui.store.viewmodel

import android.util.Log
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import `in`.koreatech.koin.core.viewmodel.BaseViewModel
import `in`.koreatech.koin.domain.model.store.BenefitCategoryList
import `in`.koreatech.koin.domain.model.store.StoreBenefit
import `in`.koreatech.koin.domain.usecase.store.benefit.BenefitStoreListUseCase
import `in`.koreatech.koin.domain.usecase.store.benefit.StoreBenefitCategoryUseCase
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class StoreBenefitViewModel @Inject constructor(
    val storeBenefitCategoryUseCase: StoreBenefitCategoryUseCase,
    val benefitShopListUseCase: BenefitStoreListUseCase,
):BaseViewModel() {
    private val _storeBenefitCategories = MutableStateFlow(BenefitCategoryList(emptyList()))
    private val _storeBenefitShopList = MutableStateFlow(StoreBenefit(0, emptyList()))
    private val _categoryId = MutableStateFlow(0)
    val storeBenefitCategories get() = _storeBenefitCategories
    val benefitShopList get() = _storeBenefitShopList
    val categoryId get() = _categoryId

    init{
        getStoreBenefitCategories()
    }
    private fun getStoreBenefitCategories() = viewModelScope.launchWithLoading {
        storeBenefitCategoryUseCase().onSuccess {
            _storeBenefitCategories.value = it
            _categoryId.value = it.benefitCategories[0].id
            getStoreBenefitShopList(_categoryId.value)
        }.onFailure {
            it.printStackTrace()
        }
    }

    fun getStoreBenefitShopList(uid: Int) = viewModelScope.launchWithLoading {
        benefitShopListUseCase(uid).onSuccess {
            _storeBenefitShopList.value = it
        }.onFailure {
            it.printStackTrace()
            _storeBenefitShopList.value = StoreBenefit(0, emptyList())
        }
    }

    fun setCategoryId(id: Int){
        _categoryId.value = id
        getStoreBenefitShopList(id)
    }

    fun getCategoryTitle() = _storeBenefitCategories.value.benefitCategories[_categoryId.value - 1].title
}