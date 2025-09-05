package `in`.koreatech.koin.feature.store.origin

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import `in`.koreatech.koin.domain.model.user.User
import `in`.koreatech.koin.domain.usecase.orderShop.GetOrderShopOriginInfoUseCase
import `in`.koreatech.koin.domain.usecase.store.GetCartItemsCountUseCase
import `in`.koreatech.koin.domain.usecase.user.GetUserStatusUseCase
import `in`.koreatech.koin.feature.store.model.DeliveryTipModel
import `in`.koreatech.koin.feature.store.model.OriginModel
import `in`.koreatech.koin.feature.store.model.OwnerInfoModel
import `in`.koreatech.koin.feature.store.model.StoreDescriptionModel
import `in`.koreatech.koin.feature.store.navigation.IS_ORDERABLE_SHOP
import `in`.koreatech.koin.feature.store.navigation.STORE_ID
import `in`.koreatech.koin.feature.store.util.toKoreanWeek
import javax.inject.Inject
import org.orbitmvi.orbit.ContainerHost
import org.orbitmvi.orbit.syntax.simple.intent
import org.orbitmvi.orbit.syntax.simple.reduce
import org.orbitmvi.orbit.viewmodel.container

@HiltViewModel
class ShopOriginViewModel @Inject constructor(
    savedStateHandle: SavedStateHandle,
    private val getCartItemsCountUseCase: GetCartItemsCountUseCase,
    private val getOrderShopOriginInfoUseCase: GetOrderShopOriginInfoUseCase,
    private val getUserStatusUseCase: GetUserStatusUseCase
) : ViewModel(), ContainerHost<ShopOriginState, Unit> {
    override val container = container<ShopOriginState, Unit>(ShopOriginState()) {
        val storeId = savedStateHandle.get<Int>(STORE_ID)
        val isOrderableShop = savedStateHandle.get<Boolean>(IS_ORDERABLE_SHOP) ?: true
        checkNotNull(storeId)

        if (isOrderableShop) {
            fetchOrderStoreNotice(storeId)
        }
    }

    init {
        getUserType()
    }

    private fun getUserType() = intent {
        getUserStatusUseCase().collect {
            when (it) {
                is User.Student,
                is User.General -> {
                    reduce {
                        state.copy(isLoggedIn = true)
                    }
                }
                is User.Anonymous -> {
                    reduce {
                        state.copy(isLoggedIn = false)
                    }
                }
            }
        }
    }

    fun getCartItemsCount() = intent {
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

    private fun fetchOrderStoreNotice(id: Int) = intent {
        getOrderShopOriginInfoUseCase(id).also { result ->
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
        }
    }
}
