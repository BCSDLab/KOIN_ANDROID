package `in`.koreatech.koin.feature.store.detail

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import `in`.koreatech.koin.core.developer.DeveloperOption
import `in`.koreatech.koin.core.developer.DeveloperOptionUtil
import `in`.koreatech.koin.domain.error.store.KoinStoreException
import `in`.koreatech.koin.domain.model.cart.CartType
import `in`.koreatech.koin.domain.usecase.orderShop.GetOrderShopMenuUseCase
import `in`.koreatech.koin.domain.usecase.orderShop.GetOrderShopOriginInfoUseCase
import `in`.koreatech.koin.domain.usecase.orderShop.GetOrderShopSummaryUseCase
import `in`.koreatech.koin.domain.usecase.store.GetCartItemUseCase
import `in`.koreatech.koin.domain.usecase.store.GetCartItemsCountUseCase
import `in`.koreatech.koin.domain.usecase.store.GetCartSummaryUseCase
import `in`.koreatech.koin.domain.usecase.store.GetShopEventsUseCase
import `in`.koreatech.koin.domain.usecase.store.GetShopMenusUseCase
import `in`.koreatech.koin.domain.usecase.store.GetStoreReviewUseCase
import `in`.koreatech.koin.domain.usecase.store.GetStoreWithMenuUseCase
import `in`.koreatech.koin.domain.usecase.store.ValidateCartItemsUseCase
import `in`.koreatech.koin.domain.usecase.token.IsTokenSavedInDeviceUseCase
import `in`.koreatech.koin.feature.store.enums.CartValidation
import `in`.koreatech.koin.feature.store.model.DeliveryTipModel
import `in`.koreatech.koin.feature.store.model.MenuCategoryModel
import `in`.koreatech.koin.feature.store.model.OriginModel
import `in`.koreatech.koin.feature.store.model.OwnerInfoModel
import `in`.koreatech.koin.feature.store.model.StoreDescriptionModel
import `in`.koreatech.koin.feature.store.model.toMenuCategoryModel
import `in`.koreatech.koin.feature.store.model.toStoreIndoModel
import `in`.koreatech.koin.feature.store.model.toStoreInfoModel
import `in`.koreatech.koin.feature.store.navigation.IS_ORDERABLE_SHOP
import `in`.koreatech.koin.feature.store.navigation.STORE_ID
import `in`.koreatech.koin.feature.store.util.toKoreanWeek
import javax.inject.Inject
import kotlinx.collections.immutable.persistentListOf
import kotlinx.collections.immutable.toImmutableList
import kotlinx.coroutines.Job
import org.orbitmvi.orbit.ContainerHost
import org.orbitmvi.orbit.annotation.OrbitExperimental
import org.orbitmvi.orbit.syntax.simple.blockingIntent
import org.orbitmvi.orbit.syntax.simple.intent
import org.orbitmvi.orbit.syntax.simple.postSideEffect
import org.orbitmvi.orbit.syntax.simple.reduce
import org.orbitmvi.orbit.syntax.simple.subIntent
import org.orbitmvi.orbit.viewmodel.container

