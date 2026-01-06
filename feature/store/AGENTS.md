# FEATURE Store Module - AGENTS.md

This file provides prescriptive coding guidelines for AI coding agents working in the FEATURE STORE module of the KOIN_ANDROID repository.

## Module Overview

The `feature:store` module provides e-commerce functionality for the KOIN_ANDROID application, including shop browsing, cart management, ordering, and review systems. It follows Clean Architecture with Orbit MVI state management and is one of the most complex feature modules.

## Core Responsibilities

1. **Shop Discovery**: Browse and search shops with filtering
2. **Product Details**: View shop menus and item details
3. **Cart Management**: Add, update, remove items in shopping cart
4. **Order Management**: Place orders, view order history, track status
5. **Review System**: Read and write shop reviews
6. **Category Filtering**: Filter shops by category and features

## Package Structure

```
feature/store/src/main/java/in/koreatech/koin/feature/store/
├── ui/
│   ├── StoreScreen.kt                # Shop list screen
│   ├── StoreDetailScreen.kt          # Shop detail screen
│   ├── ShoppingCartScreen.kt         # Cart management
│   ├── OrderHistoryScreen.kt         # Order history
│   ├── ReviewScreen.kt               # Review list/write
│   └── component/
│       ├── StoreItem.kt              # Shop item card
│       ├── MenuItemCard.kt           # Menu item component
│       ├── CartItemCard.kt           # Cart item component
│       └── ReviewCard.kt             # Review component
├── viewmodel/
│   ├── StoreViewModel.kt             # Shop list state
│   ├── StoreDetailViewModel.kt       # Shop detail state
│   ├── ShoppingCartViewModel.kt      # Cart state
│   ├── OrderHistoryViewModel.kt      # Order history state
│   └── ReviewViewModel.kt            # Review state
├── navigation/
│   └── Navigation.kt                 # Type-safe navigation
└── model/
    └── StoreUiModel.kt               # UI-specific models
```

## Implementation Patterns

### Store ViewModel Pattern (Orbit MVI)

**MUST** use Orbit MVI for all ViewModels.

#### ⚠️ CRITICAL: UseCase Return Types

Store UseCases use **three different return patterns**:

| Return Type | Usage | Example |
|-------------|-------|---------|
| `T` (direct) | Simple data fetch without error handling | `GetStoreWithMenuUseCase`, `GetStoreReviewUseCase` |
| `Result<T>` | Operations that can fail, use `.onSuccess/.onFailure` | `GetCartItemUseCase`, `AddCartItemUseCase`, `ValidateCartItemsUseCase` |
| `Flow<T>` | Streaming operations | `CartMenuQuantityUseCase`, `DeleteCartMenuItemUseCase` |

**Actual StoreDetailViewModel implementation** (from codebase):

