package `in`.koreatech.koin.ui.store.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import `in`.koreatech.koin.core.viewmodel.BaseViewModel
import `in`.koreatech.koin.domain.model.store.ReviewFilterEnum
import `in`.koreatech.koin.domain.model.store.ShopEvent
import `in`.koreatech.koin.domain.model.store.ShopMenus
import `in`.koreatech.koin.domain.model.store.Store
import `in`.koreatech.koin.domain.model.store.StoreMenu
import `in`.koreatech.koin.domain.model.store.StoreReview
import `in`.koreatech.koin.domain.model.store.StoreReviewContent
import `in`.koreatech.koin.domain.model.store.StoreWithMenu
import `in`.koreatech.koin.domain.usecase.store.GetRecommendStoresUseCase
import `in`.koreatech.koin.domain.usecase.store.GetShopEventsUseCase
import `in`.koreatech.koin.domain.usecase.store.GetShopMenusUseCase
import `in`.koreatech.koin.domain.usecase.store.GetStoreReviewUseCase
import `in`.koreatech.koin.domain.usecase.store.GetStoreWithMenuUseCase
import javax.inject.Inject

@HiltViewModel
class StoreDetailViewModel @Inject constructor(
    private val getStoreWithMenuUseCase: GetStoreWithMenuUseCase,
    private val getRecommendStoresUseCase: GetRecommendStoresUseCase,
    private val getShopMenusUseCase: GetShopMenusUseCase,
    private val getStoreReviewUseCase: GetStoreReviewUseCase,
    private val getStoreEventsUseCase: GetShopEventsUseCase
) : BaseViewModel() {
    val store: LiveData<StoreWithMenu> get() = _store
    private val _store = MutableLiveData<StoreWithMenu>()
    val categories: LiveData<StoreMenu> get() = _categories
    private var _categories = MutableLiveData<StoreMenu>()
    val storeMenu: LiveData<List<ShopMenus>> get() = _storeMenu
    private val _storeMenu = MutableLiveData<List<ShopMenus>>()
    val storeEvent: LiveData<List<ShopEvent>> get() = _storeEvent
    private val _storeEvent = MutableLiveData<List<ShopEvent>>()

    val storeReview: LiveData<StoreReview> get() = _storeReview
    private val _storeReview = MutableLiveData<StoreReview>()

    val storeReviewContent: LiveData<List<StoreReviewContent>>  get() = _storeReviewContent
    private val _storeReviewContent = MutableLiveData<List<StoreReviewContent>>()

    val recommendStores: LiveData<List<Store>?> get() = _recommendStores
    private val _recommendStores = MutableLiveData<List<Store>?>()

    fun getStoreWithMenu(storeId: Int) = viewModelScope.launchWithLoading {
        getStoreWithMenuUseCase(storeId).also { store ->
            _store.value = store
            _recommendStores.value = getRecommendStoresUseCase(store)
        }
    }

    fun getShopMenus(storeId: Int) = viewModelScope.launchWithLoading {
        getShopMenusUseCase(storeId).also { shop ->
            _categories.value = shop
            _storeMenu.value = if (shop.menuCategories?.isEmpty() == true) {
                emptyList()
            } else {
                shop.menuCategories?.first()?.menus ?: emptyList()
            }
        }
    }

    fun getShopEvents(storeId: Int) = viewModelScope.launchWithLoading {
        getStoreEventsUseCase(storeId).also { events ->
            _storeEvent.value = events.events.ifEmpty {
                emptyList()
            }

        }
    }

    fun getShopReviews(storeId: Int) = viewModelScope.launchWithLoading {
        getStoreReviewUseCase(storeId).also { reviews ->
            _storeReview.value = reviews
            _storeReviewContent.value = reviews.reviews.sortedByDescending {
                it.createdAt
            }
        }
    }

    fun checkShowMyReview(isChecked: Boolean){
        if(isChecked){
            _storeReviewContent.value = _storeReview.value?.reviews?.filter {
                it.isMine
            }
        }
        else _storeReviewContent.value = _storeReview.value?.reviews
    }

    fun filterReview(filter: ReviewFilterEnum){
        when(filter){
            ReviewFilterEnum.LATEST -> {
                _storeReviewContent.value = _storeReview.value?.reviews?.sortedByDescending {
                    it.createdAt
                }
            }
            ReviewFilterEnum.OLDEST -> {
                _storeReviewContent.value = _storeReview.value?.reviews?.sortedBy {
                    it.createdAt
                }
            }
            ReviewFilterEnum.HIGH_RATTING -> {
                _storeReviewContent.value = _storeReview.value?.reviews?.sortedByDescending {
                    it.rating
                }
            }
            ReviewFilterEnum.LOW_RATIONG -> {
                _storeReviewContent.value = _storeReview.value?.reviews?.sortedBy {
                    it.rating
                }
            }
        }
    }
}