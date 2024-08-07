package `in`.koreatech.koin.domain.usecase.store

import `in`.koreatech.koin.domain.model.user.Gender
import `in`.koreatech.koin.domain.model.user.Graduated
import `in`.koreatech.koin.domain.repository.SignupRepository
import `in`.koreatech.koin.domain.repository.StoreRepository
import `in`.koreatech.koin.domain.state.signup.SignupContinuationState
import `in`.koreatech.koin.domain.state.store.StoreReviewExceptionState
import `in`.koreatech.koin.domain.state.store.StoreReviewState
import javax.inject.Inject

class ReportStoreReviewUseCase @Inject constructor(
    private val storeRepository: StoreRepository
) {
    suspend operator fun invoke(
        storeId: Int?,
        reviewId: Int?,
        reportTitle: String,
        reportReason: String,
        isNotRelation: Boolean?,
        isSpam: Boolean?,
        isAbuse: Boolean?,
        isPrivate: Boolean?,
        isEtc: Boolean?,
    ): Result<StoreReviewState> {
        return when {
            !(isNotRelation == true || isSpam == true || isAbuse == true || isPrivate == true || isEtc == true)
            -> Result.failure(StoreReviewExceptionState.ToastNullCheckBox)

            else -> storeRepository.reportReview(storeId, reviewId, reportTitle ,reportReason).map{
               StoreReviewState.ReportComplete
            }
        }
    }
}