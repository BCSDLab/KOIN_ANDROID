# CORE Network Module - AGENTS.md

This file provides prescriptive coding guidelines for AI coding agents working in the CORE NETWORK module of the KOIN_ANDROID repository.

## Module Overview

The `core:network` module provides network connectivity monitoring and status reporting for the KOIN_ANDROID application. It abstracts Android's ConnectivityManager and provides reactive network state observation.

## Core Responsibilities

1. **Network Monitoring**: Observe network connectivity state changes
2. **Status Reporting**: Provide current network status synchronously and reactively
3. **Reactive Streams**: Emit network state changes via Flow

## Package Structure

```
core/network/src/main/java/in/koreatech/koin/core/network/
├── state/
│   └── NetworkStatus.kt              # Network state sealed class
├── service/
│   ├── NetworkConnectivityService.kt  # Main service interface
│   └── NetworkConnectivityServiceImpl.kt # Implementation
└── di/
    └── NetworkModule.kt              # Dependency injection
```

## Implementation Patterns

### Network Status Sealed Class

The module uses a **sealed class** (NOT an enum) with exactly 2 data objects:

```kotlin
sealed class NetworkStatus {
    data object Connected : NetworkStatus()
    data object Disconnected : NetworkStatus()
}
```

**Rules**:
- **MUST** use sealed class pattern with data objects
- **MUST NOT** add additional states (CONNECTING, LOST, etc. do not exist)
- **MUST** use `data object` for singleton state objects

### Network Connectivity Service Interface

The service interface has exactly **3 members**:

```kotlin
interface NetworkConnectivityService {
    /**
     * Observe network status changes as a Flow
     */
    val networkStatus: Flow<NetworkStatus>
    
    /**
     * Get current network status synchronously
     */
    fun getLatestStatus(): NetworkStatus
    
    /**
     * Check if device is currently connected
     */
    fun isConnected(): Boolean
}
```

**Rules**:
- **MUST** provide Flow-based reactive observation via `networkStatus`
- **MUST** provide synchronous status access via `getLatestStatus()`
- **MUST** provide boolean connectivity check via `isConnected()`
- **MUST NOT** add `connectionType` or `connectionTypeFlow` - these do not exist

### Usage Patterns

**ViewModel Network Monitoring**:

```kotlin
@HiltViewModel
class StoreViewModel @Inject constructor(
    private val getStoresUseCase: GetStoresUseCase,
    private val networkConnectivityService: NetworkConnectivityService
) : ViewModel(), ContainerHost<StoreState, StoreSideEffect> {
    
    override val container = container<StoreState, StoreSideEffect>(StoreState())
    
    init {
        observeNetworkStatus()
    }
    
    private fun observeNetworkStatus() {
        viewModelScope.launch {
            networkConnectivityService.networkStatus.collect { status ->
                when (status) {
                    NetworkStatus.Connected -> {
                        reduce { state.copy(isNetworkAvailable = true) }
                    }
                    NetworkStatus.Disconnected -> {
                        reduce { state.copy(isNetworkAvailable = false) }
                        postSideEffect(StoreSideEffect.ShowNetworkError)
                    }
                }
            }
        }
    }
    
    fun refreshStores() {
        if (!networkConnectivityService.isConnected()) {
            postSideEffect(StoreSideEffect.ShowNetworkError)
            return
        }
        loadStores()
    }
}
```

**Synchronous Check**:

```kotlin
// Check current connectivity
if (networkConnectivityService.isConnected()) {
    // Perform network operation
}

// Get current status object
when (networkConnectivityService.getLatestStatus()) {
    NetworkStatus.Connected -> { /* ... */ }
    NetworkStatus.Disconnected -> { /* ... */ }
}
```

## Critical Rules

These rules are **non-negotiable**:

1. **Sealed Class Pattern**: NetworkStatus is a sealed class with `Connected` and `Disconnected` data objects only
2. **Interface Members**: NetworkConnectivityService has exactly 3 members - no more, no less
3. **No ConnectionType**: There is no ConnectionType enum - do not reference WiFi, Cellular, etc.
4. **Function vs Property**: `getLatestStatus()` and `isConnected()` are functions, not properties
5. **Dependency Injection**: **ALWAYS** inject NetworkConnectivityService via Hilt

## Common Mistakes to Avoid

### Wrong: Using enum with multiple states
```kotlin
// WRONG - This does not exist
enum class NetworkStatus {
    CONNECTED, DISCONNECTED, CONNECTING, LOST
}
```

### Correct: Sealed class with 2 data objects
```kotlin
// CORRECT - Actual implementation
sealed class NetworkStatus {
    data object Connected : NetworkStatus()
    data object Disconnected : NetworkStatus()
}
```

### Wrong: Accessing non-existent properties
```kotlin
// WRONG - These do not exist
val type = networkService.connectionType
val isWifi = networkService.connectionTypeFlow
```

### Correct: Using actual interface members
```kotlin
// CORRECT - Actual interface
val status = networkService.networkStatus.collect { ... }
val current = networkService.getLatestStatus()
val connected = networkService.isConnected()
```

## Build Commands

```bash
# Build network module
./gradlew :core:network:build

# Run network tests
./gradlew :core:network:test
```

---

**Last Updated**: 2026-01-06  
**For**: AI Coding Agents working on CORE NETWORK module  
**Maintainers**: BCSD Android Track
