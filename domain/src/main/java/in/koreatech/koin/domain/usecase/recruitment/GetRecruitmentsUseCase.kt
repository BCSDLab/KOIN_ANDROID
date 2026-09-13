package `in`.koreatech.koin.domain.usecase.recruitment

import `in`.koreatech.koin.domain.model.recruitment.Recruitments
import `in`.koreatech.koin.domain.repository.RecruitmentRepository
import javax.inject.Inject

class GetRecruitmentsUseCase @Inject constructor(
    private val recruitmentRepository: RecruitmentRepository
) {
    @Suppress("LongParameterList")
    suspend operator fun invoke(
        keyword: String? = null,
        status: String? = null,
        categories: List<String>? = null,
        meetingType: String? = null,
        sort: String? = null,
        page: Int = DEFAULT_PAGE,
        limit: Int = DEFAULT_LIMIT
    ): Result<Recruitments> = recruitmentRepository.getRecruitments(
        keyword = keyword,
        status = status,
        categories = categories,
        meetingType = meetingType,
        sort = sort,
        page = page,
        limit = limit
    )

    companion object {
        const val DEFAULT_PAGE = 1
        const val DEFAULT_LIMIT = 10
    }
}
