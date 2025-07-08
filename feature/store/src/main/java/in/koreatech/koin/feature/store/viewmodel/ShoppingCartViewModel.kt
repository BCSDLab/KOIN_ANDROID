package `in`.koreatech.koin.feature.store.viewmodel

import androidx.lifecycle.ViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import `in`.koreatech.koin.domain.model.cart.CartType
import `in`.koreatech.koin.domain.usecase.cart.CartMenuQuantityUseCase
import `in`.koreatech.koin.domain.usecase.cart.CartUseCase
import `in`.koreatech.koin.domain.usecase.cart.CartValidateUseCase
import `in`.koreatech.koin.domain.usecase.cart.DeleteCartMenuItemUseCase
import `in`.koreatech.koin.domain.usecase.cart.ResetCartUseCase
import `in`.koreatech.koin.feature.store.view.CartState
import org.orbitmvi.orbit.ContainerHost
import org.orbitmvi.orbit.syntax.simple.blockingIntent
import org.orbitmvi.orbit.syntax.simple.intent
import org.orbitmvi.orbit.syntax.simple.reduce
import org.orbitmvi.orbit.viewmodel.container
import javax.inject.Inject

@HiltViewModel
class ShoppingCartViewModel @Inject constructor(
    private val cartUseCase: CartUseCase,
    private val cartValidateUseCase: CartValidateUseCase,
    private val cartMenuQuantityUseCase: CartMenuQuantityUseCase,
    private val deleteCartMenuItemUseCase: DeleteCartMenuItemUseCase,
    private val resetCartUseCase: ResetCartUseCase,
) : ViewModel(), ContainerHost<CartState, Unit> {
    override val container =
        container<CartState, Unit>(CartState())

    init {
        getCart(CartType.DELIVERY)
    }

    fun getCart(type: CartType) = intent {
        cartUseCase(type).collect { cart ->
            reduce { state.copy(cart = cart, cartType = type) }
        }
    }

    fun getCartValidate() = intent {
        cartValidateUseCase().collect { cartValidate ->
            reduce { state.copy(isValidateCart = cartValidate) }
        }
    }

    fun modifyCartMenuQuantity(cartMenuItemId: Int, quantity: Int) = intent {
        cartMenuQuantityUseCase(cartMenuItemId, quantity).collect {
            reduce {
                state.copy(
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
            reduce { state.copy(cart = state.cart.copy(items = emptyList())) }
        }
    }

    fun setShowDeleteDialog(isVisible: Boolean) = blockingIntent {
        reduce { state.copy(showDeleteDialog = isVisible) }
    }

}
