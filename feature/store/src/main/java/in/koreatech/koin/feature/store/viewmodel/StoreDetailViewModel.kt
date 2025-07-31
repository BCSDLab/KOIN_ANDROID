package `in`.koreatech.koin.feature.store.viewmodel

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import `in`.koreatech.koin.domain.usecase.orderShop.GetOrderShopMenuUseCase
import `in`.koreatech.koin.domain.usecase.orderShop.GetOrderShopOriginInfoUseCase
import `in`.koreatech.koin.domain.usecase.orderShop.GetOrderShopSummaryUseCase
import `in`.koreatech.koin.domain.usecase.store.GetShopMenusUseCase
import `in`.koreatech.koin.domain.usecase.store.GetStoreReviewUseCase
import `in`.koreatech.koin.domain.usecase.store.GetStoreWithMenuUseCase
import `in`.koreatech.koin.domain.usecase.token.IsTokenSavedInDeviceUseCase
import `in`.koreatech.koin.feature.store.model.DeliveryTipModel
import `in`.koreatech.koin.feature.store.model.MenuCategoryModel
import `in`.koreatech.koin.feature.store.model.OriginModel
import `in`.koreatech.koin.feature.store.model.OwnerInfoModel
import `in`.koreatech.koin.feature.store.model.StoreDescriptionModel
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
    private val getOrderShopMenuUseCase: GetOrderShopMenuUseCase,
    private val getStoreWithMenuUseCase: GetStoreWithMenuUseCase,
    private val getShopMenusUseCase: GetShopMenusUseCase,
    private val getStoreReviewUseCase: GetStoreReviewUseCase,
    private val isTokenSavedInDeviceUseCase: IsTokenSavedInDeviceUseCase
) : ViewModel(), ContainerHost<StoreDetailState, StoreDetailSideEffect> {
    override val container =
        container<StoreDetailState, StoreDetailSideEffect>(StoreDetailState()) {
            val storeId = savedStateHandle.get<Int>(STORE_ID)
            val isOrderableShop = savedStateHandle.get<Boolean>("isOrderableShop") ?: true
            checkNotNull(storeId)

            if (isOrderableShop) {
                fetchOrderableStore(storeId)
            } else {
                fetchStore(storeId)
            }
            fetchReview(storeId)
            checkToken()
        }

    private fun fetchOrderStoreNotice(id: Int) = intent {
        getOrderShopOriginInfoUseCase(id).also { result ->
            reduce {
                state.copy(
                    isLoading = false,
                    shopDescription = StoreDescriptionModel(
                        id = id,
                        storeName = result.name,
                        description = result.introduction,
                        notice = result.notice,
                        deliveryTips = result.deliveryTips.map { tips ->
                            DeliveryTipModel(
                                fromAmount = tips.fromAmount,
                                toAmount = tips.toAmount,
                                fee = tips.feel
                            )
                        },
                        origins = result.origins.map { origin ->
                            listOf(
                                OriginModel(
                                    ingredients = origin.ingredient,
                                    origin = origin.origin
                                )
                            )
                        }.firstOrNull() ?: listOf(OriginModel.empty()),
                        ownerInfo = OwnerInfoModel(
                            result.ownerInfo.name ?: "",
                            result.ownerInfo.shopName ?: "",
                            result.address,
                            result.ownerInfo.companyRegistrationNumber ?: ""
                        )
                    )
                )
            }
        }
    }

    private fun fetchOrderableStore(id: Int) = intent {
        getOrderShopSummaryUseCase(id).also { result ->
            reduce {
                state.copy(
                    store = result.toStoreIndoModel()
                )
            }
        }
        fetchOrderableStoreMenu(id)
    }

    private fun fetchOrderableStoreMenu(id: Int) = intent {
        getOrderShopMenuUseCase(id).also { result ->
            reduce {
                state.copy(
                    categories = result.map {
                        it.toMenuCategoryModel().copy(
                            isChecked = result.indexOf(it) == 0
                        )
                    }
                )
            }
        }
        fetchOrderStoreNotice(id)
    }

    private fun fetchStore(id: Int) = intent {
        getStoreWithMenuUseCase(id).also { result ->
            reduce {
                state.copy(
                    store = result.toStoreInfoModel(),
                    isLoading = false,
                    shopDescription = StoreDescriptionModel(
                        id = id,
                        storeName = result.name,
                        description = result.description,
                        notice = result.description,
                        deliveryTips = DeliveryTipModel(
                            fromAmount = 0,
                            toAmount = null,
                            fee = result.deliveryPrice
                        ).let { listOf(it) },
                        origins = null,
                        ownerInfo = null
                    )
                )
            }
        }
        fetchMenus(id)
    }

    private fun fetchMenus(id: Int) = intent {
        getShopMenusUseCase(id).also { shop ->
            reduce {
                state.copy(
                    categories = shop.menuCategories?.map { storeMenuCategories ->
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
        val hasToken = isTokenSavedInDeviceUseCase()
        if (hasToken) {
            reduce {
                state.copy(
                    isLogin = true
                )
            }
        } else {
            reduce {
                state.copy(
                    isLogin = false
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
    }
}
