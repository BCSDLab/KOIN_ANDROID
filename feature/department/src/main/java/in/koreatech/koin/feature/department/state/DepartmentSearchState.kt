package `in`.koreatech.koin.feature.department.state

interface DepartmentSearchState<S : DepartmentSearchState<S>> {
    val query: String
    val searchUiState: DepartmentSearchUiState

    fun withSearch(
        query: String = this.query,
        searchUiState: DepartmentSearchUiState = this.searchUiState
    ): S
}
