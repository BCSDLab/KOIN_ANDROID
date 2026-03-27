package `in`.koreatech.koin.domain.usecase.callvan

import `in`.koreatech.koin.domain.model.callvan.CallvanPostCreate
import `in`.koreatech.koin.domain.repository.CallvanRepository
import javax.inject.Inject

class CreateCallvanPostUseCase @Inject constructor(
    private val callvanRepository: CallvanRepository
) {
    suspend operator fun invoke(
        departureType: String,
        departureCustomName: String? = null,
        arrivalType: String,
        arrivalCustomName: String? = null,
        departureDate: String,
        departureTime: String,
        maxParticipants: Int
    ): Result<CallvanPostCreate> = callvanRepository.createCallvanPost(
        departureType = departureType,
        departureCustomName = departureCustomName,
        arrivalType = arrivalType,
        arrivalCustomName = arrivalCustomName,
        departureDate = departureDate,
        departureTime = departureTime,
        maxParticipants = maxParticipants
    )
}
