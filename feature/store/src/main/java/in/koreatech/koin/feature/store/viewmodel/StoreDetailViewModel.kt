package `in`.koreatech.koin.feature.store.viewmodel

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import `in`.koreatech.koin.domain.usecase.orderShop.GetOrderShopOriginInfoUseCase
import `in`.koreatech.koin.domain.usecase.orderShop.GetOrderShopSummaryUseCase
import `in`.koreatech.koin.domain.usecase.store.GetShopMenusUseCase
import `in`.koreatech.koin.domain.usecase.store.GetStoreReviewUseCase
import `in`.koreatech.koin.domain.usecase.store.GetStoreWithMenuUseCase
import `in`.koreatech.koin.domain.usecase.token.IsTokenSavedInDeviceUseCase
import `in`.koreatech.koin.feature.store.model.MenuCategoryModel
import `in`.koreatech.koin.feature.store.model.toMenuCategoryModel
import `in`.koreatech.koin.feature.store.model.toStoreIndoModel
import `in`.koreatech.koin.feature.store.model.toStoreInfoModel
import `in`.koreatech.koin.feature.store.view.StoreDetailSideEffect
import `in`.koreatech.koin.feature.store.view.StoreDetailState
import javax.inject.Inject
import org.orbitmvi.orbit.ContainerHost
import org.orbitmvi.orbit.syntax.simple.blockingIntent
import org.orbitmvi.orbit.syntax.simple.intent
import org.orbitmvi.orbit.syntax.simple.reduce
import org.orbitmvi.orbit.viewmodel.container

@HiltViewModel
class StoreDetailViewModel @Inject constructor(
    savedStateHandle: SavedStateHandle,
    private val getOrderShopOriginInfoUseCase: GetOrderShopOriginInfoUseCase,
    private val getOrderShopSummaryUseCase: GetOrderShopSummaryUseCase,
    private val getStoreWithMenuUseCase: GetStoreWithMenuUseCase,
    private val getShopMenusUseCase: GetShopMenusUseCase,
    private val getStoreReviewUseCase: GetStoreReviewUseCase,
    private val isTokenSavedInDeviceUseCase: IsTokenSavedInDeviceUseCase
) : ViewModel(), ContainerHost<StoreDetailState, StoreDetailSideEffect> {
    override val container =
        container<StoreDetailState, StoreDetailSideEffect>(StoreDetailState()) {
            val storeId = savedStateHandle.get<Int>(STORE_ID)
            checkNotNull(storeId)
            fetchStore(storeId)
            fetchMenus(storeId)
            fetchReview(storeId)
            checkToken()
        }

    private fun fetchOrderableStore(id: Int) = intent {
        getOrderShopSummaryUseCase(id).also { result ->
            reduce {
                state.copy(
                    store = result.toStoreIndoModel(),
                    isLoading = false
                )
            }
        }
    }

    private fun fetchStore(id: Int) = intent {
        getStoreWithMenuUseCase(id).also { result ->
            reduce {
                state.copy(
                    store = result.toStoreInfoModel(),
                    isLoading = false
                )
            }
        }
    }

    private fun fetchMenus(id: Int) = intent {
        getShopMenusUseCase(id).also { shop ->
            reduce {
                state.copy(
                    categories = shop.menuCategories ?.map { storeMenuCategories ->
                        MenuCategoryModel(
                            menuGroupId = storeMenuCategories.toMenuCategoryModel().menuGroupId,
                            menuGroupName = storeMenuCategories.toMenuCategoryModel().menuGroupName,
                            menus = storeMenuCategories.toMenuCategoryModel().menus,
                            isChecked = shop.menuCategories?.indexOf(storeMenuCategories) == 0
                        )
                    } ?: emptyList()
                )
            }
        }
    }

    private fun checkToken() = intent {
        reduce { state.copy(isLoading = true) }
        val hasToken = isTokenSavedInDeviceUseCase()
        if (hasToken) {
            reduce {
                state.copy(
                    isLogin = true,
                    isLoading = false
                )
            }
        } else {
            reduce {
                state.copy(
                    isLogin = false,
                    isLoading = false
                )
            }
        }
    }

    private fun fetchReview(storeId: Int) = intent {
        getStoreReviewUseCase(storeId).also { reviews ->
            reduce {
                state.copy(
                    storeReview = reviews
                )
            }
        }
    }

    fun clickMenuCategory(categoryId: Int) = blockingIntent {
        reduce {
            state.copy(
                categories = state.categories.map {
                    if (it.menuGroupId == categoryId) {
                        it.copy(isChecked = true)
                    } else {
                        it.copy(isChecked = false)
                    }
                }
            )
        }
    }

    companion object {
        const val STORE_ID = "storeId"
        const val ORDERABLE_STORE_ID = "orderableStoreId"
    }
}
