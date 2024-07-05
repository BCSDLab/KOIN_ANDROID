package `in`.koreatech.koin.domain.usecase.business

import `in`.koreatech.koin.domain.error.owner.OwnerErrorHandler
import `in`.koreatech.koin.domain.model.error.ErrorHandler
import `in`.koreatech.koin.domain.model.store.Store
import `in`.koreatech.koin.domain.repository.OwnerShopRepository
import javax.inject.Inject

class GetOwnerShopListUseCase @Inject constructor(
    private val storeRepository: OwnerShopRepository,
    private val errorHandler: OwnerErrorHandler,
) {
    suspend operator fun invoke(): Pair<List<Store>, ErrorHandler?> {
        return try {
            storeRepository.getMyShopList() to null
        } catch (throwable: Throwable) {
            emptyList<Store>() to errorHandler.handleGetOwnerShopListError(throwable)
        }
    }
}