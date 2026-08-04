package `in`.koreatech.koin.feature.department.state

import kotlinx.collections.immutable.toImmutableList
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import org.orbitmvi.orbit.ContainerHost
import org.orbitmvi.orbit.syntax.simple.intent
import org.orbitmvi.orbit.syntax.simple.reduce

class DepartmentSearchFieldHandler<S, SE : Any>(
    private val host: ContainerHost<S, SE>,
    private val scope: CoroutineScope,
    private val fetch: suspend (keyword: String) -> Result<List<DepartmentState>>,
    private val onSearchLog: (keyword: String) -> Unit = {}
) where S : DepartmentSearchState<S>, S : Any {

    private var searchJob: Job? = null
    private var latestRequestId = 0

    fun onQueryChange(query: String) {
        searchJob?.cancel()
        val requestId = ++latestRequestId

        if (query.isBlank()) {
            host.intent {
                reduce { state.withSearch(query = query, searchUiState = DepartmentSearchUiState.Idle) }
            }
            return
        }

        host.intent {
            reduce { state.withSearch(query = query, searchUiState = DepartmentSearchUiState.Loading) }
        }

        searchJob = scope.launch {
            delay(SEARCH_DEBOUNCE_MILLIS)
            search(query, requestId)
        }
    }

    fun onSearch() {
        searchJob?.cancel()
        val query = host.container.stateFlow.value.query
        if (query.isBlank()) return
        onSearchLog(query)
        val requestId = ++latestRequestId

        host.intent {
            reduce { state.withSearch(searchUiState = DepartmentSearchUiState.Loading) }
        }

        searchJob = scope.launch { search(query, requestId) }
    }

    private suspend fun search(keyword: String, requestId: Int) {
        val result = fetch(keyword)
        if (requestId != latestRequestId) return

        host.intent {
            result
                .onSuccess { departments ->
                    reduce {
                        state.withSearch(
                            searchUiState = if (departments.isEmpty()) {
                                DepartmentSearchUiState.Empty
                            } else {
                                DepartmentSearchUiState.Success(departments.toImmutableList())
                            }
                        )
                    }
                }
                .onFailure {
                    reduce { state.withSearch(searchUiState = DepartmentSearchUiState.Failure) }
                }
        }
    }

    companion object {
        private const val SEARCH_DEBOUNCE_MILLIS = 300L
    }
}
