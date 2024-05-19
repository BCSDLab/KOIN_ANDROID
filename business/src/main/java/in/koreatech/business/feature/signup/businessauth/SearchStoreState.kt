package `in`.koreatech.business.feature.signup.businessauth

import `in`.koreatech.koin.domain.model.store.Store
import kotlinx.coroutines.Job

data class SearchStoreState(
    val search: String= "",
    val stores: List<Store> = emptyList(),
    val itemIndex: Int = -1,
    val searchJob: Job? = null
)