package `in`.koreatech.koin.feature.lostandfound.enums

import androidx.annotation.StringRes
import `in`.koreatech.koin.feature.lostandfound.R

enum class ReportReason(@StringRes val titleRes: Int, @StringRes val descriptionRes: Int) {
    OFF_TOPIC(R.string.report_reason_off_topic_title, R.string.report_reason_off_topic_description),
    SPAM(R.string.report_reason_spam_title, R.string.report_reason_spam_description),
    INAPPROPRIATE_LANGUAGE(R.string.report_reason_inappropriate_title, R.string.report_reason_inappropriate_description),
    PRIVACY(R.string.report_reason_privacy_title, R.string.report_reason_privacy_description),
    OTHER(R.string.report_reason_other_title, R.string.report_reason_other_description)
}