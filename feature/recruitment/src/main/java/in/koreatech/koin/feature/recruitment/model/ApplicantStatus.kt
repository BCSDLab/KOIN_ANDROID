package `in`.koreatech.koin.feature.recruitment.model

import androidx.annotation.StringRes
import `in`.koreatech.koin.feature.recruitment.R

enum class ApplicantStatus(@StringRes val labelRes: Int) {
    PENDING(R.string.recruitment_applied_status_pending),
    APPROVED(R.string.recruitment_applied_status_approved),
    REJECTED(R.string.recruitment_applied_status_rejected)
}