```kotlin
@HiltViewModel
class StoreDetailViewModel @Inject constructor(
    savedStateHandle: SavedStateHandle,
    private val getCartItemUseCase: GetCartItemUseCase,
    private val validateCartItemsUseCase: ValidateCartItemsUseCase,
    private val getCartSummaryUseCase: GetCartSummaryUseCase,
    private val getStoreWithMenuUseCase: GetStoreWithMenuUseCase,
    private val getShopMenusUseCase: GetShopMenusUseCase,
    private val getStoreReviewUseCase: GetStoreReviewUseCase,
    private val getCartItemsCountUseCase: GetCartItemsCountUseCase,
    private val isTokenSavedInDeviceUseCase: IsTokenSavedInDeviceUseCase,
    private val getUserStatusUseCase: GetUserStatusUseCase,
    // ... other dependencies
) : ViewModel(), ContainerHost<StoreDetailState, StoreDetailSideEffect> {
    
    override val container = container<StoreDetailState, StoreDetailSideEffect>(StoreDetailState()) {
        val storeId = savedStateHandle.get<Int>(STORE_ID)
        val isOrderableShop = savedStateHandle.get<Boolean>(IS_ORDERABLE_SHOP) ?: true
        checkNotNull(storeId)
        
        getUserType()
        intent {
            reduce { state.copy(storeId = storeId, isOrderableShop = isOrderableShop) }
        }
        
        if (isOrderableShop) {
            fetchOrderableStore(storeId)
        } else {
            fetchStore(storeId)
        }
        fetchReview(storeId)
        checkToken()
    }
    
    // DIRECT RETURN: GetStoreWithMenuUseCase returns StoreWithMenu directly (no Result wrapper)
    private fun fetchStore(id: Int) = intent {
        getStoreWithMenuUseCase(id).also { result ->
            reduce {
                state.copy(
                    store = result.toStoreInfoModel(),
                    isLoading = false,
                    shopDescription = StoreDescriptionModel(
                        id = id,
                        storeName = result.name,
                        address = result.address ?: "",
                        // ... mapping fields
                    )
                )
            }
        }
        fetchMenus(id)
    }
    
    // DIRECT RETURN: GetStoreReviewUseCase returns StoreReview directly
    private fun fetchReview(storeId: Int) = intent {
        getStoreReviewUseCase(storeId).also { reviews ->
            reduce { state.copy(storeReview = reviews) }
        }
    }
    
    // RESULT PATTERN: GetCartItemUseCase returns Result<Cart>
    fun getCart(type: CartType): Job = intent {
        reduce { state.copy(isLoading = true) }
        getCartItemUseCase(type.name).onSuccess {
            reduce { state.copy(cart = it, cartType = type, isLoading = false) }
            getCartValidate()
        }.onFailure {
            reduce { state.copy(isLoading = false) }
            when (it) {
                is KoinStoreException.ShopNotDeliverableException -> getCart(CartType.TAKE_OUT)
                is KoinStoreException.ShopNotTakeoutAvailableException -> getCart(CartType.TAKE_OUT)
            }
        }
    }
    
    // RESULT PATTERN: ValidateCartItemsUseCase returns Result<Unit>
    fun getCartValidate() = intent {
        reduce { state.copy(isLoading = true) }
        validateCartItemsUseCase(state.cartType.name).onSuccess {
            reduce {
                state.copy(isLoading = false, cartValidation = CartValidation.VALID)
            }
            getCartSummary()
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
    
    fun setCallDialogState(newState: Boolean) = blockingIntent {
        reduce { state.copy(showCallDialog = newState) }
    }
    
    fun clickMenuCategory(categoryId: Int) = blockingIntent {
        reduce { state.copy(selectedCategoryId = categoryId) }
        postSideEffect(StoreDetailSideEffect.CollapseToolbar)
    }
    
    fun navigateToCart() = intent {
        if (state.isLoggedIn) {
            postSideEffect(StoreDetailSideEffect.NavigateToCart)
        } else {
            reduce { state.copy(showSignInDialog = true) }
        }
    }
}
```

```

**Rules**:
- **MUST** use SavedStateHandle with `savedStateHandle.get<Type>(KEY)` for navigation arguments
- **MUST** handle all domain exceptions properly
- **MUST** show appropriate error messages for each exception type
- **MUST** use `intent { }` for async operations
- **MUST** use `blockingIntent { }` for sync state updates
- **MUST** check return type of UseCase - NOT all return `Result<T>`

### Shopping Cart ViewModel Pattern

**Actual ShoppingCartViewModel implementation** (from codebase):

#### ⚠️ IMPORTANT: Cart UseCases are in TWO packages

```kotlin
// Store package - returns Result<T>
import `in`.koreatech.koin.domain.usecase.store.GetCartItemUseCase
import `in`.koreatech.koin.domain.usecase.store.ValidateCartItemsUseCase

