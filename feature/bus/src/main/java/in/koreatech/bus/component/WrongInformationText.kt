package `in`.koreatech.bus.component

import android.content.Intent
import android.net.Uri
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import `in`.koreatech.koin.core.analytics.EventLogger
import `in`.koreatech.koin.core.designsystem.component.text.LeadingIconText
import `in`.koreatech.koin.core.designsystem.noRippleClickable
import `in`.koreatech.koin.core.designsystem.theme.KoinTheme
import `in`.koreatech.koin.feature.bus.R

@Composable
internal fun WrongInformationText(
    modifier: Modifier = Modifier,
    loggingEventValue: String = "",
) {
    val context = LocalContext.current

    LeadingIconText(
        modifier =
            modifier.noRippleClickable {
                EventLogger.logCampusClickEvent(
                    "error_feedback_button",
                    loggingEventValue,
                )
                val url = GOOGLE_FORM_URL
                val intent = Intent(Intent.ACTION_VIEW, Uri.parse(url))
                context.startActivity(intent)
            }.padding(top = 4.dp),
        text = stringResource(R.string.request_for_incorrect_information),
        iconRes = R.drawable.ic_caution,
        iconTint = KoinTheme.colors.neutral500,
        textStyle =
            KoinTheme.typography.regular12.copy(
                color = KoinTheme.colors.neutral500,
            ),
    )
}

private const val GOOGLE_FORM_URL = "https://docs.google.com/forms/d/1GR4t8IfTOrYY4jxq5YAS7YiCS8QIFtHaWu_kE-SdDKY"
