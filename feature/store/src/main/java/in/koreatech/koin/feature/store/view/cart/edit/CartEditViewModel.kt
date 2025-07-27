package `in`.koreatech.koin.feature.store.view.cart.edit

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import `in`.koreatech.koin.domain.error.store.KoinStoreException
import `in`.koreatech.koin.domain.model.store.AddCartItemOption
import `in`.koreatech.koin.domain.model.store.CartItem
import `in`.koreatech.koin.domain.usecase.store.GetCartItemEditUseCase
import `in`.koreatech.koin.domain.usecase.store.UpdateCartItemUseCase
import `in`.koreatech.koin.feature.store.enums.CartError
import `in`.koreatech.koin.feature.store.model.toLocalShopMenuOptionGroup
import `in`.koreatech.koin.feature.store.model.toLocalShopPrice
import `in`.koreatech.koin.feature.store.navigation.CART_MENU_ITEM_ID
import javax.inject.Inject
import org.orbitmvi.orbit.ContainerHost
import org.orbitmvi.orbit.syntax.simple.intent
import org.orbitmvi.orbit.syntax.simple.postSideEffect
import org.orbitmvi.orbit.syntax.simple.reduce
import org.orbitmvi.orbit.viewmodel.container

@HiltViewModel
class CartEditViewModel @Inject constructor(
    savedStateHandle: SavedStateHandle,
    private val getCartItemEditUseCase: GetCartItemEditUseCase,
    private val updateCartItemUseCase: UpdateCartItemUseCase
) : ViewModel(), ContainerHost<CartEditState, CartEditSideEffect> {
    override val container = container<CartEditState, CartEditSideEffect>(CartEditState()) {
        val cartMenuItemId = savedStateHandle.get<Int>(CART_MENU_ITEM_ID)

        checkNotNull(cartMenuItemId)

        intent {
            reduce {
                state.copy(
                    cartMenuItemId = cartMenuItemId
                )
            }
        }

        getCart()
    }

    private fun getCart() = intent {
        getCartItemEditUseCase(
            cartMenuItemId = state.cartMenuItemId
        ).onSuccess {
            reduce {
                state.copy(
                    menuName = it.name,
                    menuDescription = it.description,
                    menuImageUrls = it.images,
                    prices = it.prices.map { it.toLocalShopPrice() },
                    options = it.optionGroups.map { it.toLocalShopMenuOptionGroup() },
                    orderableShopMenuPriceId = it.prices.filter { it.isSelected }.getOrNull(0)?.id ?: -1,
                )
            }
        }.onFailure {
            postSideEffect(CartEditSideEffect.UnknownError)
        }
    }

    fun updateCartItem() = intent {
        updateCartItemUseCase(
            cartMenuItemId = state.cartMenuItemId,
            cartItem = CartItem(
                orderableShopMenuPriceId = state.orderableShopMenuPriceId,
                options = state.options.flatMap {
                    it.options.filter { option -> option.optionSelected }.map { option ->
                        AddCartItemOption(
                            optionGroupId = it.id,
                            optionId = option.id
                        )
                    }
                }
            )
        ).onSuccess {

        }.onFailure { exception ->
            intent {
                when (exception) {
                    is KoinStoreException.RequiredOptionGroupMissingException -> {
                        reduce {
                            state.copy(error = CartError.REQUIRED_OPTION_GROUP_MISSING, showErrorDialog = true)
                        }
                    }

                    is KoinStoreException.MinimumSelectionNotMetException -> {
                        reduce {
                            state.copy(error = CartError.MIN_SELECTION_NOT_MET, showErrorDialog = true)
                        }
                    }

                    is KoinStoreException.MaxSelectionExceededException -> {
                        reduce {
                            state.copy(error = CartError.MAX_SELECTION_EXCEEDED, showErrorDialog = true)
                        }
                    }

                    is KoinStoreException.InvalidMenuInShopException -> {
                        reduce {
                            state.copy(error = CartError.INVALID_MENU_IN_SHOP, showErrorDialog = true)
                        }
                    }

                    else -> {
                        postSideEffect(CartEditSideEffect.UnknownError)
                    }
                }
            }
        }
    }

    fun updateMenuPriceId(index: Int) = intent {
        reduce {
            state.copy(orderableShopMenuPriceId = index)
        }
    }

    fun updateSelectedOptionGroup(
        optionGroupId: Int,
        selectedOptionId: Int
    ) = intent {
        reduce {
            state.copy(
                options = state.options.map { optionGroup ->
                    if (optionGroup.id == optionGroupId) {
                        optionGroup.copy(
                            options = optionGroup.options.map { option ->
                                if (option.id == selectedOptionId) {
                                    option.copy(optionSelected = !option.optionSelected)
                                } else {
                                    option
                                }
                            }
                        )
                    } else {
                        optionGroup
                    }
                }
            )
        }
    }

    fun dismissErrorDialog() = intent {
        reduce {
            state.copy(
                showErrorDialog = false,
                error = CartError.NONE
            )
        }
    }
}