// Cart package - returns Flow<Unit>
import `in`.koreatech.koin.domain.usecase.cart.CartMenuQuantityUseCase
import `in`.koreatech.koin.domain.usecase.cart.DeleteCartMenuItemUseCase
import `in`.koreatech.koin.domain.usecase.cart.ResetCartUseCase
```

```kotlin
@HiltViewModel
class ShoppingCartViewModel @Inject constructor(
    private val getCartItemUseCase: GetCartItemUseCase,           // Result<Cart>
    private val validateCartItemsUseCase: ValidateCartItemsUseCase, // Result<Unit>
    private val cartMenuQuantityUseCase: CartMenuQuantityUseCase,   // Flow<Unit>
    private val getCartSummaryUseCase: GetCartSummaryUseCase,
    private val deleteCartMenuItemUseCase: DeleteCartMenuItemUseCase, // Flow<Unit>
    private val resetCartUseCase: ResetCartUseCase,                 // Flow<Unit>
    private val getUserStatusUseCase: GetUserStatusUseCase
) : ViewModel(), ContainerHost<CartState, Unit> {
    
    override val container = container<CartState, Unit>(CartState())
    
    init {
        getUserType()
    }
    
    private fun getUserType() = intent {
        getUserStatusUseCase().collect {
            when (it) {
                is User.Student, is User.General -> {
                    getCart(CartType.DELIVERY)
                    reduce { state.copy(isLoggedIn = true) }
                }
                is User.Anonymous -> {
                    reduce { state.copy(isLoggedIn = false) }
                }
            }
        }
    }
    
    // RESULT PATTERN: GetCartItemUseCase(type: String): Result<Cart>
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
    
    // RESULT PATTERN: ValidateCartItemsUseCase(orderType: String): Result<Unit>
    fun getCartValidate() = intent {
        reduce { state.copy(isLoading = true) }
        validateCartItemsUseCase(state.cartType.name).onSuccess {
            reduce { state.copy(isLoading = false, cartValidation = CartValidation.VALID) }
            getCartSummary()
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
    
    // FLOW PATTERN: CartMenuQuantityUseCase(cartMenuItemId: Int, quantity: Int): Flow<Unit>
    fun modifyCartMenuQuantity(cartMenuItemId: Int, quantity: Int) = intent {
        reduce { state.copy(isLoading = true) }
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
    
    // FLOW PATTERN: DeleteCartMenuItemUseCase(cartMenuItemId: Int): Flow<Unit>
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
    
    // FLOW PATTERN: ResetCartUseCase(): Flow<Unit>
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
```

### Navigation Pattern

The store module uses **two different navigation patterns**:

#### Pattern 1: String-based SavedStateHandle (used in store detail)
```kotlin
// Navigation keys
const val STORE_ID = "storeId"
const val IS_ORDERABLE_SHOP = "isOrderableShop"

// ViewModel extraction
@HiltViewModel
class StoreDetailViewModel @Inject constructor(
    savedStateHandle: SavedStateHandle,
    // ... use cases
) : ViewModel(), ContainerHost<StoreDetailState, StoreDetailSideEffect> {
    
    override val container = container<StoreDetailState, StoreDetailSideEffect>(StoreDetailState()) {
        val storeId = savedStateHandle.get<Int>(STORE_ID)
        val isOrderableShop = savedStateHandle.get<Boolean>(IS_ORDERABLE_SHOP) ?: true
        checkNotNull(storeId)
        // ... initialization
    }
}
```

#### Pattern 2: Type-Safe Navigation with toRoute (used in review screens)
```kotlin
// Navigation type
@Serializable
data class StoreReviewHome(
    val storeNavigationData: StoreNavigationData,
    val storeName: String
)

// Custom NavType for complex data
object StoreNavigationDataType : NavType<StoreNavigationData>(isNullableAllowed = false) {
    // ... serialization implementation
}

// ViewModel extraction with toRoute
@HiltViewModel
class ReviewViewModel @Inject constructor(
    savedStateHandle: SavedStateHandle,
    // ... use cases
) : ViewModel(), ContainerHost<ReviewState, ReviewSideEffect> {
    override val container = container<ReviewState, ReviewSideEffect>(ReviewState()) {
        val route = savedStateHandle.toRoute<StoreReviewNavType.StoreReviewHome>(
            typeMap = mapOf(typeOf<StoreNavigationData>() to StoreNavigationDataType)
        )
        
        blockingIntent {
            reduce {
                state.copy(
                    storeNavigationData = route.storeNavigationData,
                    storeName = route.storeName
                )
            }
        }
    }
}
```

**When to use which**:
| Pattern | Use When |
|---------|----------|
| String-based `get<Type>(KEY)` | Simple primitive arguments (Int, String, Boolean) |
| Type-safe `toRoute<T>()` | Complex data objects with custom NavType |

### Store UI Components Pattern

**MUST** follow Compose two-function pattern:

```kotlin
@Composable
fun StoreDetailScreen(
    modifier: Modifier = Modifier,
    onBackClick: () -> Unit = {},
    onCartClick: () -> Unit = {},
    viewModel: StoreDetailViewModel = hiltViewModel()
) {
    val uiState by viewModel.collectAsState()
    val context = LocalContext.current
    val navigator = LocalNavigator.current
    
    viewModel.collectSideEffect { sideEffect ->
        when (sideEffect) {
            is StoreDetailSideEffect.ItemAddedToCart -> {
                Toast.makeText(
                    context,
                    "${sideEffect.itemName} added to cart",
                    Toast.LENGTH_SHORT
                ).show()
            }
            is StoreDetailSideEffect.CallStore -> {
                navigator.navigateToPhone(context, sideEffect.phoneNumber)
            }
            is StoreDetailSideEffect.ShowDifferentShopWarning -> {
                // Show dialog for cart conflict
            }
            is StoreDetailSideEffect.ShowSoldOutError -> {
                Toast.makeText(
                    context,
                    "${sideEffect.itemName} is sold out",
                    Toast.LENGTH_LONG
                ).show()
            }
            is StoreDetailSideEffect.ShowError -> {
                Toast.makeText(context, sideEffect.message, Toast.LENGTH_SHORT).show()
            }
            is StoreDetailSideEffect.NavigateToCart -> {
                onCartClick()
            }
        }
    }
    
    StoreDetailScreenImpl(
        uiState = uiState,
        onBackClick = onBackClick,
        onCategorySelect = viewModel::selectCategory,
        onAddToCart = viewModel::addToCart,
        onCallClick = viewModel::callStore,
        onCartClick = onCartClick,
        modifier = modifier
    )
}

@Composable
fun StoreDetailScreenImpl(
    uiState: StoreDetailState,
    onBackClick: () -> Unit,
    onCategorySelect: (MenuCategory) -> Unit,
    onAddToCart: (MenuItem, Int) -> Unit,
    onCallClick: () -> Unit,
    onCartClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text(uiState.store?.name ?: "Store") },
                navigationIcon = {
                    IconButton(onClick = onBackClick) {
                        Icon(Icons.Default.ArrowBack, "Back")
                    }
                },
                actions = {
                    IconButton(onClick = onCartClick) {
                        Icon(Icons.Default.ShoppingCart, "Cart")
                    }
                }
            )
        },
        modifier = modifier
    ) { padding ->
        if (uiState.isLoading) {
            LoadingIndicator(modifier = Modifier.padding(padding))
        } else if (!uiState.hasStore) {
            EmptyState(modifier = Modifier.padding(padding))
        } else {
            StoreDetailContent(
                store = uiState.store!!,
                categories = uiState.menuCategories,
                selectedCategory = uiState.selectedCategory,
                menuItems = uiState.displayMenuItems,
                reviews = uiState.reviews,
                averageRating = uiState.averageRating,
                onCategorySelect = onCategorySelect,
                onAddToCart = onAddToCart,
                onCallClick = onCallClick,
                modifier = Modifier.padding(padding)
            )
        }
    }
}