@Suppress("LongParameterList")
@OptIn(OrbitExperimental::class)
@HiltViewModel
class StoreDetailViewModel @Inject constructor(
    savedStateHandle: SavedStateHandle,
    private val getCartItemUseCase: GetCartItemUseCase,
    private val validateCartItemsUseCase: ValidateCartItemsUseCase,
    private val getCartSummaryUseCase: GetCartSummaryUseCase,
    private val getOrderShopOriginInfoUseCase: GetOrderShopOriginInfoUseCase,
    private val getOrderShopSummaryUseCase: GetOrderShopSummaryUseCase,
    private val getOrderShopMenuUseCase: GetOrderShopMenuUseCase,
    private val getStoreWithMenuUseCase: GetStoreWithMenuUseCase,
    private val getShopMenusUseCase: GetShopMenusUseCase,
    private val getShopEventsUseCase: GetShopEventsUseCase,
    private val getStoreReviewUseCase: GetStoreReviewUseCase,
    private val getCartItemsCountUseCase: GetCartItemsCountUseCase,
    private val isTokenSavedInDeviceUseCase: IsTokenSavedInDeviceUseCase
) : ViewModel(), ContainerHost<StoreDetailState, StoreDetailSideEffect> {
    override val container =
        container<StoreDetailState, StoreDetailSideEffect>(StoreDetailState()) {
            val storeId = savedStateHandle.get<Int>(STORE_ID)
            val isOrderableShop = savedStateHandle.get<Boolean>(IS_ORDERABLE_SHOP) ?: true
            checkNotNull(storeId)

            intent {
                reduce {
                    state.copy(
                        storeId = storeId,
                        isOrderableShop = isOrderableShop
                    )
                }

                if (isOrderableShop) {
                    fetchOrderableStore(storeId)
                } else {
                    fetchStore(storeId)
                    fetchEventPreview(storeId)
                }
                checkToken()
            }
        }

    private suspend fun fetchOrderStoreNotice(id: Int) = subIntent {
        getOrderShopOriginInfoUseCase(id).onSuccess { result ->
            reduce {
                state.copy(
                    isLoading = false,
                    shopDescription = StoreDescriptionModel(
                        id = id,
                        storeName = result.name,
                        address = result.address,
                        description = result.introduction,
                        notice = result.notice,
                        phone = result.phone ?: "",
                        openTime = result.openTime,
                        closeTime = result.closeTime,
                        closedDays = result.closedDays.map { it.toKoreanWeek() },
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
        }.onFailure {
            reduce { state.copy(isLoading = false) }
        }
    }

    private suspend fun fetchOrderableStore(id: Int) = subIntent {
        getOrderShopSummaryUseCase(id).onSuccess { result ->
            reduce {
                state.copy(
                    store = result.toStoreIndoModel()
                )
            }
            fetchReview(result.shopId)
            fetchOrderableStoreMenu(id)
        }.onFailure {
            reduce { state.copy(isLoading = false) }
        }
    }

    private suspend fun fetchOrderableStoreMenu(id: Int) = subIntent {
        getOrderShopMenuUseCase(id).onSuccess { result ->
            reduce {
                state.copy(
                    categories = result.map {
                        it.toMenuCategoryModel().copy(
                            isChecked = result.indexOf(it) == 0
                        )
                    }.toImmutableList()
                )
            }
            fetchOrderStoreNotice(id)
        }.onFailure {
            reduce { state.copy(isLoading = false) }
        }
    }

    private suspend fun fetchStore(id: Int) = subIntent {
        getStoreWithMenuUseCase(id).onSuccess { result ->
            reduce {
                state.copy(
                    store = result.toStoreInfoModel(),
                    isLoading = false,
                    shopDescription = StoreDescriptionModel(
                        id = id,
                        storeName = result.name,
                        address = result.address ?: "",
                        description = result.description,
                        notice = result.description,
                        phone = result.phone,
                        openTime = result.open.openTime,
                        closeTime = result.open.closeTime,
                        closedDays = emptyList(),
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
            fetchReview(id)
            fetchMenus(id)
        }.onFailure {
            reduce { state.copy(isLoading = false) }
        }
    }

    private suspend fun fetchEventPreview(id: Int) = subIntent {
        getShopEventsUseCase(id).onSuccess { result ->
            val first = result.events.firstOrNull()
            reduce {
                state.copy(
                    noticePreview = if (first != null) "${first.title}\n${first.content}" else null
                )
            }
        }.onFailure {
            reduce { state.copy(noticePreview = null) }
        }
    }

    private suspend fun fetchMenus(id: Int) = subIntent {
        getShopMenusUseCase(id).onSuccess { shop ->
            reduce {
                state.copy(
                    categories = shop.menuCategories?.map { storeMenuCategories ->
                        MenuCategoryModel(
                            menuGroupId = storeMenuCategories.toMenuCategoryModel().menuGroupId,
                            menuGroupName = storeMenuCategories.toMenuCategoryModel().menuGroupName,
                            menus = storeMenuCategories.toMenuCategoryModel().menus,
                            isChecked = shop.menuCategories?.indexOf(storeMenuCategories) == 0
                        )
                    }?.toImmutableList() ?: persistentListOf()
                )
            }
        }.onFailure {
            reduce { state.copy(categories = persistentListOf()) }
        }
    }

    private suspend fun checkToken() = subIntent {
        val hasToken = isTokenSavedInDeviceUseCase()
        reduce {
            state.copy(
                isLogin = hasToken
            )
        }
    }

    private suspend fun fetchReview(storeId: Int) = subIntent {
        getStoreReviewUseCase(storeId).onSuccess { reviews ->
            reduce {
                state.copy(
                    storeReview = reviews
                )
            }
        }.onFailure {
            reduce { state.copy(isLoading = false) }
        }
    }

    fun setCallDialogState(newState: Boolean) = blockingIntent {
        reduce {
            state.copy(
                showCallDialog = newState
            )
        }
    }

    fun setImageDialogState(newState: Boolean) = blockingIntent {
        reduce {
            state.copy(
                showImageDialog = newState
            )
        }
    }

    fun getCartItemsCount() = intent {
        if (!DeveloperOptionUtil.getDeveloperOption(DeveloperOption.DeliverySprint)) return@intent
        reduce {
            state.copy(isLoading = true)
        }
        getCartItemsCountUseCase().onSuccess { count ->
            reduce {
                state.copy(cartItemCount = count.totalQuantity, isLoading = false)
            }
        }.onFailure {
            reduce {
                state.copy(isLoading = false)
            }
        }
    }

    fun navigateToCart() = intent {
        if (state.isLogin) {
            postSideEffect(StoreDetailSideEffect.NavigateToCart)
        } else {
            reduce {
                state.copy(showSignInDialog = true)
            }
        }
    }

    fun hideSignInDialog() = intent {
        reduce { state.copy(showSignInDialog = false) }
    }

    fun clickMenuCategory(categoryId: Int) = blockingIntent {
        reduce {
            state.copy(
                selectedCategoryId = categoryId
            )
        }
        postSideEffect(StoreDetailSideEffect.ScrollToMenuCategory(categoryId))
    }

    fun changeCategory(categoryId: Int) = blockingIntent {
        reduce {
            state.copy(
                categories = state.categories.map {
                    if (it.menuGroupId == categoryId) {
                        it.copy(isChecked = true)
                    } else {
                        it.copy(isChecked = false)
                    }
                }.toImmutableList()
            )
        }
    }

    fun getCart(type: CartType): Job = intent {
        fetchCart(type)
    }

    private suspend fun fetchCart(type: CartType): Unit = subIntent {
        if (!DeveloperOptionUtil.getDeveloperOption(DeveloperOption.DeliverySprint)) return@subIntent
        reduce { state.copy(isLoading = true) }
        getCartItemUseCase(type.name).onSuccess {
            reduce { state.copy(cart = it, cartType = type, isLoading = false) }
            validateCart()
        }.onFailure {
            reduce { state.copy(isLoading = false) }
            when (it) {
                is KoinStoreException.ShopNotDeliverableException -> fetchCart(CartType.TAKE_OUT)
                is KoinStoreException.ShopNotTakeoutAvailableException -> fetchCart(CartType.TAKE_OUT)
            }
        }
    }

    private suspend fun validateCart() = subIntent {
        if (!DeveloperOptionUtil.getDeveloperOption(DeveloperOption.DeliverySprint)) return@subIntent
        reduce { state.copy(isLoading = true) }
        validateCartItemsUseCase(state.cartType.name).onSuccess {
            reduce {
                state.copy(
                    isLoading = false,
                    cartValidation = CartValidation.VALID
                )
            }
            fetchCartSummary()
        }.onFailure {
            reduce {
                state.copy(
                    cartValidation = when (it) {
                        is KoinStoreException.OrderAmountBelowMinimumException -> CartValidation.AMOUNT_NOT_ENOUGH
                        is KoinStoreException.CartNotFoundException -> CartValidation.CART_NOT_FOUND
                        is KoinStoreException.ShopClosedException -> CartValidation.NOT_OPERATING
                        else -> CartValidation.NONE
                    },
                    isLoading = false
                )
            }
        }
    }

    private suspend fun fetchCartSummary() = subIntent {
        if (!DeveloperOptionUtil.getDeveloperOption(DeveloperOption.DeliverySprint)) return@subIntent
        if (state.cart.orderableShopId == null) return@subIntent
        getCartSummaryUseCase(state.cart.orderableShopId!!).onSuccess {
            reduce {
                state.copy(
                    minimumOrderAmount = it.shopMinimumOrderAmount,
                    isLoading = false
                )
            }
        }.onFailure {
            reduce {
                state.copy(
                    isLoading = false
                )
            }
        }
    }
}
