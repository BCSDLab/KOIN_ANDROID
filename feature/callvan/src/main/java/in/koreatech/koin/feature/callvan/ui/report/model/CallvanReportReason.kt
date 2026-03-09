package `in`.koreatech.koin.feature.callvan.ui.report.model

import androidx.annotation.StringRes
import `in`.koreatech.koin.feature.callvan.R

enum class CallvanReportReason(
    @StringRes val titleRes: Int,
    @StringRes val descriptionRes: Int?
) {
    NO_SHOW(R.string.callvan_report_reason_no_show_title, R.string.callvan_report_reason_no_show_description),
    NON_PAYMENT(R.string.callvan_report_reason_non_payment_title, R.string.callvan_report_reason_non_payment_description),
    PROFANITY(R.string.callvan_report_reason_profanity_title, R.string.callvan_report_reason_profanity_description),
    OTHER(R.string.callvan_report_reason_other_title, null)
}
