package `in`.koreatech.koin.feature.recruitment.ui.notification.model

import androidx.annotation.DrawableRes
import androidx.annotation.StringRes
import `in`.koreatech.koin.feature.recruitment.R

internal enum class RecruitmentNotificationCategory(
    @param:DrawableRes val drawableRes: Int,
    @param:StringRes val labelRes: Int
) {
    MESSAGE(R.drawable.ic_notification_recruitment_chat, R.string.recruitment_notification_category_message),
    APPLICATION_APPROVED(
        R.drawable.ic_notification_recruitment,
        R.string.recruitment_notification_category_application_approved
    ),
    APPLICATION_REJECTED(
        R.drawable.ic_notification_recruitment,
        R.string.recruitment_notification_category_application_rejected
    ),
    POST_DELETED(R.drawable.ic_notification_recruitment, R.string.recruitment_notification_category_post_deleted),
    RECRUITMENT_CLOSED(
        R.drawable.ic_notification_recruitment,
        R.string.recruitment_notification_category_recruitment_closed
    )
}
