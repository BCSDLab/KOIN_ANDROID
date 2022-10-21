package `in`.koreatech.koin.domain.usecase.land

import `in`.koreatech.koin.domain.model.land.Land
import `in`.koreatech.koin.domain.repository.LandRepository
import java.util.concurrent.CancellationException
import javax.inject.Inject

class GetLandListUseCase @Inject constructor(
    private val landRepository: LandRepository
) {
    suspend operator fun invoke(): Result<List<Land>> {
        return try {
            Result.success(landRepository.getLandList())
        } catch (e: CancellationException) {
            throw e
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
}