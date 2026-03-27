package `in`.koreatech.koin.domain.usecase.callvan

import `in`.koreatech.koin.domain.model.callvan.CallvanPostSearch
import `in`.koreatech.koin.domain.repository.CallvanRepository
import javax.inject.Inject

class GetCallvanPostsUseCase @Inject constructor(
    private val callvanRepository: CallvanRepository
) {
    suspend operator fun invoke(
        author: String?,
        departures: List<String>?,
        departureKeyword: String?,
        arrivals: List<String>?,
        arrivalKeyword: String?,
        statuses: List<String>?,
        title: String?,
        sort: String?,
        joined: Boolean,
        page: Int?,
        limit: Int?
    ): Result<CallvanPostSearch> = callvanRepository.getCallvanPosts(
        author = author,
        departures = departures,
        departureKeyword = departureKeyword,
        arrivals = arrivals,
        arrivalKeyword = arrivalKeyword,
        statuses = statuses,
        title = title,
        sort = sort,
        joined = joined,
        page = page,
        limit = limit
    )
}