@Preview(showBackground = true)
@Preview(showBackground = true, uiMode = Configuration.UI_MODE_NIGHT_YES)
@Composable
private fun StoreDetailScreenPreview() {
    val sampleStore = Store(
        id = 1,
        name = "Sample Store",
        phone = "010-1234-5678",
        address = "123 Main St",
        isOpen = true,
        menuCategories = listOf(
            MenuCategory(id = 1, name = "Main Dishes"),
            MenuCategory(id = 2, name = "Side Dishes")
        ),
        menuItems = listOf(
            MenuItem(id = 1, name = "Burger", price = 8000, categoryId = 1),
            MenuItem(id = 2, name = "Fries", price = 3000, categoryId = 2)
        )
    )
    
    RebrandKoinTheme {
        StoreDetailScreenImpl(
            uiState = StoreDetailState(store = sampleStore),
            onBackClick = {},
            onCategorySelect = {},
            onAddToCart = { _, _ -> },
            onCallClick = {},
            onCartClick = {}
        )
    }
}
```

## Exception Handling

**MUST** handle all store-specific exceptions:

```kotlin
// Located at: domain/src/main/java/in/koreatech/koin/domain/error/store/KoinStoreException.kt
sealed class KoinStoreException : Exception() {
    class DifferentShopItemInCartException : KoinStoreException()
    class MenuSoldOutException : KoinStoreException()
    class ShopClosedException : KoinStoreException()          // Store closed, not "StoreClosed"
    class ShopNotDeliverableException : KoinStoreException()
    class ShopNotTakeoutAvailableException : KoinStoreException()
    class OrderAmountBelowMinimumException : KoinStoreException()
    class CartNotFoundException : KoinStoreException()
    class UnauthorizedException : KoinStoreException()
    class BadRequestException : KoinStoreException()
}

// Actual exception handling pattern from ShoppingCartViewModel
getCartItemUseCase(type.name).onSuccess {
    reduce { state.copy(cart = it, cartType = type, isLoading = false) }
}.onFailure {
    reduce { state.copy(isLoading = false) }
    when (it) {
        is KoinStoreException.ShopNotDeliverableException -> getCart(CartType.TAKE_OUT)
        is KoinStoreException.ShopNotTakeoutAvailableException -> getCart(CartType.TAKE_OUT)
    }
}

