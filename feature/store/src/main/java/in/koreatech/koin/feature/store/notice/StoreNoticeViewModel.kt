package `in`.koreatech.koin.feature.store.notice

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import `in`.koreatech.koin.domain.usecase.store.GetShopEventsUseCase
import `in`.koreatech.koin.domain.usecase.store.GetStoreWithMenuUseCase
import `in`.koreatech.koin.feature.store.navigation.STORE_ID
import javax.inject.Inject
import kotlinx.collections.immutable.persistentListOf
import kotlinx.collections.immutable.toImmutableList
import org.orbitmvi.orbit.ContainerHost
import org.orbitmvi.orbit.annotation.OrbitExperimental
import org.orbitmvi.orbit.syntax.simple.blockingIntent
import org.orbitmvi.orbit.syntax.simple.intent
import org.orbitmvi.orbit.syntax.simple.reduce
import org.orbitmvi.orbit.syntax.simple.subIntent
import org.orbitmvi.orbit.viewmodel.container
import timber.log.Timber

@HiltViewModel
@OptIn(OrbitExperimental::class)
class StoreNoticeViewModel @Inject constructor(
    savedStateHandle: SavedStateHandle,
    private val getShopEventsUseCase: GetShopEventsUseCase,
    private val getStoreWithMenuUseCase: GetStoreWithMenuUseCase
) : ViewModel(), ContainerHost<StoreNoticeListState, Unit> {

    override val container = container<StoreNoticeListState, Unit>(StoreNoticeListState()) {
        val storeId = checkNotNull(savedStateHandle.get<Int>(STORE_ID))
        intent {
            fetchStoreName(storeId)
            fetchNotices(storeId)
        }
    }

    private suspend fun fetchStoreName(storeId: Int) = subIntent {
        getStoreWithMenuUseCase(storeId).onSuccess { result ->
            reduce { state.copy(storeName = result.name) }
        }.onFailure {
            Timber.e(it)
        }
    }

    private suspend fun fetchNotices(storeId: Int) = subIntent {
        reduce { state.copy(isFirstPageLoading = true) }
        getShopEventsUseCase(storeId).onSuccess { result ->
            reduce {
                state.copy(
                    isFirstPageLoading = false,
                    notices = result.events.map { event ->
                        StoreNoticeItemState(
                            id = event.eventId,
                            title = event.title,
                            description = event.content,
                            dateRange = "${event.startDate} - ${event.endDate}",
                            imageUris = event.thumbnailImages?.toImmutableList() ?: persistentListOf()
                        )
                    }.toImmutableList()
                )
            }
        }.onFailure {
            reduce { state.copy(isFirstPageLoading = false) }
        }
    }

    fun expandNotice(id: Int) = blockingIntent {
        reduce {
            state.copy(
                notices = state.notices.map {
                    if (it.id == id) it.copy(isExpanded = true) else it
                }.toImmutableList()
            )
        }
    }

    fun foldNotice(id: Int) = blockingIntent {
        reduce {
            state.copy(
                notices = state.notices.map {
                    if (it.id == id) it.copy(isExpanded = false) else it
                }.toImmutableList()
            )
        }
    }
}
