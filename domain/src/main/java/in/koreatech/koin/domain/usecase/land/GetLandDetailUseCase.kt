package `in`.koreatech.koin.domain.usecase.land

import `in`.koreatech.koin.domain.model.land.LandDetail
import `in`.koreatech.koin.domain.repository.LandRepository
import java.util.concurrent.CancellationException
import javax.inject.Inject

class GetLandDetailUseCase @Inject constructor(
    private val landRepository: LandRepository
) {
    suspend operator fun invoke(id: Int): Result<LandDetail> {
        return try {
            Result.success(landRepository.getLandDetail(id))
        } catch (e: CancellationException) {
            throw e
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
}