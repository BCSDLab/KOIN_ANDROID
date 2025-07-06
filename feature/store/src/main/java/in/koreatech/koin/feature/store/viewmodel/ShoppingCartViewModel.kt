package `in`.koreatech.koin.feature.store.viewmodel

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import `in`.koreatech.koin.domain.usecase.cart.CartUseCase
import `in`.koreatech.koin.feature.store.view.CartSideEffect
import `in`.koreatech.koin.feature.store.view.CartState
import `in`.koreatech.koin.feature.store.view.StoreDetailSideEffect
import `in`.koreatech.koin.feature.store.view.StoreDetailState
import org.orbitmvi.orbit.ContainerHost
import org.orbitmvi.orbit.syntax.simple.intent
import org.orbitmvi.orbit.syntax.simple.reduce
import org.orbitmvi.orbit.viewmodel.container
import javax.inject.Inject

@HiltViewModel
class ShoppingCartViewModel @Inject constructor(
    savedStateHandle: SavedStateHandle,
    private val cartUseCase: CartUseCase,
) : ViewModel(), ContainerHost<CartState, CartSideEffect> {
    override val container =
        container<CartState, CartSideEffect>(CartState()) {
            val storeId = savedStateHandle.get<Int>(STORE_ID)
        }

    init {
        getCart()
    }

    fun getCart() = intent {
        cartUseCase().collect { cart ->
            reduce { state.copy(cart = cart) }
        }
    }

    fun getCartValidate() = intent {
        cartValidateUseCase().collect { cartValidate ->
            reduce { state.copy(cartValidate = cartValidate) }
        }
    }
    companion object {
        const val STORE_ID = "storeId"
    }
}
