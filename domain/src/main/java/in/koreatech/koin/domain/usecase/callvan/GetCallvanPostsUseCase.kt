package `in`.koreatech.koin.domain.usecase.callvan

import `in`.koreatech.koin.domain.model.callvan.CallvanPostSearch
import `in`.koreatech.koin.domain.repository.CallvanRepository
import javax.inject.Inject

class GetCallvanPostsUseCase @Inject constructor(
    private val callvanRepository: CallvanRepository
) {
    suspend operator fun invoke(
        author: String? = "ALL",
        departures: List<String>? = null,
        departureKeyword: String? = null,
        arrivals: List<String>? = null,
        arrivalKeyword: String? = null,
        statuses: List<String>? = null,
        title: String? = null,
        sort: String? = "LATEST_DESC",
        page: Int? = 1,
        limit: Int? = 10
    ): Result<CallvanPostSearch> = callvanRepository.getCallvanPosts(
        author = author,
        departures = departures,
        departureKeyword = departureKeyword,
        arrivals = arrivals,
        arrivalKeyword = arrivalKeyword,
        statuses = statuses,
        title = title,
        sort = sort,
        page = page,
        limit = limit
    )
}
