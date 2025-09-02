package `in`.koreatech.koin.feature.store.cart

import androidx.lifecycle.ViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import `in`.koreatech.koin.domain.error.store.KoinStoreException
import `in`.koreatech.koin.domain.model.cart.CartType
import `in`.koreatech.koin.domain.model.user.User
import `in`.koreatech.koin.domain.usecase.cart.CartMenuQuantityUseCase
import `in`.koreatech.koin.domain.usecase.cart.DeleteCartMenuItemUseCase
import `in`.koreatech.koin.domain.usecase.cart.ResetCartUseCase
import `in`.koreatech.koin.domain.usecase.store.GetCartItemUseCase
import `in`.koreatech.koin.domain.usecase.store.GetCartSummaryUseCase
import `in`.koreatech.koin.domain.usecase.store.ValidateCartItemsUseCase
import `in`.koreatech.koin.domain.usecase.user.GetUserStatusUseCase
import `in`.koreatech.koin.feature.store.enums.CartValidation
import `in`.koreatech.koin.feature.store.model.CartState
import javax.inject.Inject
import kotlinx.coroutines.Job
import org.orbitmvi.orbit.ContainerHost
import org.orbitmvi.orbit.syntax.simple.blockingIntent
import org.orbitmvi.orbit.syntax.simple.intent
import org.orbitmvi.orbit.syntax.simple.reduce
import org.orbitmvi.orbit.viewmodel.container

@HiltViewModel
class ShoppingCartViewModel @Inject constructor(
    private val getCartItemUseCase: GetCartItemUseCase,
    private val validateCartItemsUseCase: ValidateCartItemsUseCase,
    private val cartMenuQuantityUseCase: CartMenuQuantityUseCase,
    private val getCartSummaryUseCase: GetCartSummaryUseCase,
    private val deleteCartMenuItemUseCase: DeleteCartMenuItemUseCase,
    private val resetCartUseCase: ResetCartUseCase,
    private val getUserStatusUseCase: GetUserStatusUseCase
) : ViewModel(), ContainerHost<CartState, Unit> {
    override val container =
        container<CartState, Unit>(CartState())

    init {
        getUserType()
    }

    private fun getUserType() = intent {
        getUserStatusUseCase().collect {
            when (it) {
                is User.Student,
                is User.General -> {
                    getCart(CartType.DELIVERY)
                    reduce {
                        state.copy(isLoggedIn = true)
                    }
                }
                is User.Anonymous -> {
                    // Do nothing
                    reduce {
                        state.copy(isLoggedIn = false)
                    }
                }
            }
        }
    }

    fun getCart(type: CartType): Job = intent {
        reduce { state.copy(isLoading = true) }
        getCartItemUseCase(type.name).onSuccess {
            reduce { state.copy(cart = it, cartType = type, isLoading = false) }
        }.onFailure {
            reduce { state.copy(isLoading = false) }
            when (it) {
                is KoinStoreException.ShopNotDeliverableException -> getCart(CartType.TAKE_OUT)
                is KoinStoreException.ShopNotTakeoutAvailableException -> getCart(CartType.TAKE_OUT)
            }
        }
    }

    fun getCartValidate() = intent {
        reduce { state.copy(isLoading = true) }
        validateCartItemsUseCase(state.cartType.name).onSuccess {
            reduce {
                state.copy(
                    isLoading = false,
                    cartValidation = CartValidation.VALID
                )
            }
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
        getCartSummary()
    }

    private fun getCartSummary() = intent {
        if (state.cart.orderableShopId == null) return@intent
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

    fun resetCartValidation() = blockingIntent {
        reduce {
            state.copy(cartValidation = CartValidation.NONE)
        }
    }

    fun modifyCartMenuQuantity(cartMenuItemId: Int, quantity: Int) = intent {
        reduce {
            state.copy(isLoading = true)
        }
        cartMenuQuantityUseCase(cartMenuItemId, quantity).collect {
            reduce {
                state.copy(
                    isLoading = false,
                    cart = state.cart.copy(
                        items = state.cart.items.map { menuItem ->
                            if (menuItem.cartMenuItemId == cartMenuItemId) {
                                menuItem.copy(quantity = quantity)
                            } else {
                                menuItem
                            }
                        }
                    )
                )
            }
        }
        getCart(state.cartType)
    }

    fun deleteCartMenuItem(cartMenuItemId: Int) = intent {
        deleteCartMenuItemUseCase(cartMenuItemId).collect {
            reduce {
                state.copy(
                    cart = state.cart.copy(
                        items = state.cart.items.filter { menuItem ->
                            menuItem.cartMenuItemId != cartMenuItemId
                        }
                    )
                )
            }
        }
        getCart(state.cartType)
    }

    fun resetCart() = intent {
        resetCartUseCase().collect {
            reduce {
                state.copy(
                    cart = state.cart.copy(
                        items = emptyList(),
                        itemsAmount = 0,
                        deliveryFee = 0,
                        totalAmount = 0,
                        finalPaymentAmount = 0
                    )
                )
            }
        }
    }

    fun setShowDeleteDialog(isVisible: Boolean) = blockingIntent {
        reduce { state.copy(showDeleteDialog = isVisible) }
    }
}
