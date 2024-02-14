package `in`.koreatech.koin.domain.util.ext

import `in`.koreatech.koin.domain.model.store.Store

fun List<Store>.sortedOpenStore() = this.sortedWith { store, otherStore ->
    when {
        !store.open.closed && store.open.openStore() -> -1
        !otherStore.open.closed && otherStore.open.openStore() -> 1
        else -> 0
    }
}