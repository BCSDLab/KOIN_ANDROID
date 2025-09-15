package `in`.koreatech.koin.feature.store.orderhistory

import androidx.lifecycle.ViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject
import org.orbitmvi.orbit.ContainerHost
import org.orbitmvi.orbit.viewmodel.container

@HiltViewModel
class StoreOrderHistoryViewModel @Inject constructor() : ViewModel(), ContainerHost<StoreOrderHistoryState, StoreOrderHistorySideEffect> {
    override val container = container<StoreOrderHistoryState, StoreOrderHistorySideEffect>(StoreOrderHistoryState())

}