// Cart validation exception handling
validateCartItemsUseCase(state.cartType.name).onFailure {
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
```

## UseCase Reference (ACTUAL Signatures)

### Store UseCases (`domain.usecase.store.*`)

| UseCase | Signature | Return Type | Notes |
|---------|-----------|-------------|-------|
| `GetStoreWithMenuUseCase` | `invoke(storeId: Int)` | `StoreWithMenu` | Direct return, no Result wrapper |
| `GetStoreReviewUseCase` | `invoke(shopId: Int)` | `StoreReview` | Direct return, singular name |
| `GetCartItemUseCase` | `invoke(type: String)` | `Result<Cart>` | Requires cart type (DELIVERY/TAKE_OUT) |
| `AddCartItemUseCase` | `invoke(cartAdd: CartAdd)` | `Result<Unit>` | |
| `DeleteCartItemUseCase` | `invoke(cartMenuItemId: Int)` | `Result<Unit>` | |
| `UpdateCartItemUseCase` | `invoke(cartMenuItemId: Int, cartItem: CartItem)` | `Result<Unit>` | |
| `ValidateCartItemsUseCase` | `invoke(orderType: String)` | `Result<Unit>` | |
| `GetCartItemsCountUseCase` | `invoke()` | `Result<CartItemsCount>` | |
| `GetCartSummaryUseCase` | `invoke(orderableShopId: Int)` | `Result<CartSummary>` | |

### Cart UseCases (`domain.usecase.cart.*`)

| UseCase | Signature | Return Type | Notes |
|---------|-----------|-------------|-------|
| `CartMenuQuantityUseCase` | `invoke(cartMenuItemId: Int, quantity: Int)` | `Flow<Unit>` | Returns Flow, use `.collect {}` |
| `DeleteCartMenuItemUseCase` | `invoke(cartMenuItemId: Int)` | `Flow<Unit>` | Returns Flow |
| `ResetCartUseCase` | `invoke()` | `Flow<Unit>` | Returns Flow |

## Testing Guidelines

### Store ViewModel Testing

```kotlin
@ExperimentalCoroutinesApi
class StoreDetailViewModelTest {
    
    private lateinit var viewModel: StoreDetailViewModel
    private lateinit var mockGetStoreUseCase: GetStoreWithMenuUseCase
    private lateinit var mockGetCartItemUseCase: GetCartItemUseCase
    private lateinit var mockGetStoreReviewUseCase: GetStoreReviewUseCase
    private lateinit var savedStateHandle: SavedStateHandle
    
    @Before
    fun setup() {
        mockGetStoreUseCase = mockk()
        mockGetCartItemUseCase = mockk()
        mockGetStoreReviewUseCase = mockk()
        
        // Use string-based key, not type-safe route
        savedStateHandle = SavedStateHandle(mapOf(
            "storeId" to 1,
            "isOrderableShop" to false
        ))
        
        viewModel = StoreDetailViewModel(
            savedStateHandle = savedStateHandle,
            getStoreWithMenuUseCase = mockGetStoreUseCase,
            getCartItemUseCase = mockGetCartItemUseCase,
            getStoreReviewUseCase = mockGetStoreReviewUseCase,
            // ... other dependencies
        )
    }
    
    @Test
    fun `fetchStore updates state with store data`() = runTest {
        // Given: GetStoreWithMenuUseCase returns StoreWithMenu DIRECTLY (no Result wrapper)
        val sampleStore = StoreWithMenu(
            id = 1,
            name = "Test Store",
            phone = "010-1234-5678",
            // ... other fields
        )
        
        coEvery { mockGetStoreUseCase(1) } returns sampleStore  // Direct return, NOT Result.success()
        coEvery { mockGetStoreReviewUseCase(1) } returns mockStoreReview
        
        // When
        // Note: fetchStore is private, called during init via container block
        
        // Then
        assertEquals(sampleStore.name, viewModel.container.stateFlow.value.store?.name)
        assertFalse(viewModel.container.stateFlow.value.isLoading)
    }
    
    @Test
    fun `getCart handles ShopNotDeliverableException by retrying with TAKE_OUT`() = runTest {
        // Given: GetCartItemUseCase returns Result<Cart>
        coEvery { mockGetCartItemUseCase(CartType.DELIVERY.name) } returns 
            Result.failure(KoinStoreException.ShopNotDeliverableException())
        coEvery { mockGetCartItemUseCase(CartType.TAKE_OUT.name) } returns 
            Result.success(mockCart)
        
        // When
        viewModel.getCart(CartType.DELIVERY)
        
        // Then
        coVerify { mockGetCartItemUseCase(CartType.TAKE_OUT.name) }
    }
}
```

## Import Organization

```kotlin
// 1. AndroidX/Lifecycle imports
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel

// 2. Compose imports (for UI files)
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier

// 3. Navigation imports
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.toRoute

// 4. Dagger/Hilt imports
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

// 5. Domain imports (store UseCase - Result<T> and direct returns)
import `in`.koreatech.koin.domain.usecase.store.GetStoreWithMenuUseCase
import `in`.koreatech.koin.domain.usecase.store.GetStoreReviewUseCase
import `in`.koreatech.koin.domain.usecase.store.GetCartItemUseCase
import `in`.koreatech.koin.domain.usecase.store.AddCartItemUseCase
import `in`.koreatech.koin.domain.usecase.store.ValidateCartItemsUseCase

// 6. Domain imports (cart UseCase - Flow returns)
import `in`.koreatech.koin.domain.usecase.cart.CartMenuQuantityUseCase
import `in`.koreatech.koin.domain.usecase.cart.DeleteCartMenuItemUseCase
import `in`.koreatech.koin.domain.usecase.cart.ResetCartUseCase

// 7. Domain model imports
import `in`.koreatech.koin.domain.model.store.StoreWithMenu
import `in`.koreatech.koin.domain.model.store.StoreReview
import `in`.koreatech.koin.domain.model.store.Cart
import `in`.koreatech.koin.domain.model.cart.CartType
import `in`.koreatech.koin.domain.error.store.KoinStoreException

// 8. Kotlin/Coroutines imports
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.collect
import kotlinx.collections.immutable.toImmutableList

// 9. Orbit MVI imports
import org.orbitmvi.orbit.ContainerHost
import org.orbitmvi.orbit.viewmodel.container
import org.orbitmvi.orbit.syntax.simple.intent
import org.orbitmvi.orbit.syntax.simple.blockingIntent
import org.orbitmvi.orbit.syntax.simple.reduce
import org.orbitmvi.orbit.syntax.simple.postSideEffect

// 10. Core/Feature imports
import `in`.koreatech.koin.core.designsystem.theme.RebrandKoinTheme
import `in`.koreatech.koin.feature.store.navigation.STORE_ID
import `in`.koreatech.koin.feature.store.enums.CartValidation
```

## Critical Rules

These rules are **non-negotiable**:

1. **MVI Pattern**: **MUST** use Orbit MVI for all ViewModels
2. **UseCase Return Types**: **MUST** check actual return type - NOT all UseCases return `Result<T>`:
   - Direct return: `GetStoreWithMenuUseCase`, `GetStoreReviewUseCase`
   - `Result<T>`: `GetCartItemUseCase`, `AddCartItemUseCase`, `ValidateCartItemsUseCase`
   - `Flow<T>`: `CartMenuQuantityUseCase`, `DeleteCartMenuItemUseCase`, `ResetCartUseCase`
3. **UseCase Packages**: Cart operations are split between `domain.usecase.store.*` and `domain.usecase.cart.*`
4. **Navigation**: Use `savedStateHandle.get<Type>(KEY)` for simple args, `toRoute<T>()` for complex data
5. **Exception Handling**: **MUST** handle all store-specific exceptions in `KoinStoreException`
6. **Cart Type**: **MUST** pass cart type (DELIVERY/TAKE_OUT) to `GetCartItemUseCase` and `ValidateCartItemsUseCase`
7. **State Consistency**: **MUST** ensure cart and order state consistency
8. **Compose Pattern**: **MUST** follow two-function Compose pattern

## Build Commands

```bash
# Build store module
./gradlew :feature:store:build

# Run store tests
./gradlew :feature:store:test

# Run UI tests
./gradlew :feature:store:connectedAndroidTest
```

## Store Module Best Practices

1. **Cart Management**: Always validate cart items before checkout
2. **Error Handling**: Provide clear error messages for each failure scenario
3. **State Consistency**: Keep cart state synchronized across screens
4. **Performance**: Use efficient image loading and lazy loading for menus
5. **User Experience**: Provide clear feedback for all operations

---

**Last Updated**: 2026-01-06  
**For**: AI Coding Agents working on FEATURE STORE module  
**Maintainers**: BCSD Android Track