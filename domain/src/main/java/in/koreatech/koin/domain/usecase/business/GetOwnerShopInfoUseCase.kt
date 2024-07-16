package `in`.koreatech.koin.domain.usecase.business

import `in`.koreatech.koin.domain.constant.ERROR_OWNERSHOP_NOT_SET
import `in`.koreatech.koin.domain.error.owner.OwnerErrorHandler
import `in`.koreatech.koin.domain.model.owner.StoreDetailInfo
import `in`.koreatech.koin.domain.repository.OwnerShopRepository
import javax.inject.Inject

class GetOwnerShopInfoUseCase @Inject constructor(
    private val storeRepository: OwnerShopRepository,
) {
    suspend operator fun invoke(
        storeId: Int
    ): Result<StoreDetailInfo?> {
        return try {
            if (storeId < 0) {
                throw IllegalArgumentException(ERROR_OWNERSHOP_NOT_SET)
            }
            Result.success(storeRepository.getOwnerShopInfo(storeId))

        } catch (throwable: Throwable) {
            Result.failure(throwable)
        }
    }
}