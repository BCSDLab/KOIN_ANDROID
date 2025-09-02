package `in`.koreatech.koin.feature.store.cartadd

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import `in`.koreatech.koin.domain.error.store.KoinStoreException
import `in`.koreatech.koin.domain.model.store.CartAdd
import `in`.koreatech.koin.domain.model.user.User
import `in`.koreatech.koin.domain.usecase.store.AddCartItemUseCase
import `in`.koreatech.koin.domain.usecase.store.GetCartItemsCountUseCase
import `in`.koreatech.koin.domain.usecase.store.GetOrderableShopMenuUseCase
import `in`.koreatech.koin.domain.usecase.store.ResetCartUseCase
import `in`.koreatech.koin.domain.usecase.user.GetUserStatusUseCase
import `in`.koreatech.koin.feature.store.enums.CartError
import `in`.koreatech.koin.feature.store.model.LocalAddCartItemOption
import `in`.koreatech.koin.feature.store.model.LocalCartAdd
import `in`.koreatech.koin.feature.store.model.toAddCartItemOption
import `in`.koreatech.koin.feature.store.model.toLocalShopMenuOptionGroup
import `in`.koreatech.koin.feature.store.model.toLocalShopPrice
import `in`.koreatech.koin.feature.store.navigation.CART_DATA
import `in`.koreatech.koin.feature.store.navigation.ORDERABLE_SHOP_ID
import `in`.koreatech.koin.feature.store.navigation.ORDERABLE_SHOP_MENU_ID
import javax.inject.Inject
import kotlinx.serialization.json.Json
import org.orbitmvi.orbit.ContainerHost
import org.orbitmvi.orbit.syntax.simple.blockingIntent
import org.orbitmvi.orbit.syntax.simple.intent
import org.orbitmvi.orbit.syntax.simple.postSideEffect
import org.orbitmvi.orbit.syntax.simple.reduce
import org.orbitmvi.orbit.viewmodel.container
import timber.log.Timber

@HiltViewModel
class CartAddViewModel @Inject constructor(
    savedStateHandle: SavedStateHandle,
    private val getOrderableShopMenuUseCase: GetOrderableShopMenuUseCase,
    private val getCartItemsCountUseCase: GetCartItemsCountUseCase,
    private val addCartItemUseCase: AddCartItemUseCase,
    private val resetCartUseCase: ResetCartUseCase,
    private val getUserStatusUseCase: GetUserStatusUseCase
) : ViewModel(), ContainerHost<CartAddState, CartAddSideEffect> {
    override val container = container<CartAddState, CartAddSideEffect>(CartAddState()) {
        val orderableShopId = savedStateHandle.get<Int>(ORDERABLE_SHOP_ID)
        val orderableShopMenuId = savedStateHandle.get<Int>(ORDERABLE_SHOP_MENU_ID)
        val cartData = savedStateHandle.get<String>(CART_DATA)

        checkNotNull(orderableShopId)
        checkNotNull(orderableShopMenuId)

        blockingIntent {
            reduce {
                state.copy(
                    orderableShopId = orderableShopId,
                    orderableShopMenuId = orderableShopMenuId
                )
            }
        }

        getMenus()
        getUserType()
        Timber.d("cartData: $cartData")
        if (cartData != null) {
            Json.decodeFromString<LocalCartAdd>(cartData).let {
                blockingIntent {
                    reduce {
                        state.copy(
                            orderableShopMenuId = it.orderableShopMenuId,
                            orderableShopMenuPriceId = it.orderableShopMenuPriceId,
                            orderableShopMenuOptionIds = it.orderableShopMenuOptionIds,
                            quantity = it.quantity
                        )
                    }
                    addCartItem()
                }
            }
        }
    }

    private fun getUserType() = intent {
        getUserStatusUseCase().collect {
            when (it) {
                is User.Student,
                is User.General -> {
                    getCartItemsCount()
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

    private fun getCartItemsCount() = intent {
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

    private fun getMenus() = intent {
        reduce {
            state.copy(isLoading = true)
        }
        getOrderableShopMenuUseCase(
            shopId = state.orderableShopId,
            menuId = state.orderableShopMenuId
        ).onSuccess {
            reduce {
                state.copy(
                    isLoading = false,
                    menuName = it.name,
                    menuDescription = it.description,
                    menuImageUrls = it.images,
                    prices = it.prices.map { it.toLocalShopPrice() },
                    options = it.optionGroups.map { it.toLocalShopMenuOptionGroup() },
                    orderableShopMenuPriceId = if (it.prices.size == 1) {
                        it.prices.first().id
                    } else {
                        -1
                    }
                )
            }
        }.onFailure {
            reduce {
                state.copy(isLoading = false)
            }
        }
    }

    fun showSignInDialog() = blockingIntent {
        reduce { state.copy(showSignInDialog = true) }
    }

    fun hideSignInDialog() = blockingIntent {
        reduce { state.copy(showSignInDialog = false) }
    }

    fun addCartItem() = intent {
        reduce {
            state.copy(isLoading = true)
        }
        addCartItemUseCase(
            CartAdd(
                orderableShopId = state.orderableShopId,
                orderableShopMenuId = state.orderableShopMenuId,
                orderableShopMenuPriceId = state.orderableShopMenuPriceId,
                orderableShopMenuOptionIds = state.orderableShopMenuOptionIds.toAddCartItemOption(),
                quantity = state.quantity
            )
        ).onSuccess {
            reduce {
                state.copy(
                    isLoading = false
                )
            }
            postSideEffect(CartAddSideEffect.CartItemAdded)
        }.onFailure { exception ->
            reduce {
                state.copy(isLoading = false)
            }
            when (exception) {
                is KoinStoreException.DifferentShopItemInCartException -> {
                    reduce {
                        state.copy(error = CartError.DIFFERENT_SHOP_ITEM_IN_CART, showErrorDialog = true)
                    }
                }

                is KoinStoreException.MenuSoldOutException -> {
                    reduce {
                        state.copy(error = CartError.MENU_SOLD_OUT, showErrorDialog = true)
                    }
                }

                is KoinStoreException.RequiredOptionGroupMissingException -> {
                    reduce {
                        state.copy(error = CartError.REQUIRED_OPTION_GROUP_MISSING, showErrorDialog = true)
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

                is KoinStoreException.ShopClosedException -> {
                    reduce {
                        state.copy(error = CartError.SHOP_CLOSED, showErrorDialog = true)
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
    ) = blockingIntent {
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
        reduce {
            state.copy(
                orderableShopMenuOptionIds = state.options.flatMap {
                    it.options.filter { option -> option.optionSelected }.map { option ->
                        LocalAddCartItemOption(
                            optionGroupId = it.id,
                            optionId = option.id
                        )
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

    fun updateQuantity(quantity: Int) = intent {
        reduce {
            state.copy(quantity = quantity)
        }
    }

    fun resetCart() = intent {
        reduce {
            state.copy(isLoading = true)
        }
        resetCartUseCase().onSuccess {
            reduce {
                state.copy(isLoading = false)
            }
            addCartItem() // Call add cart again
            dismissErrorDialog()
        }.onFailure {
            // Should not happen
            reduce {
                state.copy(isLoading = false)
            }
        }
    }
}
